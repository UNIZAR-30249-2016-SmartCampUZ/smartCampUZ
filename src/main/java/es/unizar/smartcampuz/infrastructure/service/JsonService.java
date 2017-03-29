package es.unizar.smartcampuz.infrastructure.service;

import es.unizar.smartcampuz.model.report.Report;
import es.unizar.smartcampuz.model.worker.Worker;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;

public class JsonService {

    public static JSONObject readJson (BufferedReader bf) throws Exception {
        StringBuffer jb = new StringBuffer();
        String line = null;

        while ((line = bf.readLine()) != null) {
            jb.append(line);
        }

        return JSONObject.fromObject(jb.toString());
    }

    public static JSONArray createFeedbackList(Iterable<Report> iter){
        JSONArray feedbackList = new JSONArray();
        for(Report report : iter){
            String title = report.getDescription().length()>40 ?
                report.getDescription().substring(0, 40) : report.getDescription();

            JSONObject feedback = new JSONObject();
            feedback.element("state", report.getState());
            feedback.element("worker", report.getWorker()==null ? "" : report.getWorker().getUserData().getName());
            feedback.element("description", report.getDescription());
            feedback.element("title", title);
            // Por el momento devuelvo el ID del lugar en vez de el nombre porque aún no existe la entidad Room
            feedback.element("location", report.getRoomID());
            feedback.element("id", report.getId());
            feedbackList.add(feedback);
        }
        return feedbackList;
    }

    public static JSONArray createWorkerList(Iterable<Worker> iter){
        JSONArray workerList = new JSONArray();
        for(Worker worker : iter){
            JSONObject w = new JSONObject();
            w.element("id", worker.getId());
            w.element("email", worker.getEmail());
            w.element("name", worker.getUserData().getName());
            workerList.add(w);
        }
        return workerList;
    }
}
