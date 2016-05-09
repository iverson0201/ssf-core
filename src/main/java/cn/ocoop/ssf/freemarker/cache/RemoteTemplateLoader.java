package cn.ocoop.ssf.freemarker.cache;

import freemarker.cache.URLTemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by liolay on 15-12-2.
 */
public class RemoteTemplateLoader extends URLTemplateLoader {
    private static final Logger log = LoggerFactory.getLogger(RemoteTemplateLoader.class);

    private String templateLoaderPath;
    private Set<String> remotePaths;

    private static boolean isSchemeless(String fullPath) {
        int i = 0;
        int ln = fullPath.length();

        // Skip a single initial /, as things like "/file:/..." might work:
        if (i < ln && fullPath.charAt(i) == '/') i++;

        // Check if there's no ":" earlier than a '/', as the URLClassLoader
        // could interpret that as an URL scheme:
        while (i < ln) {
            char c = fullPath.charAt(i);
            if (c == '/') return true;
            if (c == ':') return false;
            i++;
        }
        return true;
    }

    @Override
    protected URL getURL(String name) {
        String fullPath = this.templateLoaderPath + name;
        if ("/".equals(this.templateLoaderPath) && !isSchemeless(fullPath)) return null;
        URL url = null;
        try {
            if (noIncludePath() || noPathMatch(name)) {
//                log.error("{}不在RemoteTemplateLoader的处理范围内", name);
                return null;
            }
            url = new URL(fullPath);
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("HEAD");
            url = con.getResponseCode() == HttpURLConnection.HTTP_OK ? url : null;
        } catch (MalformedURLException e) {
            log.error("获取远程模板出错,{}", fullPath, e);
        } catch (IOException e) {
            log.error("获取远程模板出错,{}", fullPath, e);
        }
        return url;
    }

    public String getTemplateLoaderPath() {
        return templateLoaderPath;
    }

    public void setTemplateLoaderPath(String templateLoaderPath) {
        if (templateLoaderPath == null)
            throw new IllegalArgumentException("未设置远程模板路径!");
        this.templateLoaderPath = canonicalizePrefix(templateLoaderPath);
        if (this.templateLoaderPath.indexOf('/') == 0) {
            this.templateLoaderPath = this.templateLoaderPath.substring(this.templateLoaderPath.indexOf('/') + 1);
        }
    }

//    @Override
//    public Object findTemplateSource(String name) throws IOException {
//        if (noIncludePath() || noPathMatch(name)) {
//            log.error("{}不在RemoteTemplateLoader的处理范围内", name);
//            return null;
//        }
//        return super.findTemplateSource(name);
//    }

    private boolean noIncludePath() {
        return this.remotePaths == null || this.remotePaths.isEmpty();
    }

    private boolean noPathMatch(String name) {
        return this.remotePaths.stream().allMatch(path -> !Pattern.compile(path).asPredicate().test(name));
    }

    public Set<String> getRemotePaths() {
        return remotePaths;
    }

    public void setRemotePaths(Set<String> remotePaths) {
        this.remotePaths = remotePaths;
    }

    @Override
    public String toString() {
        return "RemoteTemplateLoader{" +
                "templateLoaderPath='" + templateLoaderPath + '\'' +
                ", includePaths=" + remotePaths +
                '}';
    }
}
