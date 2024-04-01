package bupt.edu.jhc.jrpc.registry;

import bupt.edu.jhc.jrpc.config.RegistryConfig;
import bupt.edu.jhc.jrpc.domain.ServiceMetaInfo;
import cn.hutool.json.JSONUtil;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

/**
 * @Description: Etcd 注册中心
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/25
 */
public class EtcdRegistry implements Registry {

    private Client client; // Etcd 客户端
    private KV kvClient; // Etcd KV 客户端
    private static final String ETCD_ROOT_PATH = "/rpc/"; // Etcd 根路径

    @Override
    public void init(RegistryConfig config) {
        client = Client.builder()
                .endpoints(config.getAddress())
                .connectTimeout(Duration.ofMillis(config.getTimeout()))
                .build();
        kvClient = client.getKVClient();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 1.创建一个 Lease 客户端
        var leaseClient = client.getLeaseClient();

        // 2.创建一个 30s 的租约
        var leaseId = leaseClient.grant(30).get().getID();

        // 3.创建要存储的键值对
        var registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        var key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        var value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // 4.将键值对存入 Etcd
        kvClient.put(key, value, PutOption.builder().withLeaseId(leaseId).build()).get();
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) {
        kvClient.delete(ByteSequence.from(ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey(), StandardCharsets.UTF_8));
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        var searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";

        try {
            // 前缀查询
            var getOption = GetOption.builder()
                    .isPrefix(true)
                    .build();
            List<KeyValue> results = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption)
                    .get()
                    .getKvs();

            // 解析服务信息
            return results.stream().map(kv -> JSONUtil.toBean(kv.getValue().toString(StandardCharsets.UTF_8), ServiceMetaInfo.class))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败", e);
        }
    }

    @Override
    public void destroy() {
        System.out.println("当前节点下线");
        // 释放资源
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }
}
