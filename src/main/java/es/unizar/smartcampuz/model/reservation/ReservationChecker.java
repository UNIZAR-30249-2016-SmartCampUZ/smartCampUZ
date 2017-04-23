package es.unizar.smartcampuz.model.reservation;


public class ReservationChecker {

    public static final int START_HOUR = 7;
    public static final int FINISH_HOUR = 19;

    // TODO: El iterable hay que cambiar ? por una Reservation
    public static boolean checkSchedule (boolean [] reservation, Iterable<?> iter){

        for(Object o: iter){
            // TODO: Cogeré este array del objeto Reservation
            boolean [] reserved = new boolean[24];
            for(int i=START_HOUR;i<=FINISH_HOUR;i++){
                if(reservation[i] && reserved[i]){
                    return false;
                }
            }
        }
        return true;
    }
}
