package net.kimleo.grabbie.model;

public enum ExecutionStatus {
    EXECUTED_SUCCESS, EXECUTED_FAILURE,
    NOT_EXECUTED,
    STARTED,
    IGNORED_BY_CLIENT,
    RETRIED_SUCCESS, RETRIED_FAILED
}
