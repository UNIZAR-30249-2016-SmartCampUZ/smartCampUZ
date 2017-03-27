package es.unizar.smartcampuz.model.report;

import es.unizar.smartcampuz.model.worker.Worker;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ReportRepository extends CrudRepository<Report, Long> {

    public Set<Report> findByWorker(Worker worker);
    public Set<Report> findByRoomID(String room);
}
