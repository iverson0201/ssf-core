package cn.ocoop.ssf.freemarker.view;


import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import java.util.Set;

/**
 * Created by liolay on 15-12-14.
 */
public class FreeMarkerViewResolver extends org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver {
    private String remoteSuffix;
    private Set<String> remotePath;

    public void setRemoteSuffix(String remoteSuffix) {
        this.remoteSuffix = remoteSuffix;
    }

    public void setRemotePath(Set<String> remotePath) {
        this.remotePath = remotePath;
    }

    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        AbstractUrlBasedView view = (AbstractUrlBasedView) BeanUtils.instantiateClass(getViewClass());
        view.setUrl(getPrefix() + viewName + getSuffix(viewName));

        String contentType = getContentType();
        if (contentType != null) {
            view.setContentType(contentType);
        }

        view.setRequestContextAttribute(getRequestContextAttribute());
        view.setAttributesMap(getAttributesMap());

        Boolean exposePathVariables = getExposePathVariables();
        if (exposePathVariables != null) {
            view.setExposePathVariables(exposePathVariables);
        }
        Boolean exposeContextBeansAsAttributes = getExposeContextBeansAsAttributes();
        if (exposeContextBeansAsAttributes != null) {
            view.setExposeContextBeansAsAttributes(exposeContextBeansAsAttributes);
        }
        String[] exposedContextBeanNames = getExposedContextBeanNames();
        if (exposedContextBeanNames != null) {
            view.setExposedContextBeanNames(exposedContextBeanNames);
        }

        return view;
    }

    protected String getSuffix(String viewName) {
        if (hasRemotePath() && hasPathMatch(viewName)) return remoteSuffix;
        return super.getSuffix();
    }

    public boolean hasRemotePath() {
        return this.remotePath != null && this.remotePath.size() > 0;
    }

    public boolean hasPathMatch(String viewName) {
        if (CollectionUtils.isEmpty(remotePath)) return false;
        return remotePath.stream().anyMatch(path -> viewName.matches(path));
    }

}
