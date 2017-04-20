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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Calendar;

import es.unizar.smartcampuz.infrastructure.service.*;
import es.unizar.smartcampuz.model.report.Report;
import es.unizar.smartcampuz.model.report.ReportRepository;

@Controller
public class DashboardController {

    private static final Logger LOG = LoggerFactory
        .getLogger(CredentialController.class);

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
        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        try{
            cal.getTime();
        }
        catch (Exception e){
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

    /*
     * Checks if the username and password fields are null or empty.
     */
    private boolean isBlank(String field){
        return field==null || field.trim().equals("");
    }
}
