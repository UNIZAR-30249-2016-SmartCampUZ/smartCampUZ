package es.unizar.smartcampuz.model.reservation;

import es.unizar.smartcampuz.model.report.ReportState;
import es.unizar.smartcampuz.model.worker.Worker;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * This entity represents a reservation made in an specific room.
 */
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String roomID;

    @NotNull
    private String userID;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReservationState state = ReservationState.PENDING;

    @NotNull
    @Embedded
    private TimeReservation timeReservation;

    public Reservation(){ }

    public Reservation(long id) {
        this.id = id;
    }

    public Reservation(@NotNull String roomID, @NotNull String userID,
                       @NotNull TimeReservation timeReservation){
        this.roomID=roomID;
        this.userID=userID;
        this.timeReservation=timeReservation;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setState(ReservationState state) {
        this.state = state;
    }

    public ReservationState getState() {
        return state;
    }

    public void setTimeReservation(TimeReservation timeReservation) {
        this.timeReservation = timeReservation;
    }

    public TimeReservation getTimeReservation() {
        return timeReservation;
    }
}
