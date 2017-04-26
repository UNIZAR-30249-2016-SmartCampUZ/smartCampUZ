package es.unizar.smartcampuz.model.reservation;

import org.springframework.data.repository.CrudRepository;

/**
 * A repository for the entity Reservation is simply created by extending the CrudRepository
 * interface.
 * Represents a collection of every Reservation stored in the system.
 */
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    /**
     * Return the reservation having TODO
     *
     * @param userID the user id.
     */
    public Reservation findByUserID(String userID);
}
