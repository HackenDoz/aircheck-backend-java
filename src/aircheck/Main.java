package aircheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static Map<Integer, UserReport> reports;
    public static DatabaseAdapter dbAdapter;

    public static void main(String[] args) throws Exception {
        reports = new HashMap<>();
        dbAdapter = new DatabaseAdapter();

        while(true){
            try{ Thread.sleep(5000); } catch (Exception ex) { }
            dbAdapter.getReports(reports);
        }
    }
}
