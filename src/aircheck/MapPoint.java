package aircheck;

public class MapPoint {

    public int reportID;

    public double latitude;
    public double longitude;
    public double radius;

    public MapPoint() {
    }

    public MapPoint(double _latitude, double _longitude, double _radius) {
        this.latitude = _latitude;
        this.longitude = _longitude;
        this.radius = _radius;
    }

    public double getDistance(MapPoint point) {
        return DatabaseAdapter.distFrom(latitude, longitude, point.latitude, point.longitude);
    }
}
