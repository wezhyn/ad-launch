package com.wezhyn.project.exception;

/**
 * @author ZLB_KAM
 * @date 2019/9/27
 */
public class UpdateOperationException extends RuntimeException {

    public UpdateOperationException() {
    }

    public UpdateOperationException(String message) {
        super(message);
    }

    public UpdateOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
