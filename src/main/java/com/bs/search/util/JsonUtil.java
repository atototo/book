package com.bs.search.util;


import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
public class JsonUtil {

    public static Object jsonStrToObject(String jsonStr, Object object) {
        try {
            ObjectMapper om = new ObjectMapper();
            om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);	// 해당하는 변수가 없어도 오류가 발생하지 않도록 설정
            om.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);			// null 일경우 제외해주는 property
            om.enable(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);	// list로 field 정의를 했다면 단건이든 list든 array로 정보 세팅 될 수 있도록 하는 property
            return om.readValue(jsonStr.toString(), object.getClass());
        } catch (Exception e) {
            log.error("JSON TO Object 생성 오류", e);
            log.error("하단 jsonStr 확인 필요");
            for (String msg : StringUtil.subStringByIdx(jsonStr, 1000)) {
                log.error("{}", msg);
            }
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    public static JSONObject mapToJson(Map<String, Object> map) {
        JSONObject json = new JSONObject();

        for(Map.Entry<String, Object> entry: map.entrySet()) {
            String key =entry.getKey();
            Object value = entry.getValue();
            json.put(key, value);
        }

        return json;
    }

    @SuppressWarnings("unchecked")
    public static JSONObject objectToJsonObj(Object obj) {
        JSONObject jsonObj = new JSONObject();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                jsonObj.put(field.getName(), field.get(obj));
            } catch (Exception e) {
                log.error("Object TO JSON 생성 오류", e);
            }
        }
        return jsonObj;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static JSONObject objectWithSuperToJsonObj(Object obj) {
        JSONObject jsonObj = new JSONObject();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                jsonObj.put(field.getName(), field.get(obj));
            } catch (Exception e) {
                log.error("Object TO JSON 생성 오류", e);
            }
        }

        Class superClass = obj.getClass().getSuperclass();
        fields = superClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                jsonObj.put(field.getName(), field.get(obj));
            } catch (Exception e) {
                log.error("super Object TO JSON 생성 오류", e);
            }
        }

        return jsonObj;
    }
}
