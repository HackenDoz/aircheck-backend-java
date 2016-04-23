package aircheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisjointSet {
    public ArrayList<UserReport> reports;
    public Map<Integer, ArrayList<MapPoint>> points;
    public int[] disjointSet;

    public DisjointSet(ArrayList<UserReport> _reports) {
        this.disjointSet = new int[_reports.size()];

        this.reports = _reports;
        for (int i = 0; i < reports.size(); i++)
            disjointSet[i] = i;
    }

    public int find(int n) {
        while (disjointSet[n] != n) n = disjointSet[n];
        return n;
    }

    public void merge(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return;

        disjointSet[ra] = rb;
    }

    public void finish(){
        points = new HashMap<>();

        for (int i = 0; i < reports.size(); i++){
            if (points.get(find(i)) == null)
                points.put(find(i), new ArrayList<MapPoint>());

            double lat = reports.get(i).latitude, lng = reports.get(i).longitude;
            int symptom = reports.get(i).symptomID, severity = reports.get(i).getSeverity();
            points.get(find(i)).add(new MapPoint(lat, lng, ServerConfig.CIRCLE_RADIUS, symptom, severity));
        }
    }
}

