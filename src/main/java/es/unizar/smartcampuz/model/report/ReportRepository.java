package es.unizar.smartcampuz.model.report;

import es.unizar.smartcampuz.model.worker.Worker;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * A repository for the entity Worker is simply created by extending the CrudRepository
 * interface provided by spring.
 * Represents a collection of every Report stored in the system.
 */
public interface ReportRepository extends CrudRepository<Report, Long> {
    /**
     * Return every single report that is owned by a given worker
     * or null if this worker has no reports.
     * @param worker the worker who owns the reports.
     */
    public Set<Report> findByWorker(Worker worker);

    /**
     * Return every single report that is registered in a given room or null if this room has no reports.
     * @param room the room where the reports are registered.
     */
    public Set<Report> findByRoomID(String room);

    /**
     * Return every single report that is owned by a given worker and registered in a given room
     * or null if this worker has no reports in that room.
     * @param room the room where the reports are registered.
     */
    public Set<Report> findByRoomIDAndWorker(String room, Worker worker);
}
