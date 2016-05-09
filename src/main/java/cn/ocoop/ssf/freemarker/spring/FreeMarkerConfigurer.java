package cn.ocoop.ssf.freemarker.spring;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.List;

/**
 * Created by liolay on 15-12-14.
 */
public class FreeMarkerConfigurer extends FreeMarkerConfigurationFactory
        implements FreeMarkerConfig, InitializingBean, ResourceLoaderAware, ServletContextAware {
    private Configuration configuration;

    private TaglibFactory taglibFactory;


    /**
     * Set a preconfigured Configuration to use for the FreeMarker web config, e.g. a
     * shared one for web and email usage, set up via FreeMarkerConfigurationFactoryBean.
     * If this is not set, FreeMarkerConfigurationFactory's properties (inherited by
     * this class) have to be specified.
     *
     * @see org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Initialize the {@link TaglibFactory} for the given ServletContext.
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.taglibFactory = new TaglibFactory(servletContext);
    }


    /**
     * Initialize FreeMarkerConfigurationFactory's Configuration
     * if not overridden by a preconfigured FreeMarker Configuation.
     * <p>Sets up a ClassTemplateLoader to use for loading Spring macros.
     *
     * @see #createConfiguration
     * @see #setConfiguration
     */
    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        if (this.configuration == null) {
            this.configuration = createConfiguration();
        }
    }

    /**
     * This implementation registers an additional ClassTemplateLoader
     * for the Spring-provided macros, added to the end of the list.
     */
    @Override
    protected void postProcessTemplateLoaders(List<TemplateLoader> templateLoaders) {
        templateLoaders.add(new ClassTemplateLoader(FreeMarkerConfigurer.class, ""));
        logger.info("ClassTemplateLoader for Spring macros added to FreeMarker configuration");
    }


    /**
     * Return the Configuration object wrapped by this bean.
     */
    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    /**
     * Return the TaglibFactory object wrapped by this bean.
     */
    @Override
    public TaglibFactory getTaglibFactory() {
        return this.taglibFactory;
    }

}
