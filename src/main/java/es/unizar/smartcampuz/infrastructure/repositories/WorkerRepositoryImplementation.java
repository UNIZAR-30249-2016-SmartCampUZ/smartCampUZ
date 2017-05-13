package es.unizar.smartcampuz.infrastructure.repositories;

import es.unizar.smartcampuz.model.worker.Worker;
import es.unizar.smartcampuz.model.worker.WorkerRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * A repository for the entity Worker is simply created by extending the CrudRepository
 * interface provided by spring.
 * Represents a collection of every Worker stored in the system.
 */
public interface WorkerRepositoryImplementation extends WorkerRepository, CrudRepository<Worker, Long> {

}
