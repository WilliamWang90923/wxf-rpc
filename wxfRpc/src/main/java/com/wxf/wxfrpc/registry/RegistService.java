package com.wxf.wxfrpc.registry;

import java.net.URI;
import java.net.URISyntaxException;

public interface RegistService {

    void register(URI uri);

    void subscribe(String service, NotifyListener listener) throws URISyntaxException;

    void init(URI address);
}
