package com.qtfreet.musicuu.model;

import com.alibaba.fastjson.JSON;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.RestRequest;
import com.yolanda.nohttp.rest.StringRequest;

import java.util.List;

/**
 * Created by qtfreet on 2016/12/21.
 */

public class JavaBeanRequest<T> extends RestRequest<List<T>> {

    // 要解析的JavaBean的class。
    private Class<T> clazz;

    public JavaBeanRequest(String url, Class<T> clazz) {
        this(url, RequestMethod.GET, clazz);
    }

    public JavaBeanRequest(String url, RequestMethod requestMethod, Class<T> clazz) {
        super(url, requestMethod);
        this.clazz = clazz;
    }

    @Override
    public List<T> parseResponse(Headers responseHeaders, byte[] responseBody) throws Throwable {
        String response = StringRequest.parseResponseString(responseHeaders, responseBody);

        // 这里如果数据格式错误，或者解析失败，会在失败的回调方法中返回 ParseError 异常。
        return JSON.parseArray(response, clazz);
    }
}