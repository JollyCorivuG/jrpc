package bupt.edu.jhc.jrpc.registry;

import bupt.edu.jhc.jrpc.config.RegistryConfig;
import bupt.edu.jhc.jrpc.domain.dto.service.ServiceMetaInfo;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: Etcd 注册中心
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/3/25
 */
@Slf4j
public class EtcdRegistry implements Registry {

    private Client client; // Etcd 客户端
    private KV kvClient; // Etcd KV 客户端
    private static final String ETCD_ROOT_PATH = "/rpc/"; // Etcd 根路径
    private static final Set<String> localRegisterNodeKeys = new HashSet<>(); // 本地注册节点 key 集合 (用于维护续期)
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache(); // 注册中心服务端缓存
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    @Override
    public void init(RegistryConfig config) {
        client = Client.builder()
                .endpoints(config.getAddress())
                .connectTimeout(Duration.ofMillis(config.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        // 开启心跳检测
        heartBeat();
    }

    @Override
    public void heartBeat() {
        // 使用 hutool 定时任务工具, 10s 续签一次
        CronUtil.schedule("*/10 * * * * *", (Task) () -> {
            // 遍历本节点所有的 key
            for (var key : localRegisterNodeKeys) {
                try {
                    var keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                            .get()
                            .getKvs();
                    // 该节点已过期（需要重启节点才能重新注册）
                    if (CollUtil.isEmpty(keyValues)) {
                        continue;
                    }
                    // 节点未过期，重新注册（相当于续签）
                    var keyValue = keyValues.getFirst();
                    var value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                    var serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                    register(serviceMetaInfo);
                } catch (Exception e) {
                    throw new RuntimeException(key + "续签失败", e);
                }
            }
        });

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
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

        // 5.添加节点信息到注册中心本地缓存
        localRegisterNodeKeys.add(registerKey);
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) {
        var registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registerKey, StandardCharsets.UTF_8));
        localRegisterNodeKeys.remove(registerKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        // 优先从缓存获取服务
        var cachedServiceMetaInfoList = registryServiceCache.readCache();
        if (cachedServiceMetaInfoList != null) {
            log.info("从缓存获取服务列表：{}", cachedServiceMetaInfoList);
            return cachedServiceMetaInfoList;
        }

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
            var handledResult = results.stream().map(kv -> {
                var key = kv.getKey().toString(StandardCharsets.UTF_8);
                // 监听 key 的变化
                watch(key);
                var value = kv.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).toList();

            // 写入服务缓存
            registryServiceCache.writeCache(handledResult);
            return handledResult;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败", e);
        }
    }

    @Override
    public void watch(String serviceNodeKey) {
        var watchClient = client.getWatchClient();
        // 之前未被监听，开启监听
        if (watchingKeySet.add(serviceNodeKey)) {
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8), response -> {
                for (var event : response.getEvents()) {
                    switch (event.getEventType()) {
                        // key 删除时触发
                        case DELETE -> registryServiceCache.clearCache(); // 清空缓存
                        case PUT -> {
                        }
                        default -> {

                        }
                    }
                }
            });
        }
    }

    @Override
    public void destroy() {
        System.out.println("当前节点下线");

        // 遍历本节点所有的 key
        for (var key : localRegisterNodeKeys) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(key + "节点下线失败");
            }
        }

        // 释放资源
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }
}
