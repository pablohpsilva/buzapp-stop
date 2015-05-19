package aloeio.buzapp.app.Services;

import android.location.Location;
import android.util.Log;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * Created by pablohenrique on 2/22/15.
 */
public class NearAreaService {
    private boolean onBoard = false;
    private int proximityCounter = 0;
    private int separationCounter = 0;
    private ExceptionService exceptionControllerSingleton = ExceptionService.getInstance();

    final private int DISTANCE_ERROR_TOLERANCE = 1; //m
    final private int SPEED_ERROR_TOLERANCE = 3; // km
    final private int WALKING_SPEED = 5; // km
    final private int MIN_TIME_ON_BOARD = 40; // 2 minutes
    final private int MIN_TIME_NOT_ON_BOARD = 10; // 1 minutes

    final private double TINY_RADIUS_IN_METERS = 13;
    final private double LITTLE_RADIUS_IN_METERS = 50;
    final private double SMALL_RADIUS_IN_METERS = 100;
    final private double BIG_RADIUS_IN_METERS = 500;
    final private double LARGE_RADIUS_IN_METERS = 1000;
    final private double GIGANTIC_RADIUS_IN_METERS = 2000;
    final private double TO_METERS = 6371000;
    final private double TO_KILOMETERS = 6371;
    //    final private double DEGREES_TO_RADIANS = Math.PI/180;
    final private double DEGREES_TO_RADIANS = 0.0174532925199433;

    private boolean checkTwoSpeeds(double user, double bus){
        return ( Math.abs(bus - user) > SPEED_ERROR_TOLERANCE && user > WALKING_SPEED + 0.5 );
    }

    private boolean checkTwoDistances(double distance){ return (distance > DISTANCE_ERROR_TOLERANCE * 2); }

    public double getDistanceBetween(GeoPoint user, Location point) throws NullPointerException{
        return this.getDistanceHelper(user.getLatitude(), user.getLongitude(), point.getLatitude(), point.getLongitude());
    }

    public double getDistanceBetween(Location user, GeoPoint point) throws NullPointerException{
        return this.getDistanceHelper(user.getLatitude(), user.getLongitude(), point.getLatitude(), point.getLongitude());
    }

    public double getDistanceBetween(GeoPoint user, GeoPoint point) throws NullPointerException{
        return this.getDistanceHelper(user.getLatitude(), user.getLongitude(), point.getLatitude(), point.getLongitude());
    }

    public double getDistanceBetween(Location user, Location point) throws NullPointerException{
        return this.getDistanceHelper(user.getLatitude(), user.getLongitude(), point.getLatitude(), point.getLongitude());
    }

    public GeoPoint nearestGeoPoint(GeoPoint user, ArrayList<GeoPoint> points){
        try {
            GeoPoint nearest = null;
            double smallerDistance = 10000000;
            for (GeoPoint point : points) {
                if(point != null) {
                    double aux = getDistanceBetween(user, point);
                    if (aux < smallerDistance) {
                        smallerDistance = aux;
                        nearest = point;
                    }
                }
            }
            return nearest;
        } catch (NullPointerException e){
            exceptionControllerSingleton.catchException(NearAreaService.class, e, "bad math");
        } catch (Exception e){
            exceptionControllerSingleton.catchException(NearAreaService.class, e, "bad math");
        }
        return null;
    }

    public GeoPoint nearestGeoPoint(Location user, final ArrayList<GeoPoint> points){
        try {
            GeoPoint nearest = null;
            double smallerDistance = 10000000;
            for (GeoPoint point : points)
                if(point != null) {
                    double aux = getDistanceBetween(user, point);
                    if (aux < smallerDistance) {
                        smallerDistance = aux;
                        nearest = point;
                    }
                }
            return nearest;
        } catch (NullPointerException e){
            exceptionControllerSingleton.catchException(NearAreaService.class, e, "bad math");
        } catch (Exception e){
            exceptionControllerSingleton.catchException(NearAreaService.class, e, "bad math");
        }
        return null;
    }

    public RoadNode nearestRoadNode(Location user, ArrayList<RoadNode> points){
        try {
            RoadNode nearest = null;
            double smallerDistance = 10000000;
            for (RoadNode point : points)
                if(point != null) {
                    double aux = getDistanceBetween(user, point.mLocation);
                    if (aux < smallerDistance) {
                        smallerDistance = aux;
                        nearest = point;
                    }
                }
            return nearest;
        } catch (NullPointerException e){
            exceptionControllerSingleton.catchException(NearAreaService.class, e, "bad math");
        } catch (Exception e){
            exceptionControllerSingleton.catchException(NearAreaService.class, e, "bad math");
        }
        return null;
    }

    public void determineStatus(GeoPoint user, double userSpeed, GeoPoint bus, double busSpeed){
        try{
            if(user != null && bus != null) {
                if (checkTwoDistances(getDistanceBetween(user, bus)) && checkTwoSpeeds(userSpeed, busSpeed) ) {
                    proximityCounter++;
                    if (proximityCounter >= MIN_TIME_ON_BOARD) {
                        separationCounter = 0;
                        onBoard = true;
//                        Dispatcher.getInstance().dispatchEvent(new Event("departed"));
                    }
                } else if( onBoard == true && !checkTwoDistances(getDistanceBetween(user, bus)) ){
                    separationCounter++;
                    if(separationCounter >= MIN_TIME_NOT_ON_BOARD){
                        proximityCounter = 0;
                        onBoard = false;
//                        Dispatcher.getInstance().dispatchEvent(new Event("arrived"));
                    }
                }
//                else
//                    Dispatcher.getInstance().dispatchEvent(new Event("not sure"));
            }
        } catch (NullPointerException e){
            exceptionControllerSingleton.catchException(NearAreaService.class, e, "bad math");
        } catch (Exception e){
            exceptionControllerSingleton.catchException(NearAreaService.class, e, "bad math");
        }
    }

//    public static void main(String[] args){
////        -18.9118766,-48.3191431/-18.911916,-48.3192322
//        GeoPoint user = new GeoPoint(-18.9118766,-48.3191431);
//        GeoPoint pointA = new GeoPoint(-18.911916,-48.3192322);
//        GeoPoint pointB = new GeoPoint(-18.911927,-48.31929);
//        double radius = 11;
//        double toMeters = 6371000;
//        double degresToradians = Math.PI/180;
////
////        double aLong1 = user.getLongitude() * degresToradians;
////        double aLat1 = user.getLatitude() * degresToradians;
////        double aLong2 = pointA.getLongitude() * degresToradians;
////        double aLat2 = pointA.getLatitude() * degresToradians;
////
////        double cos_angle = Math.sin(aLat1) * Math.sin(aLat2) + Math.cos(aLat1) * Math.cos(aLat2) * Math.cos(aLong2 - aLong1);
//////      var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
////
////    /*
////    Inaccurate trig functions can cause cos_angle to be a tiny amount
////    greater than 1 if the two positions are very close. That in turn causes
////    acos to gives a domain error and return the special floating point value
////    -1.#IND000000000000, meaning 'indefinite'. Observed on VS2008 on 64-bit Windows.
////    */
////        if (cos_angle >= 1)
////            return 0;
////
////        double angle = Math.acos(cos_angle);
////        return angle * degresToradians;
////        var myposition = _options.myposition, radius = _options.radius, center = _options.center;
//        double dLat = (user.getLatitude() - pointA.getLatitude()) * degresToradians;
//        double dLon = (user.getLongitude() - pointA.getLongitude()) * degresToradians;
//        double lat1 = pointA.getLatitude() * degresToradians;
//        double lat2 = user.getLatitude() * degresToradians;
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double d = toMeters * c;
//        System.out.print( (radius >= d)  ? "it belongs!" : "Crap!");
//    }

    public boolean isNearPoint(GeoPoint user, GeoPoint busStop, String type){
//        double dLat = (user.getLatitude() - busStop.getLatitude()) * DEGREES_TO_RADIANS;
//        double dLon = (user.getLongitude() - busStop.getLongitude()) * DEGREES_TO_RADIANS;
//        double lat1 = busStop.getLatitude() * DEGREES_TO_RADIANS;
//        double lat2 = user.getLatitude() * DEGREES_TO_RADIANS;
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double d = TO_METERS * c;
        return (this.getRadiusInMeters(type) >= this.getDistanceBetween(user, busStop));
    }

    public boolean isNearPoint(Location user, GeoPoint busStop, String type){
        double radius = this.getRadiusInMeters(type);
        double distance = this.getDistanceBetween(user, busStop);
//        return (this.getRadiusInMeters(type) >= this.getDistanceBetween(user, busStop));
        Log.i("MyLocationProvider", "radius: " + Double.toString(radius) + "\tdistance:" + Double.toString(distance));
        return radius >= distance;
    }

    public GeoPoint resetGeoPoint(GeoPoint target, Location source){
        target.setLatitudeE6((int) (source.getLatitude() * 1000000.0D));
        target.setLongitudeE6((int) (source.getLongitude() * 1000000.0D));
        return target;
    }

    private double getRadiusInMeters(String type){
        if(type.toLowerCase().equals("tiny"))
            return TINY_RADIUS_IN_METERS;
        if(type.toLowerCase().equals("little"))
            return LITTLE_RADIUS_IN_METERS;
        if(type.toLowerCase().equals("small"))
            return SMALL_RADIUS_IN_METERS;
        if(type.toLowerCase().equals("big"))
            return BIG_RADIUS_IN_METERS;
        if(type.toLowerCase().equals("large"))
            return LARGE_RADIUS_IN_METERS;
        if(type.toLowerCase().equals("gigantic"))
            return GIGANTIC_RADIUS_IN_METERS;
        return TINY_RADIUS_IN_METERS;
    }

    private double getDistanceHelper(double latu, double lonu, double latp, double lonp){
        double dLat = (latu - latp) * DEGREES_TO_RADIANS;
        double dLon = (lonu - lonp) * DEGREES_TO_RADIANS;
        double latu1 = latu * DEGREES_TO_RADIANS;
        double latp2 = latp * DEGREES_TO_RADIANS;
        double halfDLat = dLat / 2;
        double halfDLon = dLon / 2;

        double a = Math.sin(halfDLat) * Math.sin(halfDLat) + Math.sin(halfDLon) * Math.sin(halfDLon) * Math.cos(latu1) * Math.cos(latp2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return TO_METERS * c;
    }

    public double getdistance(Location u, GeoPoint p){
        return this.getDistanceBetween(u,p);
    }

    public boolean verifyBusStop(double distance){
        return (this.LITTLE_RADIUS_IN_METERS >= distance);
    }
    public boolean verify(double distance){
        return (this.TINY_RADIUS_IN_METERS >= distance);
    }

}
