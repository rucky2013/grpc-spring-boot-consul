package com.andy.gsbc.register;

/**
 * Created by 延泽 on 3/6 0006.
 */
public interface RpcRegister {
    void registerRpc(ServiceInfo serviceInfo);

    void unRegisterRpc(String serviceId);
}
