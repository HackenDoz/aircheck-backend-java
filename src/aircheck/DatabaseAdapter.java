package aircheck;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class DatabaseAdapter {

    public static final double EPS = 0.000001;

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
        return Math.abs(a - b) <= EPS;
    }

    public void getReports(ArrayList<UserReport> reports) {
        reports.clear();

        ArrayList<UserReport> tempReports = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM reports");

            while (rs.next()) {
                UserReport report = new UserReport();
                report.reportID = rs.getInt("id");
                report.latitude = rs.getDouble("latitude");
                report.longitude = rs.getDouble("longitude");
                report.submissionTime = rs.getDate("created_at");

                report.addSymptomReport(new UserReport.SymptomReport(rs.getInt("SymptomID"), rs.getInt("Severity")));

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

    public void addMappingPoint(MapPoint point) {
        try {
            Statement statement = connection.createStatement();


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
