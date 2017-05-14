package es.unizar.smartcampuz.model.worker;

import es.unizar.smartcampuz.model.EntityRepository;

/**
 * A repository for the entity Worker is simply created by extending the EntityRepository.
 * Represents a collection of every Worker stored in the system.
 */
public interface WorkerRepository extends EntityRepository<Worker, Long> {
    /**
     * Return the worker having the passed email or null if no user is found.
     *
     * @param email the worker email.
     */
    public Worker findByEmail(String email);
}
