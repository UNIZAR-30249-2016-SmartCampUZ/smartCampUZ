package es.unizar.smartcampuz.model.worker;

import org.springframework.data.repository.CrudRepository;

/**
 * A repository for the entity Worker is simply created by extending the CrudRepository
 * interface provided by spring. The following methods are some of the ones
 * available from such interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature!
 *
 */
public interface WorkerRepository extends CrudRepository<Worker, Long> {
    /**
     * Return the worker having the passed email or null if no user is found.
     *
     * @param email the worker email.
     */
    public Worker findByEmail(String email);
}
