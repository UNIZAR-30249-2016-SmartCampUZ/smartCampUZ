package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.infrastructure.service.JsonService;
import es.unizar.smartcampuz.model.report.Report;
import es.unizar.smartcampuz.model.report.ReportRepository;
import es.unizar.smartcampuz.model.report.ReportState;
import es.unizar.smartcampuz.model.report.ReportStateChecker;
import es.unizar.smartcampuz.model.worker.Worker;
import es.unizar.smartcampuz.model.worker.WorkerRepository;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Controller
public class AdminDashboardController {

    private static final Logger LOG = LoggerFactory
        .getLogger(CredentialController.class);

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @GetMapping("/listFeedback")
    @ResponseBody
    public ResponseEntity<String> listFeedbacks(HttpServletRequest request) throws IOException{
        String location = request.getHeader("location");

        if(location==null){
            return new ResponseEntity<>("\"Debe introducir una localización válida.\"", HttpStatus.BAD_REQUEST);
        }

        JSONArray feedbackList;
        JSONObject response = new JSONObject();

        if(location.trim().equals("") || location.trim().equals("0")){
            feedbackList = JsonService.createFeedbackList(reportRepository.findAll());
        }
        else{
            feedbackList = JsonService.createFeedbackList(reportRepository.findByRoomID(location));
        }
        response.put("feedbacks", feedbackList);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @PutMapping("/state")
    @ResponseBody
    public ResponseEntity<String> modifyReportState(HttpServletRequest request) throws IOException{
        String state = "";
        long reportId;

        JSONObject json;
        try{
            json = JsonService.readJson(request.getReader());
            state = json.getString("state");
            reportId = json.getLong("id");
        }
        catch (Exception e){
            return new ResponseEntity<>("\"Error interno en el servidor.\"", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(state==null || state.trim().equals("")){
            return new ResponseEntity<>("\"Debe introducir un estado válido\"", HttpStatus.BAD_REQUEST);
        }

        Report report = reportRepository.findOne(reportId);
        if(report != null){
            if(ReportStateChecker.checkTransition(report, ReportState.valueOf(state))){
                report.setState(ReportState.valueOf(state));
                reportRepository.save(report);
                return new ResponseEntity<>("\"Estado modificado correctamente\"", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("\"El cambio de estado solicitado no es posible.\"", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity<>("\"La sugerencia no existe.\"", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignWorker")
    @ResponseBody
    public ResponseEntity<String> assignWorkerToReport(HttpServletRequest request) throws IOException{
        long workerId;
        long reportId;

        JSONObject json;

        try{
            json = JsonService.readJson(request.getReader());
            workerId = json.getLong("worker");
            reportId = json.getLong("id");
        }
        catch (Exception e){
            return new ResponseEntity<>("\"Error interno en el servidor.\"", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Report report = reportRepository.findOne(reportId);
        Worker worker = workerRepository.findOne(workerId);
        if(report != null && worker != null){
            report.setWorker(worker);
            if (ReportStateChecker.checkTransition(report, ReportState.ASSIGNED)){
                report.setState(ReportState.ASSIGNED);
                reportRepository.save(report);
                return new ResponseEntity<>("\"Trabajador asignado con éxito.\"", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("\"El cambio de estado solicitado no es posible.\"", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity<>("\"La sugerencia y/o el trabajador no existen.\"", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listWorkers")
    @ResponseBody
    public ResponseEntity<String> listWorkers() throws IOException{
        JSONArray workersList;
        JSONObject response = new JSONObject();

        workersList = JsonService.createWorkerList(workerRepository.findAll());
        response.put("workers", workersList);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }
}
