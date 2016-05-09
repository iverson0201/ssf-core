//package com.raiyee.hi.boss.redis;
//
//import com.raiyee.hi.boss.config.template.ConfigTemplate;
//import com.raiyee.hi.boss.config.vo.RedisConfig;
//import org.apache.commons.lang3.StringUtils;
//import redis.clients.jedis.JedisPoolConfig;
//import redis.clients.jedis.JedisShardInfo;
//import redis.clients.jedis.ShardedJedis;
//import redis.clients.jedis.ShardedJedisPool;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by liolay on 15-7-27.
// */
//public final class RedisUtil {
//
//    private static ShardedJedisPool jedisPool = null;
//
//    static {
//        List<JedisShardInfo> shards = getJedisShardsInfo();
//
//        configJedisPool(shards);
//    }
//
//    public static void shutdown() {
//        if (jedisPool != null) jedisPool.destroy();
//    }
//
//    private static void configJedisPool(List<JedisShardInfo> shards) {
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(50);
//        poolConfig.setMaxIdle(5);
//        poolConfig.setMaxWaitMillis(1000 * 10);
//        poolConfig.setTestOnBorrow(true);
//
//        jedisPool = new ShardedJedisPool(poolConfig, shards);
//    }
//
//    private static List<JedisShardInfo> getJedisShardsInfo() {
//        ConfigTemplate configTemplate = ConfigTemplate.getConfigBean();
//        RedisConfig redisConfig = configTemplate.getRedisConfig();
//
//        if (StringUtils.isBlank(redisConfig.getHosts())) throw new RuntimeException("redis hosts未配置");
//
//        List<JedisShardInfo> shards = new ArrayList<>();
//        String[] hosts = redisConfig.getHosts().split(",");
//        for (String host : hosts) {
//            String[] shardConfig = host.split(":", 3);
//            JedisShardInfo shard = new JedisShardInfo(shardConfig[0], Integer.parseInt(shardConfig[1]));
//            if (shardConfig.length == 3) shard.setPassword(shardConfig[2]);
//            shard.setTimeout(Integer.parseInt(redisConfig.getTimeout()));
//            shards.add(shard);
//        }
//        return shards;
//    }
//
//
//    private static ShardedJedis getJedis() {
//
//        try {
//            ShardedJedis shardedJedis = jedisPool.getResource();
//            return shardedJedis;
//        } catch (Exception e) {
//            throw e;
//        }
//
//    }
//
//    public static <T> T post(JedisPostFunction<T> postFunction) {
//
//        ShardedJedis jedis = null;
//
//        try {
//            jedis = getJedis();
//            return postFunction.post(jedis);
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            jedis.close();
//        }
//    }
//
//    public static interface JedisPostFunction<T> {
//        public T post(ShardedJedis jedis);
//    }
//
//
//}
