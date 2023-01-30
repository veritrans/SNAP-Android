package com.midtrans.sdk.corekit.models;

public class ExpiryModel {
    public static final String UNIT_SECONDS = "seconds";
    public static final String UNIT_MINUTE = "minutes";
    public static final String UNIT_HOUR = "hours";
    public static final String UNIT_DAY = "days";

    private String startTime;
    private String unit;
    private int duration;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


}
