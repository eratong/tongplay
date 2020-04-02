package com.tongplay.tongplay.utils;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

@Slf4j
public class HttpsRequestUtil {

    private TrustManager ignoreCertificationTrustManger = new X509TrustManager() {

        private X509Certificate[] certificates;

        @Override
        public void checkClientTrusted(X509Certificate certificates[],
                                       String authType) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = certificates;
                log.info("init at checkClientTrusted");
            }
        }

        @Override
        public void checkServerTrusted(X509Certificate[] ax509certificate,
                                       String s) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = ax509certificate;
                log.info("init at checkServerTrusted");
            }

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    /**
     * 发送Post请求到
     */
    public void doPost(String urlStr,Map<String, String> param){
        //请求是http

        //请求是https
        if(urlStr.contains("https")){

            try {
                //准备 SSLContext
                SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
                sslContext.init(null, new TrustManager[]{ignoreCertificationTrustManger}, new SecureRandom());

                //获取工厂
                SSLSocketFactory ssf = sslContext.getSocketFactory();

                //获取连接
                URL url = new URL(urlStr);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

                //忽略hostname校验
                httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });

                httpsURLConnection.setConnectTimeout(10000);
                httpsURLConnection.setReadTimeout(10000);
                httpsURLConnection.addRequestProperty("Content-Type","application/x-www-form-urlencoded");
                httpsURLConnection.setRequestMethod("POST");

                httpsURLConnection.setSSLSocketFactory(ssf);
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);

                OutputStream out = httpsURLConnection.getOutputStream();
//            out.write(p.getBytes("utf-8"));
                out.flush();
                out.close();

                InputStream reader = httpsURLConnection.getInputStream();
                byte[] bytes = new byte[512];
                int length = reader.read(bytes);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream(512);

                do {
                    buffer.write(bytes, 0, length);
                    length = reader.read(bytes);
                } while (length > 0);

                log.info(buffer.toString());
                reader.close();

                httpsURLConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new HttpsRequestUtil().doPost("https://appservice.coushier.com/unifiedocr/doOcr.do",null);
    }

}
