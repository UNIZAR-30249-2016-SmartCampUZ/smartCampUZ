package es.unizar.smartcampuz.infrastructure.repositories;

import es.unizar.smartcampuz.model.report.Report;
import es.unizar.smartcampuz.model.report.ReportRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * A repository for the entity Worker is simply created by extending the CrudRepository
 * interface provided by spring.
 * Represents a collection of every Report stored in the system.
 */
public interface ReportRepositoryImplementation extends ReportRepository, CrudRepository<Report, Long> {

    }
