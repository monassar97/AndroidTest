package com.zak.testapp;

public class MessageResult {
    private String result;
    private boolean hasResult;

    public MessageResult(String result, boolean hasResult) {
        this.result = result;
        this.hasResult = hasResult;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isHasResult() {
        return hasResult;
    }

    public void setHasResult(boolean hasResult) {
        this.hasResult = hasResult;
    }

    @Override
    public String toString() {
        return "MessageResult{" +
                "result='" + result + '\'' +
                ", hasResult=" + hasResult +
                '}';
    }
}
