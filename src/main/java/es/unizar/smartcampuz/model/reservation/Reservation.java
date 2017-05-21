package es.unizar.smartcampuz.model.reservation;

import es.unizar.smartcampuz.model.report.ReportState;
import es.unizar.smartcampuz.model.worker.Worker;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReservationState state = ReservationState.PENDING;

    @NotNull
    @Type(type="date")
    private Date date;

    @NotNull
    @Embedded
    private TimeReservation timeReservation;

    public Reservation(){ }

    public Reservation(long id) {
        this.id = id;
    }

    public Reservation(@NotNull String roomID, @NotNull String userID, @NotNull String description, @NotNull Date date,
                       @NotNull TimeReservation timeReservation){
        this.roomID=roomID;
        this.userID=userID;
        this.description=description;
        this.date=date;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setTimeReservation(TimeReservation timeReservation) {
        this.timeReservation = timeReservation;
    }

    public TimeReservation getTimeReservation() {
        return timeReservation;
    }
}
