package aircheck;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

    public void getReports(ArrayList<UserReport> reports){
        reports.clear();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM reports");

            while(rs.next()){
                UserReport report = new UserReport();
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
