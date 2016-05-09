package cn.ocoop.ssf.freemarker;

import cn.ocoop.ssf.freemarker.cache.MultiTemplateLoader;
import cn.ocoop.ssf.freemarker.config.Configuration;
import cn.ocoop.ssf.freemarker.spring.FreeMarkerConfigurer;
import cn.ocoop.ssf.shiro.tag.ShiroTags;
import freemarker.cache.TemplateLoader;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;

/**
 * Created by liolay on 15-7-28.
 */
public class ShiroTagFreeMarkerConfigurer extends FreeMarkerConfigurer {
    private boolean enhance = false;

    public void setEnhance(boolean enhance) {
        this.enhance = enhance;
    }

    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
    }

    protected freemarker.template.Configuration newConfiguration(TemplateLoader templateLoader) throws IOException, TemplateException {
        // The default Configuration constructor is deprecated as of FreeMarker 2.3.21,
        // in favor of specifying a compatibility version - which is a 2.3.21 feature.
        // We won't be able to call that for a long while, but custom subclasses can.
        return enhance ? new Configuration(templateLoader) : new freemarker.template.Configuration();
    }

    protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders) {
        int loaderCount = templateLoaders.size();
        switch (loaderCount) {
            case 0:
                logger.info("No FreeMarker TemplateLoaders specified");
                return null;
            case 1:
                return templateLoaders.get(0);
            default:
                TemplateLoader[] loaders = templateLoaders.toArray(new TemplateLoader[loaderCount]);
                return new MultiTemplateLoader(loaders);
        }
    }
}
