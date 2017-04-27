package es.unizar.smartcampuz.model.reservation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Embeddable
public class TimeReservation {

    @Transient
    private static final int NUM_TIME_SLOTS = 24;

    @NotNull
    private int timeSlots;

    //Used by Hibernate. Private is more readable. Protected will improve performance.
    protected TimeReservation(){

    }

    public TimeReservation(@NotNull boolean[] timeSlots){
        this.timeSlots=arrayToInteger(timeSlots);
    }

    //Used by Hibernate. Private is more readable. Protected will improve performance.
    protected void setTimeSlots(boolean[] timeSlots) {
        this.timeSlots = arrayToInteger(timeSlots);
    }

    public boolean[] getTimeSlots() {
        return integerToArray(timeSlots);
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

        if (result==0) throw new IllegalArgumentException("Reservation should have at least one hour ");

        return result;
    }

    public boolean equals(TimeReservation time2){
        return this.timeSlots==time2.timeSlots;
    }

    public boolean isCompatibleWith(TimeReservation time2){
        return (this.timeSlots & time2.timeSlots)==0;
    }
}
