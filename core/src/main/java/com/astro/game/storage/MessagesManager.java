package com.astro.game.storage;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Properties;

/**
 * Manager of the all internationals text.
 */
public class MessagesManager {

    @Setter
    @Getter
    private String selectedLanguages = "pl";

    private final HashMap<String, Properties> messages = new HashMap<>();

    public void addLanguages(final String lng, final Properties msg) {
        messages.put(lng, msg);
    }

    public String getMsg(final String key) {
        return (String) messages.get(selectedLanguages).get(key);
    }

}
