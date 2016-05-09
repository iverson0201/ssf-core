package cn.ocoop.ssf.freemarker.cache;

import freemarker.cache.StatefulTemplateLoader;
import freemarker.cache.TemplateLoader;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liolay on 15-12-9.
 */
public class MultiTemplateLoader implements StatefulTemplateLoader
{
    private final TemplateLoader[] loaders;
    private final Map lastLoaderForName = Collections.synchronizedMap(new HashMap());

    /**
     * Creates a new multi template Loader that will use the specified loaders.
     * @param loaders the loaders that are used to load templates.
     */
    public MultiTemplateLoader(TemplateLoader[] loaders)
    {
        this.loaders = (TemplateLoader[])loaders.clone();
    }

    public Object findTemplateSource(String name)
            throws
            IOException
    {
        // Use soft affinity - give the loader that last found this
        // resource a chance to find it again first.
        TemplateLoader lastLoader = (TemplateLoader)lastLoaderForName.get(name);
        if(lastLoader != null)
        {
            Object source = lastLoader.findTemplateSource(name);
            if(source != null)
            {
                return new MultiSource(source, lastLoader);
            }
        }

        // If there is no affine loader, or it could not find the resource
        // again, try all loaders in order of appearance. If any manages
        // to find the resource, then associate it as the new affine loader
        // for this resource.
        for(int i = 0; i < loaders.length; ++i)
        {
            TemplateLoader loader = loaders[i];
            Object source = loader.findTemplateSource(name);
            if(source != null)
            {
                lastLoaderForName.put(name, loader);
                return new MultiSource(source, loader);
            }
        }

        lastLoaderForName.remove(name);
        // Resource not found
        return null;
    }

    private Object modifyForIcI(Object source) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getLastModified(Object templateSource)
    {
        return ((MultiSource)templateSource).getLastModified();
    }

    public Reader getReader(Object templateSource, String encoding)
            throws
            IOException
    {
        return ((MultiSource)templateSource).getReader(encoding);
    }

    public void closeTemplateSource(Object templateSource)
            throws
            IOException
    {
        ((MultiSource)templateSource).close();
    }

    public void resetState()
    {
        lastLoaderForName.clear();
        for (int i = 0; i < loaders.length; i++) {
            TemplateLoader loader = loaders[i];
            if(loader instanceof StatefulTemplateLoader) {
                ((StatefulTemplateLoader)loader).resetState();
            }
        }
    }

    /**
     * Represents a template source bound to a specific template loader. It
     * serves as the complete template source descriptor used by the
     * MultiTemplateLoader class.
     */
    public static final class MultiSource
    {
        private final Object source;
        private final TemplateLoader loader;

        MultiSource(Object source, TemplateLoader loader)
        {
            this.source = source;
            this.loader = loader;
        }

        long getLastModified()
        {
            return loader.getLastModified(source);
        }

        Reader getReader(String encoding)
                throws
                IOException
        {
            return loader.getReader(source, encoding);
        }

        void close()
                throws
                IOException
        {
            loader.closeTemplateSource(source);
        }

        Object getWrappedSource() {
            return source;
        }

        public boolean equals(Object o) {
            if(o instanceof MultiSource) {
                MultiSource m = (MultiSource)o;
                return m.loader.equals(loader) && m.source.equals(source);
            }
            return false;
        }

        public int hashCode() {
            return loader.hashCode() + 31 * source.hashCode();
        }

        public String toString() {
            return source.toString();
        }
    }

    /**
     * Show class name and some details that are useful in template-not-found errors.
     *
     * @since 2.3.21
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("MultiTemplateLoader(");
        for (int i = 0; i < loaders.length; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("loader").append(i + 1).append(" = ").append(loaders[i]);
        }
        sb.append(")");
        return sb.toString();
    }

}
