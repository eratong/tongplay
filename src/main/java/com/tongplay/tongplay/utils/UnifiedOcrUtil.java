//package com.tongplay.tongplay.utils;
//
//import com.tongplay.tongplay.pojo.UnifiedOcrResp;
//import lombok.extern.slf4j.Slf4j;
//import org.omg.CORBA.NameValuePair;
//import org.springframework.http.HttpHeaders;
//import org.springframework.util.CollectionUtils;
//
//import java.io.IOException;
//import java.util.*;
//
//@Slf4j
//public class UnifiedOcrUtil {
//
//    private static final Random random = new Random();
//
//    public static UnifiedOcrResp doOCR(String httpUrl, HashMap<String,String> param){
//
//        String s = sendPost(httpUrl, param, "Apache-HttpClient/4.5 (Java/1.8.0_131)", 100000, 100000);
//        UnifiedOcrResp resp = JSONUtil.toBean(s, UnifiedOcrResp.class);
//        return resp;
//    }
//
//
//
//    public static String sendPost(String url, Map<String,String> param, String userAgent, int connectTimeout, int readTimeout){
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        String responseString = null;
//        try {
//            HttpPost httpPost = new HttpPost(url);
//            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connectTimeout).build();//设置请求和传输超时时间
//            httpPost.setConfig(requestConfig);
//            httpPost.addHeader(HttpHeaders.USER_AGENT, userAgent);
//            httpPost.addHeader("Connection", "close");
//            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//
//            Iterator<Map.Entry<String, String>> iter = param.entrySet().iterator();
//            while (iter.hasNext()) {
//                Map.Entry<String,String> entry = (Map.Entry<String,String>) iter.next();
//                Object key = entry.getKey();
//                Object val = entry.getValue();
//                nvps.add( new BasicNameValuePair(key.toString(), val.toString()) );
//            }
//
//            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
//            log.info("Executing request " + httpPost.getRequestLine());
//            CloseableHttpResponse response = httpclient.execute(httpPost);
//            try {
//                log.info(""+response.getStatusLine());
//                HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    responseString = EntityUtils.toString(entity, "UTF-8");
//                }
//            } finally {
//                response.close();
//            }
//        } catch (ClientProtocolException e) {
//            log.error("",e);
//        } catch (IOException e) {
//            log.error("",e);
//        } finally {
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                log.error("关闭httpclient报错",e);
//            }
//        }
//        log.info("response:"+responseString);
//        return responseString;
//    }
//
//    public static UnifiedOcrConfig getUnifiedOcrConfig(){
//        List<UnifiedOcrConfig> unifiedConfig = BaseConfigTask.UNIFIEDCONFIG.get("unifiedConfig");
//        if(CollectionUtils.isEmpty(unifiedConfig)){
//            // 默认账号 return new TencentConfig("2116873579","bdoazpIBloyyfwqK");
////            return TencentConfig.defTencentConfig();
//            return null;
//        }
//        if(unifiedConfig.size() == 1){
//            return unifiedConfig.get(0);
//        }else{
//            return unifiedConfig.get(random.nextInt(unifiedConfig.size()));
//        }
//    }
//}
