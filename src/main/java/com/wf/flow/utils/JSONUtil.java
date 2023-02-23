package com.wf.flow.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public JSONUtil() {
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static <T> T jsonToObj(String json, Class<T> clz) {
        try {
            return mapper.readValue(json, clz);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new RuntimeException(var3);
        }
    }


    public static <T> T jsonToObj(String json, String propName, Class<T> clz) {
        try {
            JsonNode node = mapper.readTree(json).path(propName);
            return mapper.readValue(node.toString(), clz);
        } catch (Exception var4) {
            var4.printStackTrace();
            throw new RuntimeException(var4);
        }
    }


    public static String objToJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception var2) {
            var2.printStackTrace();
            throw new RuntimeException(var2);
        }
    }

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
