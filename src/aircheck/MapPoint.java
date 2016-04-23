package aircheck;

public class MapPoint {
    public double latitude;
    public double longitude;
    public double radius;
    public int symptom;
    public int severity;

    public MapPoint(double _latitude, double _longitude, double _radius, int _symptom, int _severity) {
        this.latitude = _latitude;
        this.longitude = _longitude;
        this.radius = _radius;
        this.symptom = _symptom;
        this.severity = _severity;
    }

    public double getDistance(MapPoint point) {
        return distFrom(latitude, longitude, point.latitude, point.longitude);
    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (double) (earthRadius * c);
    }
}
