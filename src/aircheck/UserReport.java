package aircheck;

import java.sql.Date;
import java.util.ArrayList;

public class UserReport {

    public static class SymptomReport{
        public Symptoms symptom;
        public int severity;

        public SymptomReport(Symptoms _symptom, int _severity){
            this.symptom = _symptom;
            this.severity = _severity;
        }
    }

    public int reportID;
    public Date submissionTime;

    public double latitude;
    public double longitude;

    public ArrayList<SymptomReport> symptoms;

    public UserReport(int _reportID, double _latitude, double _longitude, Date _submissionDate){
        this.reportID = _reportID;
        this.latitude = _latitude;
        this.longitude = _longitude;
        this.submissionTime = _submissionDate;
    }

    public void addSymptomReport(SymptomReport _report){
        this.symptoms.add(_report);
    }
}
