package com.example.adapter.template;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PathTemplate {
    private static final Pattern PARAM_PATTERN = Pattern.compile("\\{([A-Za-z0-9_]+)\\}");

    private final String template;
    private final Pattern regex;
    private final List<String> paramNames;
    private final List<Segment> segments;

    public PathTemplate(String template) {
        this.template = normalize(template);
        this.paramNames = new ArrayList<>();
        this.regex = Pattern.compile(toRegex(this.template, this.paramNames));
        this.segments = parseSegments(this.template);
    }

    public Map<String, String> match(String path) {
        Matcher m = regex.matcher(normalize(path));
        if (!m.matches()) return null;
        Map<String, String> vars = new LinkedHashMap<>();
        for (String name : paramNames) {
            vars.put(name, m.group(name));
        }
        return vars;
    }

    public String render(Map<String, String> vars) {
        StringBuilder out = new StringBuilder();
        for (Segment s : segments) {
            if (!s.param()) {
                out.append(s.value());
            } else {
                String v = vars.get(s.value());
                if (v == null || v.isBlank()) {
                    throw new IllegalArgumentException("Missing required path param: " + s.value());
                }
                out.append(URLEncoder.encode(v, StandardCharsets.UTF_8));
            }
        }
        return out.toString();
    }

    private static String toRegex(String template, List<String> names) {
        StringBuilder regex = new StringBuilder("^");
        Matcher m = PARAM_PATTERN.matcher(template);
        int last = 0;
        while (m.find()) {
            regex.append(Pattern.quote(template.substring(last, m.start())));
            String name = m.group(1);
            names.add(name);
            regex.append("(?<").append(name).append(">[^/]+)");
            last = m.end();
        }
        regex.append(Pattern.quote(template.substring(last)));
        regex.append("$");
        return regex.toString();
    }

    private static List<Segment> parseSegments(String template) {
        List<Segment> list = new ArrayList<>();
        Matcher m = PARAM_PATTERN.matcher(template);
        int last = 0;
        while (m.find()) {
            if (m.start() > last) list.add(new Segment(false, template.substring(last, m.start())));
            list.add(new Segment(true, m.group(1)));
            last = m.end();
        }
        if (last < template.length()) list.add(new Segment(false, template.substring(last)));
        return List.copyOf(list);
    }

    private static String normalize(String value) {
        if (value == null || value.isBlank()) return "/";
        String v = value.startsWith("/") ? value : "/" + value;
        return v.replaceAll("/+", "/");
    }

    private record Segment(boolean param, String value) {}
}
