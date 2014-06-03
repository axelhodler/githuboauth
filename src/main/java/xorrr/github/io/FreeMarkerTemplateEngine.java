package xorrr.github.io;

import java.io.StringWriter;

import spark.ModelAndView;
import spark.TemplateEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerTemplateEngine extends TemplateEngine {

    private Configuration config;

    protected FreeMarkerTemplateEngine() {
        this.config = createFreemarkerConfiguration();
    }

    @Override
    public String render(ModelAndView modelAndView) {
        StringWriter sw = new StringWriter();
        try {
            Template t = config.getTemplate(modelAndView.getViewName());
            t.process(modelAndView.getModel(), sw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    private Configuration createFreemarkerConfiguration() {
        Configuration c = new Configuration();
        c.setClassForTemplateLoading(FreeMarkerTemplateEngine.class, "freemarker");
        return c;
    }
}
