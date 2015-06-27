package com.company;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Chronotype {

    public void setDataPointList(List<EEGDataPoint> dataPointList) {
        this.dataPointList = dataPointList;
    }

    public List<EEGDataPoint> dataPointList;
    public Calendar cal = Calendar.getInstance();

    public Chronotype() {
        dataPointList = new ArrayList<EEGDataPoint>();
    }

    public void addDataPoint(EEGDataPoint point) {
        dataPointList.add(point);
    }

    public void sortPointsForDate() {
        Collections.sort(dataPointList, EEGDataPoint.compareDate);
    }

    public void sortPointsForConcentration() {
        Collections.sort(dataPointList, EEGDataPoint.compareConcentration);
    }

    public void sortPointsForTimeOfDay() {
        Collections.sort(dataPointList, EEGDataPoint.compareTimeOfDay);
    }

    public int getMinuteOfDay(long time) {
        cal.setTimeInMillis(time);
        int minute = cal.get(Calendar.MINUTE)+cal.get(Calendar.HOUR_OF_DAY)*60;
        return minute;
    }

    /**
     *
     * @return chronotype (0-6)
     */
    public int determineChronotype() {
        sortPointsForTimeOfDay();
        //resolution in minutes

        int subDivs = 7;
        int minutesInDay = (24*60);

        int[] totalConcentration = new int[subDivs];
        int[] totalPoints = new int[subDivs];

        for(EEGDataPoint dataPoint : dataPointList) {
            int minute = getMinuteOfDay(dataPoint.getTime());
            int idx = (minute)*7/minutesInDay;
            totalConcentration[idx]+=dataPoint.getConcentrationValue();
            totalPoints[idx]++;
        }

        int[] avgConcentration = new int[subDivs];
        int max = 0;
        int maxIdx = 0;
        for(int i = 0; i < subDivs; i++) {
            if(totalPoints[i]==0) continue;
            int avg = totalConcentration[i]/totalPoints[i];
            avgConcentration[i] = avg;
            if(avg > max) {
                maxIdx = i;
                max = avg;
            }
        }

        return maxIdx;
    }

    public List<EEGDataPoint> getPointsBetween(int hour1, int hour2) {
        List<EEGDataPoint> nList = new ArrayList<EEGDataPoint>();
        for(EEGDataPoint point : dataPointList) {
            cal.setTimeInMillis(point.getTime());
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            if(hour >= hour1 && hour <= hour2) {
                nList.add(point);
            }
        }
        return nList;
    }

    public void shortChronotypeTest() {
        List<EEGDataPoint> relevant = getPointsBetween(19, 23);
        Collections.sort(relevant, EEGDataPoint.compareTimeOfDay);
        //melatonine concentratie is logistische groei.

        for(int i = 0; i < relevant.size(); i++) {
            EEGDataPoint point = relevant.get(i);
            if(point.getConcentrationValue() < 50) {
                getMinuteOfDay(point.getTime());
                cal.setTimeInMillis(point.getTime());
            }
        }
        System.out.println("Melatonine productie begint om " +cal.get(Calendar.HOUR_OF_DAY) + ":"+cal.get(Calendar.MINUTE));
    }
}

