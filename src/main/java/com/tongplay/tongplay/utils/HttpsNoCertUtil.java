package com.tongplay.tongplay.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpsNoCertUtil {
    private static Log log = LogFactory.getLog(HttpsNoCertUtil.class);

    private HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
        public boolean verify(String s, SSLSession sslsession) {
            log.info("WARNING: Hostname is not matched for cert.");
            return true;
        }
    };

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

    public String post(String urlString,Map<String, String> paramToMap) {
        String p = map2Url(paramToMap);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream(512);
        try {
            URL url = new URL(urlString);
            /*
             * use ignore host name verifier
             */
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.addRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream out = connection.getOutputStream();
            out.write(p.getBytes("utf-8"));
            out.flush();
            out.close();

            InputStream reader = connection.getInputStream();
            byte[] bytes = new byte[512];
            int length = reader.read(bytes);

            do {
                buffer.write(bytes, 0, length);
                length = reader.read(bytes);
            } while (length > 0);

            log.info(buffer.toString());
            reader.close();

            connection.disconnect();

        } catch (Exception ex) {
            log.error("", ex);
        } finally {

        }
        String repString= new String (buffer.toByteArray());
        return repString;
    }

    private String map2Url(Map<String, String> paramToMap) {
        if (null == paramToMap || paramToMap.isEmpty()) {
            return null;
        }
        StringBuffer url = new StringBuffer();
        boolean isfist = true;
        for (Entry<String, String> entry : paramToMap.entrySet()) {
            if (isfist) {
                isfist = false;
            } else {
                url.append("&");
            }
            url.append(entry.getKey()).append("=");
            String value = entry.getValue();
            if (value!=null) {
                try {
                    url.append(URLEncoder.encode(value, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return url.toString();
    }
}