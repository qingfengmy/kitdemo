package com.qingfengmy.ui.network.entities;

/**
 * Created by Administrator on 2015/1/21.
 */
public class JokeList {
    private int error_code = 0;
    private String reason;
    private JokeResult result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public JokeResult getResult() {
        return result;
    }

    public void setResult(JokeResult result) {
        this.result = result;
    }
}
