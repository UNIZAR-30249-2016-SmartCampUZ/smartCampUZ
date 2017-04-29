package es.unizar.smartcampuz.model.reservation;

public class ReservationChecker {

    //Represents first valid TimeSlot 8:00-9:00
    public static final int START_TIME_SLOT = 8;

    //Represents last valid TimeSlot 20:00-21:00
    public static final int FINISH_TIME_SLOT =20;

    public static boolean checkSchedule (TimeReservation candidate, Iterable<Reservation> approvedReservations){
        boolean[] candidateArray = candidate.getTimeSlots();

        //Checks the size of the array
        if(candidateArray.length != TimeReservation.NUM_TIME_SLOTS){
            throw new IllegalArgumentException("Input array should have 24 elements");
        }

        //Checks there is no time slots before start time
        for(int i = 0; i < START_TIME_SLOT; i++) {
            if (candidateArray[i]) return false;
        }

        //Checks there is no time slots after finish time
        for(int i = FINISH_TIME_SLOT+1; i < TimeReservation.NUM_TIME_SLOTS; i++) {
            if (candidateArray[i]) return false;
        }

        //Checks there is no collision with any of the given approved TimeReservations
        for(Reservation approvedReservation: approvedReservations){
            if(!candidate.isCompatibleWith(approvedReservation.getTimeReservation())){
                return false;
            }
        }
        return true;
    }
}
