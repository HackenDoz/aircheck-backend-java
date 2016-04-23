package aircheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static ArrayList<UserReport> reports;
    public static DatabaseAdapter dbAdapter;
    public static Map<Integer, ArrayList<MapPoint>> points;

    public static int[] disjointSet;

    public static void disjoint_init() {
        for (int i = 0; i < reports.size(); i++) disjointSet[i] = i;
    }

    public static int disjoint_find(int n) {
        while (disjointSet[n] != n)
            n = disjointSet[n];
        return n;
    }

    public static void disjoint_merge(int a, int b) {
        int ra = disjoint_find(a), rb = disjoint_find(b);
        if (ra == rb) return;

        disjointSet[ra] = rb;
    }

    public static void main(String[] args) throws Exception {
        reports = new ArrayList<>();
        dbAdapter = new DatabaseAdapter();
        points = new HashMap<>();

        while (true) {
            System.out.println("Waiting for reports");

            dbAdapter.getReports(reports);

            disjointSet = new int[reports.size()];
            disjoint_init();

            for (int i = 0; i < reports.size(); i++) {
                for (int j = i + 1; j < reports.size(); j++) {
                    if (reports.get(i).getDistance(reports.get(j)) < ServerConfig.CIRCLE_RADIUS) {
                        disjoint_merge(i, j);
                    }
                }
            }

            points.clear();

            for (int i = 0; i < reports.size(); i++) {
                if (points.get(disjoint_find(i)) == null)
                    points.put(disjoint_find(i), new ArrayList<MapPoint>());

                points.get(disjoint_find(i)).add(new MapPoint(reports.get(i).latitude,
                        reports.get(i).longitude, ServerConfig.CIRCLE_RADIUS));
            }

            dbAdapter.clearMappingPoints();
            ArrayList<MapPoint> mapPoints = new ArrayList<>();
            for (Map.Entry<Integer, ArrayList<MapPoint>> entry : points.entrySet()) {
                if (entry.getValue() != null && entry.getValue().size() != 0) {
                    double minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
                    double minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

                    for (MapPoint pp : entry.getValue()) {
                        minX = Math.min(minX, pp.latitude);
                        maxX = Math.max(maxX, pp.latitude);

                        minY = Math.min(minY, pp.longitude);
                        maxY = Math.max(maxY, pp.longitude);
                    }

                    mapPoints.add(new MapPoint((minX + maxX) / 2.0, (minY + maxY) / 2.0,
                            Math.max(maxX - minX, maxY - minY) / 2.0 + ServerConfig.CIRCLE_RADIUS));
                }
            }
            dbAdapter.addMappingPoints(mapPoints);

            try {
                Thread.sleep(5000);
            } catch (Exception ex) {
            }
        }
    }
}
