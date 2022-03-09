package com.wxf.wxfrpc.registry.redis;

import com.wxf.wxfrpc.registry.NotifyListener;
import com.wxf.wxfrpc.registry.RegistService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RedisRegistry implements RegistService {

    private static final int TIME_OUT = 15;
    private URI address;
    private JedisPubSub jedisPubSub;

    ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5);

    // consumer info
    // key: service name
    Map<String, Set<URI>> localCache = new ConcurrentHashMap<>();
    Map<String, NotifyListener> listenerMap = new ConcurrentHashMap<>();

    // server info
    List<URI> serviceLsForHeartbeat = new ArrayList<>();

    @Override
    public void register(URI uri) {
        String key = "wxfrpc:" + uri.toString();
        Jedis jedis = new Jedis(address.getHost(), address.getPort());
        jedis.setex(key, TIME_OUT, String.valueOf(System.currentTimeMillis()));
        jedis.close();
        serviceLsForHeartbeat.add(uri);
    }

    @Override
    public void subscribe(String service, NotifyListener listener) throws URISyntaxException {
        // first time here, subscribe
        if (localCache.get(service) == null) {
            localCache.putIfAbsent(service, new HashSet<>());
            Jedis jedis = new Jedis(address.getHost(), address.getPort());
            Set<String> serviceInstanceUris = jedis.keys("wxfrpc:*" + service + "/*");

            for (String serviceUri : serviceInstanceUris) {
                localCache.get(service).add(new URI(serviceUri));
            }
            // notify
            listener.notify(localCache.get(service));
            listenerMap.putIfAbsent(service, listener);
            jedis.close();
        }
    }

    @Override
    public void init(URI address) {
        this.address = address;

        executorService.scheduleWithFixedDelay(() -> {
            // send heart beat
            Jedis jedis = new Jedis(address.getHost(), address.getPort());

            try {
                for (URI uri : serviceLsForHeartbeat) {
                    String key = "wxfrpc:" + uri.toString();
                    jedis.expire(key, TIME_OUT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                jedis.close();
            }
        }, 3000, 5000, TimeUnit.MILLISECONDS);

        executorService.execute(() -> {
            jedisPubSub = new JedisPubSub() {
                @Override
                public void onPMessage(String pattern, String channel, String message) {
                    try {
                        URI serviceURI = new URI(channel.replace("__keyspace@0__:", ""));
                        String serviceName = serviceURI.getPath().replace("/","");
                        // System.out.println(serviceName);
                        if("set".equals(message)) {
                            // 本地增加 一个服务实例信息
                            Set<URI> uris = localCache.get(serviceName);
                            // 收到的服务实例变动，可能与此消费者无关，此处null判断
                            if(uris != null) {
                                uris.add(serviceURI);
                            }
                        }
                        if ("expired".equals(message)) {
                            Set<URI> uris = localCache.get(serviceName);
                            if(uris != null) {
                                uris.remove(serviceURI);
                            }
                        }
                        if("expired".equals(message) || "set".equals(message)) {
                            NotifyListener notifyListener = listenerMap.get(serviceName);
                            if(notifyListener != null) {
                                notifyListener.notify(localCache.get(serviceName));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            Jedis jedis = new Jedis(address.getHost(), address.getPort());
            jedis.psubscribe(jedisPubSub, "__keyspace@0__:wxfrpc:*");
            jedis.close();
        });
    }
}
