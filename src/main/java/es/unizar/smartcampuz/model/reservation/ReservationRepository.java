package es.unizar.smartcampuz.model.reservation;

import es.unizar.smartcampuz.model.EntityRepository;

import java.util.Date;
import java.util.Set;

/**
 * A repository for the entity Reservation is simply created by extending the EntityRepository.
 * Represents a collection of every Reservation stored in the system.
 */
public interface ReservationRepository extends EntityRepository<Reservation, Long> {
    /**
     * Return the reservation having TODO
     *
     * @param userID the user id.
     */
    public Reservation findByUserID(String userID);

    public Reservation findByIdAndState(long id, ReservationState state);

    /**
     * Return the reservation having TODO
     *
     * @param date the Date.
     */
    public Set<Reservation> findAllByRoomIDAndDateAndState(String roomID, Date date, ReservationState state);

    public Set<Reservation> findAllByRoomIDAndState(String location, ReservationState state);

    public Set<Reservation> findAllByState(ReservationState state);

}
