package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.infrastructure.auth.Credential;
import es.unizar.smartcampuz.infrastructure.auth.CredentialRepository;
import es.unizar.smartcampuz.infrastructure.service.JsonService;
import es.unizar.smartcampuz.infrastructure.service.SmtpMailService;

import es.unizar.smartcampuz.model.report.Report;
import es.unizar.smartcampuz.model.report.ReportRepository;
import es.unizar.smartcampuz.model.report.ReportState;
import es.unizar.smartcampuz.model.report.ReportStateChecker;
import es.unizar.smartcampuz.model.reservation.*;
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
import java.util.*;


@Controller
public class AdminDashboardController {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private WorkerRepository workerRepository;
    
    @Autowired
    private SmtpMailService smtpMailSender;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    @GetMapping("/listReports")
    @ResponseBody
    public ResponseEntity<String> listReports (HttpServletRequest request) throws IOException{
        String location = request.getHeader("location");

        if(location==null){
            return new ResponseEntity<>("\"Debe introducir una localización válida.\"", HttpStatus.BAD_REQUEST);
        }

        JSONArray reportList;
        JSONObject response = new JSONObject();

        if(location.trim().equals("") || location.trim().equals("0")){
            reportList = JsonService.createReportList(reportRepository.findAll());
        }
        else{
            reportList = JsonService.createReportList(reportRepository.findByRoomID(location));
        }
        response.put("reports", reportList);
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

        if(isBlank(state)){
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

    @GetMapping("/listReservations")
    @ResponseBody
    public ResponseEntity<String> listReservations (HttpServletRequest request) throws IOException{
        String location = request.getHeader("location");

        if(location==null){
            return new ResponseEntity<>("\"Debe introducir una localización válida.\"", HttpStatus.BAD_REQUEST);
        }

        JSONArray reportList;
        JSONObject response = new JSONObject();

        if(location.trim().equals("") || location.trim().equals("0")){

            reportList = createReservationList(reservationRepository.findAllByState(ReservationState.PENDING));
        }
        else{
            reportList = createReservationList(
                reservationRepository.findAllByRoomIDAndState(location, ReservationState.PENDING));

        }
        response.put("reservations", reportList);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @PutMapping("/reservation")
    @ResponseBody
    public ResponseEntity<String> approveOrDenyReservation (HttpServletRequest request) throws IOException{
        long reservationId;
        boolean approved;
        JSONObject json;
        Reservation reservation;

        try{
            json = JsonService.readJson(request.getReader());
            reservationId = json.getInt("id");
            approved = json.getBoolean("approved");
            reservation = reservationRepository.findOne(reservationId);

        }
        catch (Exception e){
            return new ResponseEntity<>("\"Error interno en el servidor.\"", HttpStatus.INTERNAL_SERVER_ERROR);
        }

            // TODO: Comprobar que findAllByRoomIDAndDateAndState() busca bien

        if(reservation==null){
            return new ResponseEntity<>("\"Reserva no encontrada.\"", HttpStatus.NOT_FOUND);
        }

        Set<Reservation> approvedReservations = reservationRepository.findAllByRoomIDAndDateAndState(reservation.getRoomID(),
            reservation.getDate(), ReservationState.APPROVED);
        List<Long> deniedReservations = new ArrayList<>();

        //Checks compatibility policy with all given approved reservations
        JSONObject response = new JSONObject();
        if(approved && ReservationChecker.checkSchedule(reservation.getTimeReservation(), approvedReservations)){
            //If it is compatible we save it as approved
            reservation.setState(ReservationState.APPROVED);
            reservationRepository.save(reservation);

            //We look for the pending reservations that are hereinafter incompatible
            Set<Reservation> pendingReservations = reservationRepository.findAllByRoomIDAndDateAndState(reservation.getRoomID(),
                reservation.getDate(), ReservationState.PENDING);

            for(Reservation pendingReservation: pendingReservations){
                if( !reservation.getTimeReservation().isCompatibleWith(pendingReservation.getTimeReservation())){

                    deniedReservations.add(pendingReservation.getId()); //Add id into denied list
                    pendingReservation.setState(ReservationState.DENIED); //Mark reservation as denied
                    reservationRepository.save(pendingReservation); //Save reservation
                }
            }
             
            // Sends an email to the user letting him know his reservation was approved.
            String email = reservation.getUserID();       
            smtpMailSender.sendReservationEmail(email, reservationId, approved);

            response.element("deletedRequests", deniedReservations.toArray());
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        }
        else if(!approved){
            //If the command is DENY we change the state and save it
            reservation.setState(ReservationState.DENIED);
            reservationRepository.save(reservation);          
            
            // Sends an email to the user letting him know his reservation was denied.
            String email = reservation.getUserID();
            smtpMailSender.sendReservationEmail(email, reservationId, approved);
            
            //Create the response
            deniedReservations.add(reservation.getId());
            response.element("deletedRequests", deniedReservations.toArray());
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("\"Reserva en conflicto con otra. No puede aprobarse.\"", HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * Checks if the username and password fields are null or empty.
     */
    private boolean isBlank(String field){
        return field==null || field.trim().equals("");
    }

    private JSONArray createReservationList(Iterable<Reservation> iter){
        JSONArray reservartionList = new JSONArray();
        for(Reservation reservation: iter){
            JSONObject jReservation = new JSONObject();
            Calendar cal = Calendar.getInstance();
            cal.setTime(reservation.getDate());
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH)+1;
            Credential professor = credentialRepository.findByEmail(reservation.getUserID());

            jReservation.element("id", reservation.getId());
            jReservation.element("location", reservation.getRoomID());
            jReservation.element("day", day);
            jReservation.element("month", month);
            jReservation.element("professor", professor!=null);
            jReservation.element("email", reservation.getUserID());
            reservartionList.add(jReservation);
        }
        return reservartionList;
    }
}
