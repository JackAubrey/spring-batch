package com.example.spring.batch.app.util;

import org.apache.commons.lang3.StringUtils;

public class NameAnonymizer {
    public static String anonymize(String name){

        if(StringUtils.isNotBlank(name)) {
            String [] parts = name.split("\\s");
            StringBuilder sb = new StringBuilder();
            for(String part:parts) {
                sb.append(part.trim().toUpperCase().charAt(0)).append(".");
            }
            return sb.toString();
        } else {
            return name;
        }
    }
    private NameAnonymizer() {
        super();
    }
}
