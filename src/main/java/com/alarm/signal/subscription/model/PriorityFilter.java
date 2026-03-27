package com.alarm.signal.subscription.model;

public enum PriorityFilter {
    ALL,        // receive all signals
    HIGH_ONLY,  // only HIGH (alarm signals)
    MEDIUM,     // medium + high
    HIGH_AND_MEDIUM
}

