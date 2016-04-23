package aircheck;

import java.util.ArrayList;
import java.util.Map;

public class Main {

    public static DatabaseAdapter dbAdapter;

    public static void main(String[] args) throws Exception {
        dbAdapter = new DatabaseAdapter();

        while (true) {
            ArrayList<MapPoint> finalPoints = new ArrayList<>();

            ArrayList<Symptom> symptoms = dbAdapter.getSymptoms();
            for (int i = 0; i < symptoms.size(); i++) {
                System.out.println("Analyzing reports for symptom: " + symptoms.get(i).name + "(" + symptoms.get(i).id + ")");
                ArrayList<UserReport> reports = new ArrayList<>();
                dbAdapter.getReportsBySymptom(reports, i);
                DisjointSet dset = new DisjointSet(reports);

                for (int x1 = 0; x1 < reports.size(); x1++) {
                    for (int x2 = x1 + 1; x2 < reports.size(); x2++) {
                        if (reports.get(x1).getDistance(reports.get(x2)) <= ServerConfig.CIRCLE_RADIUS) {
                            dset.merge(x1, x2);
                        }
                    }
                }

                dset.finish();

                for (Map.Entry<Integer, ArrayList<MapPoint>> entry : dset.points.entrySet()) {

                    if (entry.getValue() == null || entry.getValue().size() == 0) continue;

                    double minLat = Integer.MAX_VALUE, maxLat = Integer.MIN_VALUE;
                    double minLng = Integer.MAX_VALUE, maxLng = Integer.MIN_VALUE;

                    int totalSeverity = 0;

                    for (MapPoint mp : entry.getValue()) {
                        minLat = Math.min(minLat, mp.latitude);
                        maxLat = Math.max(maxLat, mp.latitude);

                        minLng = Math.min(minLng, mp.longitude);
                        maxLng = Math.max(maxLng, mp.longitude);

                        totalSeverity += mp.severity;
                    }

                    System.out.printf("minLat = %.3f, minLng = %.3f, maxLat = %.3f, maxLng = %.3f\n", minLat, minLng, maxLat, maxLng);

                    double nRad = MapPoint.distFrom(minLat, minLng, maxLat, maxLng) / 2.0 + ServerConfig.CIRCLE_RADIUS;

                    finalPoints.add(new MapPoint((minLat + maxLat) / 2.0, (minLng + maxLng) / 2.0,
                            nRad, i, totalSeverity / entry.getValue().size()));
                }
            }
            dbAdapter.addMappingPoints(finalPoints);

            try {
                Thread.sleep(5000);
            } catch (Exception ex) {
            }
        }
    }
}
