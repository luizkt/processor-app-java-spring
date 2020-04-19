package br.com.fiap.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseBody {

    public ResponseBody(){};

    public ResponseBody(String message, Object data){
        this.message = message;
        this.data = data;
    };

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Object data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toJsonString() {
        return "{" +
                "\"message\":\"" + message + "\"," +
                "\"data\":" + data +
                "}";
    }
}
