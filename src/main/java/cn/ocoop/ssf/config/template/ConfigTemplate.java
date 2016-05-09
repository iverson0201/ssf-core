package cn.ocoop.ssf.config.template;

import cn.ocoop.ssf.config.vo.RedisConfig;
import cn.ocoop.ssf.spring.AppContext;

/**
 * Created by liolay on 15-7-29.
 */
public abstract class ConfigTemplate {

    private RedisConfig redisConfig;

    public static ConfigTemplate getConfigBean() {
        return AppContext.getBean(ConfigTemplate.class);
    }

    public RedisConfig getRedisConfig() {
        return getRedis();
    }

    public void setRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    protected RedisConfig getRedis() {
        return new RedisConfig();
    }

    public String getRemoteTemplatePath(){
        return "http://localhost:8080/views/";
    }

}
