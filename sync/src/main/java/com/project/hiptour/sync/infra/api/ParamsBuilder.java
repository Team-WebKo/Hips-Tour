package com.project.hiptour.sync.infra.api;

import java.util.Map;
import java.util.StringJoiner;

public class ParamsBuilder {
    public static String toUrl(String host, Map<String, String> params) {
        StringJoiner joiner = new StringJoiner("&", host + "?", "");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            joiner.add(entry.getKey() + "=" + entry.getValue());
        }

        return joiner.toString();
    }
}
