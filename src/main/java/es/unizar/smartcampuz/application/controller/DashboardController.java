package es.unizar.smartcampuz.application.controller;

import es.unizar.smartcampuz.model.reservation.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import es.unizar.smartcampuz.infrastructure.service.*;
import es.unizar.smartcampuz.model.report.Report;
import es.unizar.smartcampuz.model.report.ReportRepository;

@Controller
public class DashboardController {

    private static final Logger LOG = LoggerFactory
        .getLogger(CredentialController.class);
    private static final SimpleDateFormat dateFormater = new SimpleDateFormat("ddMMyyyy");

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @PostMapping("/report")
    @ResponseBody
    public ResponseEntity<String> newReport (HttpServletRequest request) throws IOException{
        String description;
        String location;

        JSONObject json;

        try{
            json = JsonService.readJson(request.getReader());
            description = json.getString("description");
            location = json.getString("location");
        }
        catch (Exception e){
            return new ResponseEntity<>("\"Error interno en el servidor.\"", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOG.info("Location: "+location+" Description: "+description);
        if(isBlank(description)){
            return new ResponseEntity<>("\"Debe introducir una descripción.\"", HttpStatus.BAD_REQUEST);
        }
        if(isBlank(location)){
            return new ResponseEntity<>("\"Debe introducir una localización.\"", HttpStatus.BAD_REQUEST);
        }
        //TODO: ¿Comprobar que la localización existe?

        Report newReport = new Report(location, null, description);
        reportRepository.save(newReport);
        return new ResponseEntity<>("\"Report guardado correctamente.\"", HttpStatus.OK);
    }

    @PostMapping("/reservation")
    @ResponseBody
    public ResponseEntity<String> newReservation (@RequestParam String location, @RequestParam String email,
                                                  @RequestParam String description, @RequestParam int month, @RequestParam int day,
                                                  @RequestParam boolean[] requestedHours) throws IOException, ParseException{

//        JSONObject json;
//        JSONArray jRequestedHours;
//
//        try{
//            json = JsonService.readJson(request.getReader());
//            location = json.getString("location");
//            email = json.getString("email");
//            description = json.getString("description");
//            day = json.getInt("day");
//            month = json.getInt(("month"));
//            jRequestedHours = json.getJSONArray("requestedHours");
//        }
//        catch (Exception e){
//            return new ResponseEntity<>("\"Error interno en el servidor.\"", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        requestedHours = JsonService.JSONArrayToBooleanArray(jRequestedHours);

        //TODO: ¿Comprobar que la localización existe?

        // Checks if the date is a valid one
//        if(!isValidDate(day, month)){
//            return new ResponseEntity<>("\"La fecha no es válida\"", HttpStatus.BAD_REQUEST);
//        }
        if(requestedHours.length != 24){
            return new ResponseEntity<>("\"La lista de horas no es válida\"", HttpStatus.BAD_REQUEST);
        }
        if(isBlank(email) && isBlank(description) && isBlank(location)){
            return new ResponseEntity<>("\"Debes introducir localización, email y descripción\"", HttpStatus.BAD_REQUEST);
        }

        //Creates our object to work with
        TimeReservation timeReservation = new TimeReservation(requestedHours);
        Reservation reservation = new Reservation(location, email,
            dateFormater.parse(String.format("%02d%02d2017", day, month)), timeReservation);

        //Takes all approved reservations in a room in a day
        Set<Reservation> approvedReservations = reservationRepository.findAllByRoomIDAndDateAndState(
            reservation.getRoomID(), reservation.getDate(), ReservationState.APPROVED);

        //Checks compatibility policy with all given approved reservations
        if(ReservationChecker.checkSchedule(reservation.getTimeReservation(), approvedReservations)){
            reservationRepository.save(reservation);
            return new ResponseEntity<>("\"Reserva solicitada correctamente.\"", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("\"Reserva en conflicto con otra. No puede aprobarse.\"", HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/availableHours")
    @ResponseBody
    public ResponseEntity<String> listAvailableHours(@RequestParam String location, @RequestParam int day,
                                                      @RequestParam int month) throws IOException, ParseException{

        //TODO: ¿Comprobar que la localización existe?

//        if(!isValidDate(day, month)){
//            return new ResponseEntity<>("\"La fecha no es válida\"", HttpStatus.BAD_REQUEST);
//        }

        Set<Reservation> approvedReservations = reservationRepository.findAllByRoomIDAndDateAndState(
            location, dateFormater.parse(String.format("%02d%02d2017", day, month)), ReservationState.APPROVED);
        // Initializes the array with 'false' value in all it's fields
        boolean [] availableHours = new boolean[24];
        Arrays.fill(availableHours, true);
        // Iterates all reservations for that day and location
        for(Reservation approvedReservation: approvedReservations){
            boolean [] reservationArray = approvedReservation.getTimeReservation().getTimeSlots();
            //It always represents the available hours
            for(int i = ReservationChecker.START_TIME_SLOT; i<=ReservationChecker.FINISH_TIME_SLOT; i++){
                availableHours[i] &= !reservationArray[i]; //If it where available(true) AND it's still available(true)
            }
        }
        JSONObject response = new JSONObject();

        response.element("availableHours", availableHours);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    /*
     * Checks if the username and password fields are null or empty.
     */
    private boolean isBlank(String field){
        return field==null || field.trim().equals("");
    }

    /*
     * Checks if a given date is a valid one.
     */
//    private boolean isValidDate(int day, int month){
//        GregorianCalendar cal = new GregorianCalendar();
//        cal.setLenient(false);
//        cal.set(Calendar.DAY_OF_MONTH, day);
//        cal.set(Calendar.MONTH, month-1);
//        try{
//            cal.getTime();
//        }
//        catch (Exception e){
//            return false;
//        }
//        return true;
//    }
}
