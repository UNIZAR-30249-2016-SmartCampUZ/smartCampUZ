package es.unizar.smartcampuz.model.report;


/**
 * This service checks if a transition can be made from a given state.
 *
 * INBOX -> NOTIFIED*
 *       -> REFUSED*
 *       -> APPROVED -> ASIGNED -> DONE -> CONFIRMED*
 *
 */
public class ReportStateChecker {

    /**
     * Return true if a new state can be set to a given report or false if not.
     * @param report the report which has the actual state.
     * @param newState the new state which will be evaluated.
     */
    public static boolean checkTransition(Report report, ReportState newState){
        switch (report.getState()) {
            case INBOX:
                switch (newState) {
                    case NOTIFIED:
                        return true;
                    case REFUSED:
                        return true;
                    case APPROVED:
                        return true;
                }
                break;
            case APPROVED:
                switch (newState) {
                    case ASSIGNED:
                        if (report.getWorker() != null)
                            return true;
                }
                break;
            case ASSIGNED:
                switch (newState){
                    case DONE:
                        if(report.getWorker()!=null)
                            return true;
                    case ASSIGNED:
                        if(report.getWorker()!=null)
                            return true;
                }
                break;
            case DONE:
                switch (newState){
                    case CONFIRMED:
                        return true;
                }
                break;
        }
        return false;

    }
}
