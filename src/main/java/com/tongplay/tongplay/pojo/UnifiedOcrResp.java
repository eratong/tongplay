package com.tongplay.tongplay.pojo;

import lombok.Data;

@Data
public class UnifiedOcrResp {

    private String requestId;
    private String v;
    private String result;
    private String message;
    private String sign;
    private String timestamp;
    private String signType;
    private String body;
}
