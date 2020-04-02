package com.tongplay.tongplay;

import com.tongplay.tongplay.pojo.UnifiedOcrConfig;
import com.tongplay.tongplay.pojo.UnifiedOcrResp;
import com.tongplay.tongplay.utils.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class TestHttpUtil {

    @Test
    public void testt(){
        HttpsNoCertUtil httpsNoCertUtil = new HttpsNoCertUtil();
        httpsNoCertUtil.post2("https://appservice.coushier.com/unifiedocr/loginPage",null);


//        Map<String, String> map = new HashMap<>();
//        map.put("tong","tong");
//        String s = httpsNoCertUtil.map2Url(map);
//        System.out.println(s);

    }

    public static void main(String[] args) throws IOException {
        String uri = "https://appservice.coushier.com/unifiedocr/loginPage";
        byte[] bytes = HttpsUtil.doPost(uri,"");
        System.out.println(new String (bytes,"utf-8"));
//        FileOutputStream fos = new FileOutputStream("D:/bing.txt");
//        fos.write(bytes);
//        fos.close();
//        System.out.println("done!");
//        byte[] bytes1 = "hello world".getBytes();        //Verify original content
//        System.out.println( new String(bytes1,"utf-8") );
    }

    @Test
    public void test8(){

//        UnifiedOcrConfig unifiedOcrConfig = new UnifiedOcrConfig();
//        HashMap<String, String> paramMap = new HashMap<>();
//        paramMap.put("v","0.0.1");
//        paramMap.put("timestamp",String.valueOf(new Date().getTime()));
//        paramMap.put("appid",unifiedOcrConfig.getAppid());
//        if(unifiedOcrConfig.getProjectCode() != null){
//            paramMap.put("project_code",unifiedOcrConfig.getProjectCode());
//        }
//        paramMap.put("cache","false");
//        paramMap.put("body", "");
//        paramMap.put("sign", SignUtil.md5Sign(JSONUtil.toMap(JSONUtil.toJson()), unifiedOcrConfig.getAppkey()));
//        HttpsNoCertUtil httpsNoCertUtil = new HttpsNoCertUtil();
//        httpsNoCertUtil.post("https://appservice.coushier.com/unifiedocr/loginPage",paramMap);
    }

    @Test
    public void test55(){
        final String uri = "https://appservice.coushier.com/unifiedocr/doOcr.do";

//        EmployeeVO newEmployee = new EmployeeVO(-1, "Adam", "Gilly", "test@email.com");

        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());

        UnifiedOcrConfig unifiedOcrConfig = new UnifiedOcrConfig();
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("v","0.0.1");
        paramMap.put("timestamp",String.valueOf(new Date().getTime()));
        paramMap.put("appid",unifiedOcrConfig.getAppid());
        if(unifiedOcrConfig.getProjectCode() != null){
            paramMap.put("project_code",unifiedOcrConfig.getProjectCode());
        }
        paramMap.put("cache","false");
        paramMap.put("body", "");
        paramMap.put("sign", SignUtil.md5Sign(JSONUtil.toMap(JSONUtil.toJson(paramMap)), unifiedOcrConfig.getAppkey()));

        HttpsNoCertUtil httpsNoCertUtil = new HttpsNoCertUtil();
        restTemplate.postForObject(uri,paramMap,HashMap.class);

//        System.out.println(result);
    }


}
