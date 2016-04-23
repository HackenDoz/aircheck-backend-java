package aircheck;

public class MapPoint {
    public double latitude;
    public double longitude;
    public double radius;

    public MapPoint(){}
    public MapPoint(double _latitude, double _longitude, double _radius){
        this.latitude = _latitude;
        this.longitude = _longitude;
        this.radius = _radius;
    }

    public double getDistance(MapPoint point){
        double dLatitude= Math.abs(this.latitude - point.latitude);
        double dLongitude = Math.abs(this.longitude - point.longitude);
        double a = Math.pow(Math.sin(dLatitude / 2.0), 2) +
                (Math.cos(this.latitude) * Math.cos(point.latitude) * Math.pow(Math.sin(dLongitude / 2.0), 2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 12756.0 * c;
        return d;
    }
}
