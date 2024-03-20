package com.playtomic.tests.wallet.api.error;

public class ErrorResponse {
    private final int status;
    private final String message;

    public ErrorResponse(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

