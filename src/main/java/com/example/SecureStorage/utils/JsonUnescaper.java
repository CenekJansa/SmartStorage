package com.example.SecureStorage.utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for unescaping JSON strings.
 *
 * <p>
 * This class is used to unescape JSON strings that are stored
 * in the database with escaped characters.
 * It removes the leading and trailing quotes from the JSON string if they exist.
 */
public class JsonUnescaper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUnescaper.class);

    /**
     * Unescapes a JSON string.
     *
     * @param json the JSON string to unescape
     * @return the unescaped JSON string
     */
    public static String unescape(String json) {
        if (!StringUtils.startsWith(json, "\"")) {
            return json;
        }
        LOGGER.warn("[unescape] Must unescape json " + json);
        String unescapedJson = StringEscapeUtils.unescapeJava(json);
        if (unescapedJson.startsWith("\"")) {
            unescapedJson = StringUtils.substring(unescapedJson, 1);
        }
        if (unescapedJson.endsWith("\"")) {
            unescapedJson = StringUtils.substring(unescapedJson, 0, unescapedJson.length() - 1);
        }
        return unescapedJson;
    }
}