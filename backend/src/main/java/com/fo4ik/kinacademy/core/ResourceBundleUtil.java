package com.fo4ik.kinacademy.core;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ResourceBundleUtil {
    public static Map<String, String> convertResourceBundleToMap(ResourceBundle resourceBundle) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> keys = resourceBundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = resourceBundle.getString(key);
            map.put(key, value);
        }
        return map;
    }
}
