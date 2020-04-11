package Model;

import Model.Enums.RepetitionRate;

import java.time.LocalTime;

public class TripTiming {
    private int day;
    private LocalTime time;
    private RepetitionRate repetitionRate;

    public TripTiming(int day, LocalTime time, RepetitionRate repetitionRate) {
        this.day = day;
        this.time = time;
        this.repetitionRate = repetitionRate;
    }
}
