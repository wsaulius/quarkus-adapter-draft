package com.example.adapter.template;

import java.util.*;
import java.util.regex.*;

public final class InboundPathMatcher {
    private final Pattern regex;
    private final List<String> names = new ArrayList<>();

    public InboundPathMatcher(String template) {
        String t = template.startsWith("/") ? template : "/" + template;
        this.regex = Pattern.compile(toRegex(t));
    }

    private String toRegex(String t) {
        StringBuilder sb = new StringBuilder("^");
        Matcher m = Pattern.compile("\\{([A-Za-z0-9_]+)\\}").matcher(t);
        int last = 0;
        while (m.find()) {
            sb.append(Pattern.quote(t.substring(last, m.start())));
            String n = m.group(1);
            names.add(n);
            sb.append("(?<").append(n).append(">[^/]+)");
            last = m.end();
        }
        sb.append(Pattern.quote(t.substring(last))).append("$");
        return sb.toString();
    }

    public Map<String,String> match(String path) {
        String p = path.startsWith("/") ? path : "/" + path;
        Matcher m = regex.matcher(p);
        if (!m.matches()) return null;
        Map<String,String> out = new LinkedHashMap<>();
        for (String n : names) out.put(n, m.group(n));
        return out;
    }
}
