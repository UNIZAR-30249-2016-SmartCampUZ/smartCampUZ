package es.unizar.smartcampuz.infrastructure.repositories;

import es.unizar.smartcampuz.model.reservation.Reservation;
import es.unizar.smartcampuz.model.reservation.ReservationRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * A repository for the entity Reservation is simply created by extending the CrudRepository
 * interface.
 * Represents a collection of every Reservation stored in the system.
 */
public interface ReservationRepositoryImplementation extends ReservationRepository, CrudRepository<Reservation, Long> {

}
