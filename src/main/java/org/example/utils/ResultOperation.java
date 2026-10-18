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
