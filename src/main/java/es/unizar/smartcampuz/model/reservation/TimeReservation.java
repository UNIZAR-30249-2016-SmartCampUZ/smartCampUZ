package es.unizar.smartcampuz.model.reservation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Embeddable
public class TimeReservation {

    @Transient
    private static final int NUM_TIME_SLOTS = 24;

    @NotNull
    private int timeSlots;

    @NotNull
    private Date date;

    //Used by Hibernate. Private is more readable. Protected will improve performance.
    protected TimeReservation(){

    }

    public TimeReservation(@NotNull boolean[] timeSlots, @NotNull Date date){
        this.timeSlots=arrayToInteger(timeSlots);
        this.date=date;
    }

    //Used by Hibernate. Private is more readable. Protected will improve performance.
    protected void setTimeSlots(boolean[] timeSlots) {
        this.timeSlots = arrayToInteger(timeSlots);
    }

    public boolean[] getTimeSlots() {
        return integerToArray(timeSlots);
    }

    //Used by Hibernate. Private is more readable. Protected will improve performance.
    protected void setDate(Date value) {
        this.date = value;
    }

    public Date getDate() {
        return date;
    }

    private boolean[] integerToArray( int timeSlots ){
        if( timeSlots >= (1<<24) ){
            throw new IllegalArgumentException("Input should be higher than 1<<24");
        }

        boolean aux;
        boolean[] result = new boolean[NUM_TIME_SLOTS];
        for (int i = 0; i < NUM_TIME_SLOTS; i++) {

            aux = ((timeSlots & 1) > 0) ? true : false;
            result[i] = aux;
            timeSlots >>= 1;
        }
        return result;
    }

    private int arrayToInteger( boolean[] timeSlots){
        if (timeSlots.length != NUM_TIME_SLOTS){
            throw new IllegalArgumentException("Input array should have 24 elements");
        }

        int aux;
        int result = 0;
        for (int i = 0; i < NUM_TIME_SLOTS; i++) {
            aux = timeSlots[i] ? 1 : 0;
            result |= aux << i;
        }
        return result;
    }

    public boolean equals(TimeReservation time2){
        return this.date.equals(time2.date) && this.timeSlots==time2.timeSlots;
    }
}
