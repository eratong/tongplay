package com.tongplay.tongplay;

import com.alibaba.fastjson.JSON;
import com.counect.cube.daservice.ocr.unified.UnifiedOcrConfig;
import com.counect.cube.daservice.ocr.unified.UnifiedOcrResp;
import com.counect.cube.daservice.task.BaseConfigTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class UnifiedOcrUtil {

    private static final Random random = new Random();

    public static UnifiedOcrResp doOCR(String httpUrl, HashMap<String,String> param){

        String s = sendPost(httpUrl, param, "Apache-HttpClient/4.5 (Java/1.8.0_131)", 100000, 100000);
        UnifiedOcrResp resp = JSONUtil.toBean(s, UnifiedOcrResp.class);
        return resp;
    }



    public static String sendPost(String url, Map<String,String> param, String userAgent, int connectTimeout, int readTimeout){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String responseString = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connectTimeout).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            httpPost.addHeader(HttpHeaders.USER_AGENT, userAgent);
            httpPost.addHeader("Connection", "close");
            List <NameValuePair> nvps = new ArrayList<NameValuePair>();

            Iterator<Map.Entry<String, String>> iter = param.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String,String> entry = (Map.Entry<String,String>) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                nvps.add( new BasicNameValuePair(key.toString(), val.toString()) );
            }

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            log.info("Executing request " + httpPost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                log.info(""+response.getStatusLine());
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    responseString = EntityUtils.toString(entity, "UTF-8");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            log.error("",e);
        } catch (IOException e) {
            log.error("",e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                log.error("关闭httpclient报错",e);
            }
        }
        log.info("response:"+responseString);
        return responseString;
    }

    public static UnifiedOcrConfig getUnifiedOcrConfig(){
        List<UnifiedOcrConfig> unifiedConfig = BaseConfigTask.UNIFIEDCONFIG.get("unifiedConfig");
        if(CollectionUtils.isEmpty(unifiedConfig)){
            // 默认账号 return new TencentConfig("2116873579","bdoazpIBloyyfwqK");
//            return TencentConfig.defTencentConfig();
            return null;
        }
        if(unifiedConfig.size() == 1){
            return unifiedConfig.get(0);
        }else{
            return unifiedConfig.get(random.nextInt(unifiedConfig.size()));
        }
    }

    public static <T> String sendHttpsPost(String url,T param){

//        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        RestTemplate restTemplate = new RestTemplate();

        //设置编码
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.setMessageConverters(messageConverters);
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=utf8");
        //封装请求参数
        org.springframework.http.HttpEntity httpEntity = new org.springframework.http.HttpEntity(JSON.toJSONString(param),httpHeaders);
        System.out.println(JSON.toJSONString(httpEntity));
        //发送post请求
        ResponseEntity<String> exchange = restTemplate.exchange(URI.create(url), HttpMethod.POST, httpEntity, String.class);
        return exchange.getBody();
    }
	
	
	 public static <T> String sendHttpsPost2(String url,T param){

//        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        RestTemplate restTemplate = new RestTemplate();

        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
//        httpHeaders.add("Content-Type", "application/json;charset=utf8");
//        httpHeaders.add("Content-Type",  "application/x-www-form-urlencoded; charset=UTF-8");
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //封装请求参数
        //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
//  也支持中文
        params.add("username", "用户名");
        params.add("password", "123456");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
//  执行HTTP请求

        org.springframework.http.HttpEntity<T> httpEntity = new org.springframework.http.HttpEntity(param,httpHeaders);
        System.out.println(JSON.toJSONString(httpEntity));
        //发送post请求
        ResponseEntity<String> exchange = restTemplate.exchange(URI.create(url), HttpMethod.POST, httpEntity, String.class);
//        ResponseEntity<String> exchange = restTemplate.postForEntity("http://localhost:8086/unifiedocr/uploadMoulds",  param, String.class);
        return exchange.getBody();
    }


}
