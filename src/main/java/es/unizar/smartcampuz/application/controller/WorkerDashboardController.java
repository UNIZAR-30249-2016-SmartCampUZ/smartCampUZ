package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.infrastructure.service.JwtService;
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
import java.util.ArrayList;

@Controller
public class WorkerDashboardController {

    private static final Logger LOG = LoggerFactory
        .getLogger(CredentialController.class);

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private JwtService jwtService;


    /**
     * Searches for a list of reports assigned to a worker and filtered by a
     * location. This location can be blank or 0 to indicate that no filter
     * must be applied on the returned list.
     *
     * @param request
     * @return ResponseEntity<String>
     * @throws IOException
     */
    @GetMapping("/listWorkerReports")
    @ResponseBody
    public ResponseEntity<String> listWorkerReports (HttpServletRequest request) throws IOException{
        String authHeader = request.getHeader("Authorization");
        String location = request.getHeader("location");

        if(location==null){
            return new ResponseEntity<>("\"Debe introducir una localización válida.\"", HttpStatus.BAD_REQUEST);
        }

        String workerEmail;
        try{
            workerEmail = jwtService.verify(authHeader.replaceAll("Bearer", "")).getEmail();
        }
        catch(Exception e){
            return new ResponseEntity<>("\"Error interno en el servidor.\"", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Worker worker = workerRepository.findByEmail(workerEmail);
        LOG.info(worker.getEmail());
        LOG.info(worker.getUserData().getName());
        JSONArray reportList;

        if(location.trim().equals("") || location.trim().equals("0")){
            reportList = JsonService.createReportList(reportRepository.findByWorker(worker));
        }
        else{
            reportList = JsonService.createReportList(reportRepository.findByRoomIDAndWorker(location, worker));
        }
        JSONObject response = new JSONObject();
        response.element("reports", reportList);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @PutMapping("/markReport")
    @ResponseBody
    public ResponseEntity<String> markReportAsDoneOrTrouble (HttpServletRequest request) throws IOException{
        String authHeader = request.getHeader("Authorization");

        long reportId;
        boolean done;
        String workerEmail;

        JSONObject json;
        try{
            workerEmail = jwtService.verify(authHeader.replaceAll("Bearer", "")).getEmail();
            json = JsonService.readJson(request.getReader());
            reportId = json.getLong("idReport");
            done = json.getBoolean("done");
        }
        catch(Exception e){
            return new ResponseEntity<>("\"Error interno en el servidor.\"", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Report report = reportRepository.findOne(reportId);
        Worker worker;
        if(report != null && (worker = report.getWorker()) != null && worker.getEmail().equals(workerEmail)){
            if(done && ReportStateChecker.checkTransition(report, ReportState.DONE)){
                report.setState(ReportState.DONE);
                reportRepository.save(report);
                return new ResponseEntity<>("\"Estado modificado correctamente a HECHO\"", HttpStatus.OK);
            }
            else if(!done && ReportStateChecker.checkTransition(report, ReportState.TROUBLE)){
                report.setState(ReportState.TROUBLE);
                reportRepository.save(report);
                return new ResponseEntity<>("\"Estado modificado correctamente a PROBLEMA\"", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("\"El cambio de estado solicitado no es posible.\"", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity<>("\"La sugerencia no existe o no la tienes asignada.\"", HttpStatus.NOT_FOUND);
        }
    }
}
