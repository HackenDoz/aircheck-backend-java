package aircheck;

import java.sql.Date;
import java.util.ArrayList;

public class UserReport implements Comparable<UserReport> {

    public static final double EPS = 0.000001;

    public static class SymptomReport {
        public int symptomID;
        public int severity;

        public SymptomReport(int _symptomID, int _severity) {
            this.symptomID = _symptomID;
            this.severity = _severity;
        }
    }

    public int reportID;
    public Date submissionTime;

    public double latitude;
    public double longitude;

    public ArrayList<SymptomReport> symptoms = new ArrayList<>();

    public UserReport() {
    }

    public UserReport(int _reportID, double _latitude, double _longitude, Date _submissionDate) {
        this.reportID = _reportID;
        this.latitude = _latitude;
        this.longitude = _longitude;
        this.submissionTime = _submissionDate;
    }

    public void addSymptomReport(SymptomReport _report) {
        this.symptoms.add(_report);
    }

    public double getDistance(UserReport report) {
        double dLatitude = Math.abs(this.latitude - report.latitude);
        double dLongitude = Math.abs(this.longitude - report.longitude);

        double a = Math.pow(Math.sin(dLatitude / 2.0), 2) +
                Math.cos(this.latitude) * Math.cos(report.latitude) * Math.pow(Math.sin(dLongitude / 2.0), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double d = 12756.0 * c;

        return d;
    }

    @Override
    public int compareTo(UserReport report) {
        if (Math.abs(this.latitude - report.latitude) > EPS)
            return (this.latitude < report.latitude ? -1 : 1);
        return (this.longitude < report.longitude ? -1 : 1);
    }

    public boolean equals(UserReport report) {
        return Math.abs(this.latitude - report.latitude) < EPS &&
                Math.abs(this.longitude - report.longitude) < EPS;
    }

    public void printInfo(){
        System.out.printf("reportID = %d, lat = %.4f, lng = %.4f\n", reportID, latitude, longitude);
    }

}
