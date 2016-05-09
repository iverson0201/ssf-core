package cn.ocoop.ssf.config.vo;

/**
 * Created by liolay on 15-7-29.
 */
public class RedisConfig {
    private String host;
    private String port;
    private String timeout;
    private String password;

    public RedisConfig(String host, String port, String password, String timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.password = password;
    }

    public RedisConfig() {

    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}