package br.com.fiap.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationResponseBody {

    public ApplicationResponseBody(){}

    @ConstructorProperties({"message", "exception", "data"})
    public ApplicationResponseBody(String message, String exception, Object data){
        this.message = message;
        this.exception = exception;
        this.data = data;
    };

    public ApplicationResponseBody(String message){
        this.message = message;
    };

    public ApplicationResponseBody(String message, Object data){
        this.message = message;
        this.data = data;
    };

    public ApplicationResponseBody(String message, String exception){
        this.message = message;
        this.exception = exception;
    };

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Object data;

    @JsonProperty("exception")
    private String exception;

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

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
