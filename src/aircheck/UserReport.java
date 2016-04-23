package aircheck;

import java.sql.Date;
import java.util.ArrayList;

public class UserReport implements Comparable<UserReport> {
    public int id;
    public double latitude;
    public double longitude;
    public Date createdDate;

    public int symptomID;
    public ArrayList<Integer> symptoms = new ArrayList<>();

    public UserReport(int id, double latitude, double longitude, Date createdDate, int symptomID) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdDate = createdDate;
        this.symptomID = symptomID;
    }

    public void addSymptomSeverity(int report) {
        this.symptoms.add(report);
    }

    public double getDistance(UserReport report) {
        return MapPoint.distFrom(latitude, longitude, report.latitude, report.longitude);
    }

    public int getSeverity() {
        int total = 0;
        for (Integer symptom : symptoms) {
            total += symptom;
        }
        return (symptoms.size() == 0 ? 0 : total / symptoms.size());
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
        return String.format("UserReport[id=%d, lat=%.4f, lng=%.4f, sym=%d, sev=%d]", id, latitude, longitude, symptomID, getSeverity());
    }
}
