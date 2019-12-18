/*
 * Copyright (C) 2015 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.example.utils;

import cn.edu.hfut.dmic.webcollector.plugin.net.OkHttpRequester;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * WebCollector使用豌豆代理的Http请求插件
 * <p>
 * 插件使用方法：
 * crawler.setRequester(new WanDouDynamicProxyRequester("豌豆用户名", "豌豆密码"));
 * 一行代码解决，无需自定义代理池及其它代理切换规则
 * <p>
 * 豌豆HTTP代理官网：
 * https://h.wandouip.com/
 *
 * @author wxe
 */
public class WanDouDynamicProxyRequester extends OkHttpRequester {

    String credential;
    String proxyHost;
    int proxyPort;

    public WanDouDynamicProxyRequester(String proxyUser, String proxyPass) {
        credential = Credentials.basic(proxyUser, proxyPass);
        removeSuccessCode(301);
        removeSuccessCode(302);
    }

    @Override
    public OkHttpClient.Builder createOkHttpClientBuilder(){
        this.HttpGetProxyHost();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        return super.createOkHttpClientBuilder()
                .proxy(proxy)
                .proxyAuthenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    }
                });
    }

    public Map<String,String> HttpGetProxyHost(){
        Map<String,String> map = new HashMap<String, String>();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httpget.
        HttpGet httpget = new HttpGet("http://api.wandoudl.com/api/ip?app_key=371ff5c162e8cceea4d56869d0b7681e&pack=0&mr=1&");
        // 执行get请求.
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            // 打印响应内容
            System.out.println("Response content: " + EntityUtils.toString(entity));
            JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(entity));
            if (jsonObject.get("code").equals(200)){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                proxyHost = jsonArray.getJSONObject(0).getString("ip");
                proxyPort = jsonArray.getJSONObject(0).getIntValue("port");
            }else {
                System.out.println("errorMsg:"+ jsonObject.get("msg").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("proxyHost:\n"+proxyHost);
            System.out.println("proxyPort:\n"+proxyPort);
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static void main(String[] args) throws Exception {
        final String username = "904908799@qq.com";
        final String password = "Sky81579261";
        WanDouDynamicProxyRequester requester = new WanDouDynamicProxyRequester(username, password);
//        System.out.println(requester.getResponse("https://github.com/").html());
        System.out.println(requester.getResponse("https://Movie.douban.com/").html());
    }
}
