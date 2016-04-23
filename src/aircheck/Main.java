package aircheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static ArrayList<UserReport> reports;
    public static DatabaseAdapter dbAdapter;

    public static int[] disjointSet;

    public static void disjoint_init(){
        for (int i = 0; i < reports.size(); i++) disjointSet[i] = i;
    }

    public static int disjoint_find(int n){
        while(disjointSet[n] != n)
            n = disjointSet[n];
        return n;
    }

    public static void disjoint_merge(int a, int b){
        int ra = disjoint_find(a), rb = disjoint_find(b);
        if (ra == rb) return;

        disjointSet[ra] = rb;
    }

    public static void main(String[] args) throws Exception {
        reports = new ArrayList<>();
        dbAdapter = new DatabaseAdapter();

        while(true){
            try{ Thread.sleep(5000); } catch (Exception ex) { }
            dbAdapter.getReports(reports);

            disjointSet = new int[reports.size()];
            disjoint_init();

            for (int i = 0; i < reports.size(); i++){
                for (int j = i + 1; j < reports.size(); j++){
                    if (reports.get(i).getDistance(reports.get(j)) < ServerConfig.circleRadius){
                        disjoint_merge(i, j);
                    }
                }
            }
        }
    }
}
