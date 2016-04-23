package aircheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static DatabaseAdapter dbAdapter;

    public static void main(String[] args) throws Exception {
        dbAdapter = new DatabaseAdapter();

        while (true) {
            dbAdapter.clearMappingPoints();

            for (int i = 0; i < ServerConfig.SYMPTOM_COUNT; i++){

                ArrayList<MapPoint> finalPoints = new ArrayList<>();

                ArrayList<UserReport> reports = new ArrayList<>();
                dbAdapter.getReportsBySymptom(reports, i);
                DisjointSet dset = new DisjointSet(reports);

                for (int x1 = 0; x1 < reports.size(); x1++){
                    for (int x2 = x1 + 1; x2 < reports.size(); x2++){
                        if (reports.get(x1).getDistance(reports.get(x2)) <= ServerConfig.CIRCLE_RADIUS){
                            dset.merge(x1, x2);
                        }
                    }
                }

                dset.finalize();

                for (Map.Entry<Integer, ArrayList<MapPoint>> entry : dset.points.entrySet()) {

                    if (entry.getValue() == null || entry.getValue().size() == 0) continue;

                    double minLat = Double.MAX_VALUE, maxLat = Double.MIN_VALUE;
                    double minLng = Double.MAX_VALUE, maxLng = Double.MIN_VALUE;

                    int totalSeverity = 0;

                    for (MapPoint mp : entry.getValue()){
                        minLat = Math.min(minLat, mp.latitude);
                        maxLat = Math.max(maxLat, mp.latitude);

                        minLng = Math.min(minLng, mp.longitude);
                        maxLng = Math.max(maxLng, mp.longitude);

                        totalSeverity += mp.severity;
                    }

                    finalPoints.add(new MapPoint((minLat + maxLat) / 2.0, (minLng + maxLng) / 2.0,
                            Math.max(maxLat - minLat, maxLng - minLng) / 2.0, i,
                            totalSeverity / entry.getValue().size()));
                }

                dbAdapter.addMappingPoints(finalPoints);
            }

            try{
                Thread.sleep(5000);
            } catch (Exception ex){

            }
        }
    }
}
