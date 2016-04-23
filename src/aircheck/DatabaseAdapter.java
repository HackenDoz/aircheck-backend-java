package aircheck;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class DatabaseAdapter {
    private MysqlDataSource dataSource;
    private Connection connection;

    public DatabaseAdapter() throws Exception {

        dataSource = new MysqlDataSource();
        dataSource.setServerName(ServerConfig.MYSQL_HOST);
        dataSource.setPort(ServerConfig.MYSQL_PORT);
        dataSource.setDatabaseName(ServerConfig.MYSQL_DB);
        dataSource.setUser(ServerConfig.MYSQL_USER);
        dataSource.setPassword(ServerConfig.MYSQL_PASS);

        this.connection = dataSource.getConnection();
    }

    public boolean cmpDouble(double a, double b) {
        return Math.abs(a - b) <= ServerConfig.EPS;
    }

    public void getReportsBySymptom(ArrayList<UserReport> reports, int symptomID) {
        reports.clear();

        ArrayList<UserReport> tempReports = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM reports WHERE `symptom_id` = " + symptomID);

            while (rs.next()) {
                UserReport report = new UserReport(rs.getInt("id"), rs.getDouble("latitude"),
                        rs.getDouble("longitude"), rs.getDate("created_at"), symptomID);

                report.addSymptomSeverity(rs.getInt("severity"));
                System.out.println(report);
                tempReports.add(report);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Placeholder element to make sorting work
        tempReports.add(new UserReport(-1, 99999, 99999, new Date(0), -1));

        Collections.sort(tempReports);

        System.out.println("Reported Incidents");
        System.out.println("==================");
        for (UserReport report : tempReports) {
            System.out.println(report);
        }
        System.out.println("==================");

        int lastIndex = 0;

        for (int i = 1; i < tempReports.size(); i++) {
            if (tempReports.get(i).equals(tempReports.get(lastIndex))) {
                tempReports.get(lastIndex).symptoms.addAll(tempReports.get(i).symptoms);
            } else {
                reports.add(tempReports.get(lastIndex));
                lastIndex = i;
            }
        }
    }

    public void addMappingPoints(ArrayList<MapPoint> points) {
        if (points.size() == 0) return;

        String query = "INSERT INTO mapping (latitude, longitude, radius, severity, symptom_id) VALUES ";
        for (int i = 0; i < points.size(); i++) {
            MapPoint point = points.get(i);
            query += "(" + point.latitude + "," + point.longitude + "," + point.radius + "," + points.get(i).severity+ ","
                    + points.get(i).symptom + ")";
            if (i != points.size() - 1) {
                query += ",";
            }
        }

        try {
            Statement statement = connection.createStatement();
            System.out.println(query);
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void clearMappingPoints() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("TRUNCATE TABLE mapping");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
