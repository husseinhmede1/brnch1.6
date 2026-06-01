package com.mdsl.utils;

import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class CustomContentCachingRequestWrapper extends ContentCachingRequestWrapper {

    private final Map<String, String> customHeaders = new HashMap<>();

    public CustomContentCachingRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String value = customHeaders.get(name);
        return value != null ? value : super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values = new ArrayList<>();

        if (customHeaders.containsKey(name)) {
            values.add(customHeaders.get(name));
        }

        Enumeration<String> e = super.getHeaders(name);
        while (e.hasMoreElements()) {
            values.add(e.nextElement());
        }

        return Collections.enumeration(values);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> names = new HashSet<>(customHeaders.keySet());

        Enumeration<String> e = super.getHeaderNames();
        while (e.hasMoreElements()) {
            names.add(e.nextElement());
        }

        return Collections.enumeration(names);
    }

    public void addHeader(String name, String value) {
        customHeaders.put(name, value);
    }
}
