package cn.ocoop.ssf.config.vo;

/**
 * Created by liolay on 15-7-29.
 */
public class ResourceConfig {
    private String prefix;
    private String version;

    public ResourceConfig() {
    }

    public ResourceConfig(String prefix, String version) {
        this.prefix = prefix;
        this.version = version;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
