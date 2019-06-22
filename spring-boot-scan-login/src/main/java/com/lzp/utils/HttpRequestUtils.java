package com.lzp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzp on 2018/5/10.
 */
public class HttpRequestUtils {

    private static  Logger log = LoggerFactory.getLogger(HttpRequestUtils.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * http请求工具类，post请求
     *
     * @param url    url
     * @param params 参数值 仅支持String和list两种类型
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, Map<String, Object> params) throws Exception {
        DefaultHttpClient defaultHttpClient = null;
        BufferedReader bufferedReader = null;
        try {
            defaultHttpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json;charset=ut-8");
            if (params != null) {
                //转换为json格式并打印，不需要的你们可以不要
                String jsonParams = objectMapper.writeValueAsString(params);
                log.info("参数值：{}", jsonParams);
                HttpEntity httpEntity = new StringEntity(jsonParams, "utf-8");
                httpPost.setEntity(httpEntity);
            }
            HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                String errorLog="请求失败，errorCode:"+httpResponse.getStatusLine().getStatusCode();
                log.info(errorLog);
                throw new Exception(url+errorLog);
            }
            //读取返回信息
            String output;
            bufferedReader=new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),"utf-8"));
            StringBuilder stringBuilder=new StringBuilder();
            while ((output=bufferedReader.readLine())!=null){
                stringBuilder.append(output);
            }
            return stringBuilder.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw e;
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }finally {
            if(defaultHttpClient!=null)
                defaultHttpClient.getConnectionManager().shutdown();
            if(bufferedReader!=null)
                bufferedReader.close();
        }
    }

    /**
     * http请求工具类，get请求
     * @param url
     * @param params
     * @param resonseCharSet
     * @return
     * @throws Exception
     */
    public static String httpGet(String url, Map<String, Object> params,String ...resonseCharSet) throws Exception {
        DefaultHttpClient defaultHttpClient = null;
        BufferedReader bufferedReader = null;
        try {
            defaultHttpClient = new DefaultHttpClient();
            if(params!=null){
                StringBuilder stringBuilder=new StringBuilder();
                Iterator<String> iterator=params.keySet().iterator();
                String key;
                while (iterator.hasNext()){
                    key=iterator.next();
                    Object val=params.get(key);
                    if(val instanceof List){
                        List v= (List) val;
                        for (Object o:v){
                            stringBuilder.append(key).append("=").append(o.toString()).append("&");
                        }
                    }else{
                        stringBuilder.append(key).append("=").append(val.toString()).append("&");
                    }
                }
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                url=url+"?"+stringBuilder.toString();
                log.info("url:{}",url);
            }
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type", "application/json;charset=ut-8");
            HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                String errorLog="请求失败，errorCode:"+httpResponse.getStatusLine().getStatusCode();
                log.info(errorLog);
                throw new Exception(url+errorLog);
            }
            //读取返回信息
            String charSet="utf-8";
            if(resonseCharSet!=null && resonseCharSet.length>0)
                charSet=resonseCharSet[0];
            String output;
            bufferedReader=new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),charSet));

            StringBuilder dataBuilder=new StringBuilder();
            while ((output=bufferedReader.readLine())!=null){
                dataBuilder.append(output);
            }
            return dataBuilder.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw e;
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }finally {
            if(defaultHttpClient!=null)
                defaultHttpClient.getConnectionManager().shutdown();
            if(bufferedReader!=null)
                bufferedReader.close();
        }
    }

    //测试方法
    public static void main(String[] args) {
        String url="这里填写你的地址";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("index","aaa");//这里是参数值
        try {
            String output=httpPost(url,map);
            log.info(output);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
