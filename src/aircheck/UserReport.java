package aircheck;

import java.sql.Date;
import java.util.ArrayList;

public class UserReport implements Comparable<UserReport> {
    public static class SymptomReport {
        public int id;
        public int severity;

        public SymptomReport(int id, int severity) {
            this.id = id;
            this.severity = severity;
        }
    }

    public int id;
    public double latitude;
    public double longitude;
    public Date createdDate;

    public ArrayList<SymptomReport> symptoms = new ArrayList<>();

    public UserReport() {
    }

    public UserReport(int id, double latitude, double longitude, Date createdDate) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdDate = createdDate;
    }

    public void addSymptomReport(SymptomReport _report) {
        this.symptoms.add(_report);
    }

    public double getDistance(UserReport report) {
        return MapPoint.distFrom(latitude, longitude, report.latitude, report.longitude);
    }

    @Override
    public int compareTo(UserReport report) {
        if (Math.abs(this.latitude - report.latitude) > ServerConfig.EPS)
            return (this.latitude < report.latitude ? -1 : 1);
        return (this.longitude < report.longitude ? -1 : 1);
    }

    public boolean equals(UserReport report) {
        return Math.abs(this.latitude - report.latitude) < ServerConfig.EPS &&
                Math.abs(this.longitude - report.longitude) < ServerConfig.EPS;
    }

    @Override
    public String toString() {
        return String.format("UserReport[id=%d, lat=%.4f, lng=%.4f]", id, latitude, longitude);
    }
}
