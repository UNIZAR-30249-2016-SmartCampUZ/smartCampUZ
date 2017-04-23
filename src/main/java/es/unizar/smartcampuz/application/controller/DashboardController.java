package es.unizar.smartcampuz.application.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import es.unizar.smartcampuz.infrastructure.service.*;
import es.unizar.smartcampuz.model.report.Report;
import es.unizar.smartcampuz.model.report.ReportRepository;

@Controller
public class DashboardController {

    private static final Logger LOG = LoggerFactory
        .getLogger(CredentialController.class);

    private static final int START_HOUR = 7;
    private static final int FINISH_HOUR = 19;

    @Autowired
    private ReportRepository reportRepository;

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
    public ResponseEntity<String> newReservation (HttpServletRequest request) throws IOException{
        String location;
        String email;
        String description;
        int day;
        int month;
        boolean [] requestedHours;

        JSONObject json;
        JSONArray jRequestedHours;

        try{
            json = JsonService.readJson(request.getReader());
            location = json.getString("location");
            email = json.getString("email");
            description = json.getString("description");
            day = json.getInt("day");
            month = json.getInt(("month"));
            jRequestedHours = json.getJSONArray("requestedHours");
        }
        catch (Exception e){
            return new ResponseEntity<>("\"Error interno en el servidor.\"", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        requestedHours = JsonService.JSONArrayToBooleanArray(jRequestedHours);

        //TODO: ¿Comprobar que la localización existe?

        // Checks if the date is a valid one
        if(!isValidDate(day, month)){
            return new ResponseEntity<>("\"La fecha no es válida\"", HttpStatus.BAD_REQUEST);
        }
        if(requestedHours.length != 24){
            return new ResponseEntity<>("\"La lista de horas no es válida\"", HttpStatus.BAD_REQUEST);
        }
        if(isBlank(email) && isBlank(description) && isBlank(location)){
            return new ResponseEntity<>("\"Debes introducir localización, email y descripción\"", HttpStatus.BAD_REQUEST);
        }
        // TODO: Comprobar si se puede reservar cuando esté hecha la política de comprobación
        // TODO: Crear reserva
        return new ResponseEntity<>("\"Reserva solicitada correctamente.\"", HttpStatus.OK);
    }


    @GetMapping("/availableHours")
    @ResponseBody
    public ResponseEntity<String> listAvailableHours (HttpServletRequest request) throws IOException{
        String location = request.getHeader("location");
        int day = Integer.parseInt(request.getHeader("day"));
        int month = Integer.parseInt(request.getHeader("month"));

        //TODO: ¿Comprobar que la localización existe?

        if(!isValidDate(day, month)){
            return new ResponseEntity<>("\"La fecha no es válida\"", HttpStatus.BAD_REQUEST);
        }

        //TODO: Pedir reservas aprobadas de una localización en un día concreto
        Iterable<?> iter = new ArrayList();
        // Initializes the array with 'false' value in all it's fields
        boolean [] availableHours = new boolean[24];
        // Iterates all reservations for that day and location
        for(Object o: iter){
            // TODO: Cogeré este array del objeto Reservation
            boolean [] reservation = new boolean[24];
            /*
             * A 'false' value means there's no reservation in that hour in both arrays.
             *
             * False OR False -> False (No reservation on that hour)
             * False OR True  -> True  (A reservation on that hour. Now listed as reservation in availableHours)
             * True  OR False -> True  (A reservation on that hour. There was a reservation in availableHours)
             * True  OR True  -> True  (A reservation on that hour. There was a reservation in availableHours. This operation shouldn't happen.)
             */
            for(int i=START_HOUR;i<=FINISH_HOUR;i++){
                availableHours[i] = availableHours[i] || reservation[i];
            }
        }
        // Reverses all values in availableHours from START_HOUR to FINISH_HOUR
        // so that a True value will mean an available hour and not a reservation.
        for(int i = START_HOUR;i<=FINISH_HOUR;i++){
            availableHours[i] = !availableHours[i];
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
    private boolean isValidDate(int day, int month){
        GregorianCalendar cal = new GregorianCalendar();
        cal.setLenient(false);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month-1);
        try{
            cal.getTime();
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
}
