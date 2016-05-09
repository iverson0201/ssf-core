package cn.ocoop.ssf.freemarker;

import cn.ocoop.ssf.freemarker.view.FreeMarkerViewResolver;
import cn.ocoop.ssf.spring.AppContext;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liolay on 15-7-28.
 */
public class FreeMarkerViewExtension extends FreeMarkerView {
    private static final Pattern UNDERSCORE_SCRIPT_PATTERN = Pattern.compile("<%(.|\\s)*?%>");
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("^[d|D]-(.*)$");
    private static final ThreadLocal<Map<String, String>> UNDERSCORE_SCRIPT = new ThreadLocal<>();

    @Override
    protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.put("ctx", request.getContextPath());
        super.doRender(model, request, response);
    }

    protected void processTemplate(Template template, SimpleHash model, HttpServletResponse response)
            throws IOException, TemplateException {
        FreeMarkerViewResolver freeMarkerViewResolver = AppContext.getBean(FreeMarkerViewResolver.class);
        if (!freeMarkerViewResolver.hasRemotePath() || !freeMarkerViewResolver.hasPathMatch(template.getName())) {
            super.processTemplate(template, model, response);
            return;
        }
        try (
                StringWriter writer = new StringWriter();
                PrintWriter out = response.getWriter()
        ) {
            template.process(model, writer);

            out.print(processTemplate(writer.toString()));
            out.flush();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    public String processTemplate(String html) throws IOException, ScriptException, NoSuchMethodException {
        html = html.replaceAll("((?i)((</?)(table|thead|tr|th|td)))", "$3t-t-$4");
        Document doc = Jsoup.parse(
                escapeUnderscoreScript(
                        html,
                        matchEnd -> "%underscore-script%" + matchEnd
                )
        );
        doc.select(".template-no").remove();
        doc.outputSettings().escapeMode(Entities.EscapeMode.none);
        saxCustomAttributes(doc);
        return unescapeUnderscoreScript((doc.html())).replaceAll("(</?)t-t-(table|thead|tr|th|td)", "$1$2");
    }

    private void saxCustomAttributes(Document doc) {
        Elements allElements = doc.getAllElements();
        allElements.forEach(element ->
                element.attributes().forEach(attribute -> {
                    Matcher matcher = ATTRIBUTE_PATTERN.matcher(attribute.getKey());
                    if (matcher.find()) {
                        element.attr(matcher.group(1), attribute.getValue()).attr(attribute.getKey(), false);
                    }
                })
        );

    }

    private String unescapeUnderscoreScript(String escapedTemplateSource) {
        Map<String, String> underscoreScript = UNDERSCORE_SCRIPT.get();
        if (underscoreScript == null) return escapedTemplateSource;

        for (String scriptIndex : underscoreScript.keySet()) {
            escapedTemplateSource = escapedTemplateSource.replace(scriptIndex, underscoreScript.get(scriptIndex));
        }
        return escapedTemplateSource;
    }

    private String escapeUnderscoreScript(String templateSource, Function<String, String> mapper) throws IOException {
        Matcher matcher = UNDERSCORE_SCRIPT_PATTERN.matcher(templateSource);
        Map<String, String> underScriptIndex = new HashMap<>();

        while (matcher.find()) {
            String scriptIndex = mapper.apply(UUID.randomUUID().toString());//"%underscore-script%" + matcher.end();
            underScriptIndex.put(scriptIndex, matcher.group());
            templateSource = matcher.replaceFirst(scriptIndex);
            matcher.reset(templateSource);
        }
        UNDERSCORE_SCRIPT.set(underScriptIndex);
        return templateSource;
    }

}