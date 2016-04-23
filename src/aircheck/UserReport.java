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

    public double getDistance(UserReport report){
        double dLatitude= Math.abs(this.latitude - report.latitude);
        double dLongitude = Math.abs(this.longitude - report.longitude);

        double a = Math.pow(Math.sin(dLatitude / 2.0), 2) +
                Math.cos(this.latitude) * Math.cos(report.latitude) * Math.pow(Math.sin(dLongitude / 2.0), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double d = 12756.0 * c;

        return d;
    }


}
