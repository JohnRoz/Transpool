package model;

import model.Enums.RepetitionRate;

import java.time.LocalTime;

public class TripTiming {

    private static final int ROUND_MINUTES_TO_PRODUCT_OF = 5;

    private int day;
    private LocalTime time;
    private RepetitionRate repetitionRate;

    public TripTiming(int day, int hour, int minutes, String repetitionRate) {
        this.day = day;
        this.time = roundTime(hour, minutes);
        this.repetitionRate = RepetitionRate.valueOf(repetitionRate);
    }

    //region Getters & Setters
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public RepetitionRate getRepetitionRate() {
        return repetitionRate;
    }

    public void setRepetitionRate(RepetitionRate repetitionRate) {
        this.repetitionRate = repetitionRate;
    }
    //endregion

    //region Private Methods
    private static LocalTime roundTime(int hour, int minutes) {
        LocalTime tripTime = LocalTime.of(hour, minutes);
        int roundedMinutes =
                ROUND_MINUTES_TO_PRODUCT_OF *
                Math.round((float)minutes / ROUND_MINUTES_TO_PRODUCT_OF);

         if (roundedMinutes >= 60){
             tripTime = tripTime.plusHours(1);
             tripTime = tripTime.minusMinutes(tripTime.getMinute());
         }
         else
             tripTime = LocalTime.of(hour, roundedMinutes);

         return tripTime;
    }
    //endregion

    //region Public Methods
    public static LocalTime roundTime(LocalTime time) {
        return roundTime(time.getHour(), time.getMinute());
    }
    //endregion
}
