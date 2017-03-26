package es.unizar.smartcampuz.model.report;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ReportRepository extends CrudRepository<Report, Long> {

    //You should use worker.getReports()
//    public Iterable<Report> findByWorkerID(long id);
}
