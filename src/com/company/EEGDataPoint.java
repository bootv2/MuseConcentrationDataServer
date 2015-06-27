package com.company;

/**
 * Created by TTT on 6/27/2015.
 */
public class EEGDataPoint
{
    public EEGDataPoint(String json)
    {

    }

    public EEGDataPoint()
    {

    }

    public double getConcentrationValue() {
        return concentrationValue;
    }

    public void setConcentrationValue(double concentrationValue) {
        this.concentrationValue = concentrationValue;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private long time = -1;
    private double concentrationValue = -1.d;
}
