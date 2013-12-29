package com.tinyms.data;

/**
 * Created by tinyms on 13-12-29.
 */
public class PaginationResult {
    private long total;
    private Object result;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
