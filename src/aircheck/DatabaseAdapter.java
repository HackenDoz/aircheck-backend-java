package aircheck;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class DatabaseAdapter {

    private MysqlDataSource dataSource;
    private Connection connection;

    public DatabaseAdapter() throws Exception{

        dataSource = new MysqlDataSource();
        dataSource.setServerName(ServerConfig.MYSQL_HOST);
        dataSource.setPort(ServerConfig.MYSQL_PORT);
        dataSource.setDatabaseName(ServerConfig.MYSQL_DB);
        dataSource.setUser(ServerConfig.MYSQL_USER);
        dataSource.setPassword(ServerConfig.MYSQL_PASS);

        //TODO: make the code not shit
        this.connection = dataSource.getConnection();
    }

    public void getReports(Map<Integer, UserReport> reports){
        reports.clear();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM reports");

            while(rs.next()){
                UserReport report = reports.get(rs.getInt("ReportID"));
                if (report == null){
                    report.reportID = rs.getInt("ReportID");
                    report.submissionTime = rs.getDate("ReportDate");
                    report.latitude = rs.getDouble("Latitude");
                    report.longitude = rs.getDouble("Longitude");
                }

                report.addSymptomReport(new UserReport.SymptomReport(
                        Symptoms.valueOf(rs.getString("Symptom")), rs.getInt("Severity")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

}
