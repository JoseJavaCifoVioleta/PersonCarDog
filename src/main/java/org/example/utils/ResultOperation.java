package org.example.utils;

/**
 * Standardized return object for every Service operation (v4).
 *
 * Fields:
 * - success: boolean        -> did the operation succeed?
 * - message: String         -> human readable explanation
 * - type: String            -> entity involved ("Person", "Car", "Dog", "CarTransaction")
 * - timestamp: long         -> epoch millis when the operation finished
 * - updatedObject: Object   -> optional payload (entity / list / transaction), null on failure or delete
 */
public class ResultOperation {

    private String type;
    private long timestamp;
    private boolean success;
    private String message;
    private Object updatedObject;

    public ResultOperation() {
    }

    /**
     * Convenience constructor for operations that only report a status
     * ("SUCCESS" / "ERROR") plus a human readable message.
     * Success is derived from the status; no payload and no entity type.
     */
    public ResultOperation(String status, String message) {
        this.success = "SUCCESS".equalsIgnoreCase(status);
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Fluent setter without payload (failures and successful deletes).
     */
    public ResultOperation setter(boolean success, String message, String type, long timestamp) {
        return setter(success, message, type, timestamp, null);
    }

    /**
     * Fluent setter with payload (created / updated / retrieved entity, list or transaction).
     */
    public ResultOperation setter(boolean success, String message, String type, long timestamp, Object updatedObject) {
        this.success = success;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
        this.updatedObject = updatedObject;
        return this;
    }

    // ========== GETTERS ==========

    /**
     * Primary status line for the console: "SUCCESS" or "ERROR".
     */
    public String getResult() {
        return success ? "SUCCESS" : "ERROR";
    }

    /**
     * Human readable explanation of the outcome (null when not set).
     */
    public String getReason() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Object getUpdatedObject() {
        return updatedObject;
    }

    // ========== TOSTRING ==========

    @Override
    public String toString() {
        return "ResultOperation {"
                + " success=" + success
                + ", type=" + type
                + ", timestamp=" + timestamp
                + ", message=\"" + message + "\""
                + ", updatedObject=" + updatedObject
                + " }";
    }
}
