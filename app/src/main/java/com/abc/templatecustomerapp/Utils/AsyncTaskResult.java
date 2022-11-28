package com.abc.templatecustomerapp.Utils;

public class AsyncTaskResult<T> {
    private T result;
    private Exception error;

    public AsyncTaskResult(T result) {
        this.result = result;
    }
    public AsyncTaskResult(Exception e) {
        this.error = e;
    }

    public Exception getError() {
        return error;
    }

    public T getResult() {
        return result;
    }

    public boolean isError() {
        return this.error != null;
    }
}

