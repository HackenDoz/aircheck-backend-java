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

        //TODO: make the code not shit
        this.connection = dataSource.getConnection();
    }

    public boolean cmpDouble(double a, double b) {
        return Math.abs(a - b) <= ServerConfig.EPS;
    }

    public void getReports(ArrayList<UserReport> reports) {
        reports.clear();

        ArrayList<UserReport> tempReports = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM reports");

            while (rs.next()) {
                UserReport report = new UserReport();
                report.id = rs.getInt("id");
                report.latitude = rs.getDouble("latitude");
                report.longitude = rs.getDouble("longitude");
                report.createdDate = rs.getDate("created_at");

                report.addSymptomReport(new UserReport.SymptomReport(rs.getInt("symptom_id"), rs.getInt("severity")));
                System.out.println(report);

                tempReports.add(report);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Placeholder element to make sorting work
        tempReports.add(new UserReport(-1, 99999, 99999, new Date(0)));

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
        String query = "INSERT INTO mapping (latitude, longitude, radius, severity) VALUES ";
        for (int i = 0; i < points.size(); i++) {
            MapPoint point = points.get(i);
            query += "(" + point.latitude + "," + point.longitude + "," + point.radius + "," + 1 + ")";
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
            statement.executeUpdate("DELETE FROM mapping");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
