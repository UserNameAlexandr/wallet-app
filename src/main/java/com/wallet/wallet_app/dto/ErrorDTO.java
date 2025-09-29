package com.wallet.wallet_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {

    private String message;
    private Integer code;
    private String detail;

    public ErrorDTO() {}

    public ErrorDTO(String message) {
        this.message = message;
    }

    public ErrorDTO(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public ErrorDTO(String message, Integer code, String detail) {
        this.message = message;
        this.code = code;
        this.detail = detail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
