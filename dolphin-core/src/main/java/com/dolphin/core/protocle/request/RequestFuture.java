package com.dolphin.core.protocle.request;

import com.dolphin.core.protocle.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RequestFuture {

    private Logger logger = LoggerFactory.getLogger(RequestFuture.class);

    /**
     * 是否请求成功  @author keyhunter 2016年5月11日 下午9:14:24
     */
    private AtomicBoolean isSuccess = new AtomicBoolean();

    private AtomicBoolean isDone = new AtomicBoolean();

    private Callback callback;

    private Semaphore semaphore = new Semaphore(0);

    private Response response;

    public RequestFuture() {
    }

    public RequestFuture(Callback callback) {
        this.callback = callback;
    }

    public void callback(Response response) {
        callback.handle(response);
    }

    public boolean isSuccess() {
        return isSuccess.get();
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess.set(isSuccess);
    }

    public Callback getCallback() {
        return callback;
    }

    public Response getResponse(long timeout, TimeUnit unit) {
        try {
            semaphore.tryAcquire(timeout, unit);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
        setIsDone(true);
        setIsSuccess(true);
        semaphore.release(1);
    }

    public boolean getIsDone() {
        return isDone.get();
    }

    public void setIsDone(boolean isDone) {
        this.isDone.set(isDone);
    }

}
