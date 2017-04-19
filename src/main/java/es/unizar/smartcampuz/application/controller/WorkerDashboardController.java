package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.infrastructure.service.JwtService;
import es.unizar.smartcampuz.infrastructure.service.JsonService;
import es.unizar.smartcampuz.model.report.Report;
import es.unizar.smartcampuz.model.report.ReportRepository;
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

        JSONArray reportList;

        if(location.trim().equals("") || location.trim().equals("0")){
            //TODO: Pedir al repo de reports la lista completa de reports del worker
            // Mock iterable object 'till reports Repo is working
            Iterable<Report> iter = new ArrayList<>();
            reportList = JsonService.createReportList(iter);
        }
        else{
            //TODO: Pedir al repo de reports la lista de reports del worker filtrados por localización
            // Mock iterable object 'till reports Repo is working
            Iterable<Report> iter = new ArrayList<>();
            reportList = JsonService.createReportList(iter);
        }
        JSONObject response = new JSONObject();
        response.element("reports", reportList);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }
}
