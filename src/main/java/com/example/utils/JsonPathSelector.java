package com.example.utils;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonPathSelector extends us.codecraft.webmagic.selector.JsonPathSelector {
    private String jsonPathStr;
    private JsonPath jsonPath;

    public JsonPathSelector(String jsonPathStr) {
        super(jsonPathStr);
        this.jsonPathStr = jsonPathStr;
        this.jsonPath = JsonPath.compile(this.jsonPathStr, new Predicate[0]);
    }

    public List<JSONObject> selectJsonList(String text) {
        List<JSONObject> list = new ArrayList();
        Object object = this.jsonPath.read(text);
        if (object == null) {
            return list;
        } else {
            if (object instanceof List) {
                List<Object> items = (List) object;
                Iterator var5 = items.iterator();

                while (var5.hasNext()) {
                    Object item = var5.next();
                    list.add(JSONObject.fromObject(item));
                }
            } else {
                list.add(JSONObject.fromObject(object));
            }

            return list;
        }
    }

}
