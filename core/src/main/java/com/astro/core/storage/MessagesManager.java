package com.astro.core.storage;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Properties;

/**
 *
 */
class MessagesManager {

    @Setter
    @Getter
    private String selectedLanguages = "pl";

    private HashMap<String, Properties> messages = new HashMap<>();

    public void addLanguages(final String lng, final Properties msg) {
        messages.put(lng, msg);
    }

    public String getMsg(final String key) {
        return (String) messages.get(selectedLanguages).get(key);
    }

}