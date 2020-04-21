package model;

import model.Enums.RepetitionRate;

import java.time.LocalTime;

public class TripTiming {
    private int day;
    private LocalTime time;
    private RepetitionRate repetitionRate;

    public TripTiming(int day, int hour, int minutes, String repetitionRate) {
        this.day = day;
        this.time = LocalTime.of(hour, minutes);
        this.repetitionRate = RepetitionRate.valueOf(repetitionRate);
    }
}
