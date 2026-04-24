package com.example.adapter.template;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.*;

public final class TemplateRenderer {
    private TemplateRenderer() {}
    public static String render(String template, Map<String,String> vars) {
        Matcher m = Pattern.compile("\\{([A-Za-z0-9_]+)\\}").matcher(template);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String name = m.group(1);
            String value = vars.get(name);
            if (value == null) throw new IllegalArgumentException("Missing required template variable: " + name);
            m.appendReplacement(sb, Matcher.quoteReplacement(URLEncoder.encode(value, StandardCharsets.UTF_8)));
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
