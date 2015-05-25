package aloeio.buzapp_stop.app.Services;

import aloeio.buzapp_stop.app.Models.Bus.BusCar;
import aloeio.buzapp_stop.app.Utils.HttpUtils;
import org.apache.http.HttpException;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pablohenrique on 4/16/15.
 */
public class BusService implements Cloneable{
    private HttpUtils http = null;
    private Timer busTrackingTimer = null;
    private Timer busEasingTimer = null;
    private String currentURL = "";
    private double timeElapsed = 0;
    private BusCar busCar = null;
    private GeoPoint easingGeoPointAux = new GeoPoint(0,0);
    private ExceptionService exceptionControllerSingleton = ExceptionService.getInstance();
    private RoadManager roadManager;
    private Road roadBetweenTargets;
    private GeoPoint userGeoPoint;
    ArrayList<GeoPoint> targetsArrayList;

//    final private int TIME_ELAPSED = 100;
    final private int TRACK_UPDATE = 3000;
    final private int TOTAL_TIME = 2500;
    final private int TIME_ELAPSED = 100;
    final private String MAPQUEST_API_KEY = "Fmjtd%7Cluu82quznu%2C2w%3Do5-94tgg4";

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Class constructos
     */

    public BusService(){}

    /**
     * The BusCar is an instance a.k.a. new BusCar()
     *              WITH
     * The marker setted
     */
    public BusService(BusCar car, String URL){
        setObjectData(car, URL);
    }

    /**
     * Class public methods
     */

    public void setObjectData(BusCar car, String URL){
        this.setObjectData(car, URL, null);
    }

    public void setObjectData(BusCar car, String URL, GeoPoint targetGeoPoint){
        setBusCar(car);
        setCurrentURL(URL);
        setTargetGeoPoint(targetGeoPoint);
        this.roadManager = new OSRMRoadManager();
//        this.roadManager = new MapQuestRoadManager(MAPQUEST_API_KEY);
//        this.roadManager.addRequestOption("routeType=multimodal");
    }

    public void resumeBusTracking() {
//        if(isValid())
//            startBusTracking();
    }

    public void stopBusActivity(){
        stopEasingMovement();
        stopBusTracking();
    }

    public void startBusTracking(final MapView mapView){
        if(isValid()) {
            busTrackingTimer = new Timer();
            http = new HttpUtils();
            busTrackingTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        //For easing movement only
//                        if(busCar.getPosition().getLongitude() == 0)
//                            busCar.changeMarkerPosition(new JSONObject(http.getRequest(currentURL)));
//                        else
//                            easeBusMovement(busCar.getPosition(), new JSONObject(http.getRequest(currentURL)), mapView);
                        busCar.changeMarkerPosition(new JSONObject(http.getRequest(currentURL)));
                        mapView.postInvalidate();

                        if(targetsArrayList != null)
                            determineDistanceBetweenTargets();

                        System.gc();

                    } catch (JSONException e) {
                        exceptionControllerSingleton.catchException(BusService.class, e, "bad backend");
                    }
                    catch (HttpException e){
                        exceptionControllerSingleton.catchException(BusService.class, e, "bad backend");
                    }
                    catch (URISyntaxException e){
                        exceptionControllerSingleton.catchException(BusService.class, e, "bad backend");
                    }
                    catch (IOException e){
                        exceptionControllerSingleton.catchException(BusService.class, e, "bad backend");
                    }
                    catch (Exception e) {
                        exceptionControllerSingleton.catchException(BusService.class, e);
                    }
                }
            }, 0, TRACK_UPDATE);
        }
    }

    public void easeBusMovement(final GeoPoint current, final JSONObject next, final MapView mapView){
        busEasingTimer = new Timer();
        busEasingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (timeElapsed >= TOTAL_TIME) {
                        this.cancel();
                        stopEasingMovement();
                        return;
                    }

                    busCar.changeMarkerPosition(next);

                    easingGeoPointAux.setLatitudeE6((int) ((busCar.getPosition().getLatitude() - current.getLatitude()) * 1000000.0D));
                    easingGeoPointAux.setLongitudeE6((int) ((busCar.getPosition().getLongitude() - current.getLongitude()) * 1000000.0D));

                    busCar.setPosition(getEasedGeoPointMovement(current, easingGeoPointAux));
                    mapView.postInvalidate();

                    timeElapsed += TIME_ELAPSED;
                } catch (JSONException e) {
                    exceptionControllerSingleton.catchException(BusService.class, e, "bad backend");
                } catch (NullPointerException e){
                    exceptionControllerSingleton.catchException(BusService.class, e, "bad backend");
                } catch (Exception e) {
                    exceptionControllerSingleton.catchException(BusService.class, e);
                }
            }
        }, 0, TIME_ELAPSED);
    }

    /**
     * Class private methods
     */

    private void stopBusTracking(){
        if(busTrackingTimer != null)
            busTrackingTimer.cancel();
        busTrackingTimer = null;
    }

    private void stopEasingMovement(){
        if(busEasingTimer != null)
            busEasingTimer.cancel();
        busEasingTimer = null;
        timeElapsed = 0;
    }

    /**
     * Class getters and setters
     */

    public void setCurrentURL(String URL){
        currentURL = URL;
    }

    public String getCurrentURL(){
        return this.currentURL;
    }

    public void setBusCar(BusCar car){
        this.busCar = car;
    }

    public BusCar getBusCar(){
        return this.busCar;
    }

    public void setTargetGeoPoint(GeoPoint userGeoPoint){
        if(userGeoPoint != null) {
            this.targetsArrayList = new ArrayList<GeoPoint>();
            this.userGeoPoint = userGeoPoint;
            this.targetsArrayList.add(this.userGeoPoint);
        }
    }

    /**
     * Class helpers
     */


    public GeoPoint getBusCurrentLocation(){
        return busCar.getMarker().getPosition();
    }

    public BusService copy() throws CloneNotSupportedException{
        return (BusService) this.clone();
    }

    private void determineDistanceBetweenTargets(){
        new Runnable(){
            @Override
            public void run(){
                if(busCar.getPosition() != null) {
                    try {
                        if (targetsArrayList.size() == 2)
                            targetsArrayList.remove(targetsArrayList.size()-1);
                        targetsArrayList.add(busCar.getPosition());
                        roadBetweenTargets = roadManager.getRoad(targetsArrayList);
                        BusManagerService.getSmallestDuration(busCar.getRoute(), roadBetweenTargets.mDuration);
                        this.finalize();
                    } catch (Throwable throwable) {
                        System.out.println("THREAD DID NOT DIE! " + throwable.getMessage());
                    }
//                    catch (Exception e){
//                        System.out.println("THREAD DID NOT DIE! " + e.getMessage());
//                    }
                }
            }
        }.run();
    }

    private boolean isValid(){
        return (!getCurrentURL().equals("") && getBusCar() != null);
    }

    private GeoPoint getEasedGeoPointMovement(GeoPoint actual, GeoPoint goal){
        double lat = easeOutQuad(timeElapsed, actual.getLatitude(), goal.getLatitude(), TOTAL_TIME);
        double lon = easeOutQuad(timeElapsed, actual.getLongitude(), goal.getLongitude(), TOTAL_TIME);
        actual = new GeoPoint(lat,lon);
        return actual;
    }

    private double easeOutQuad(double timeElapsed, double actual, double goal, double totalTime){
        double aux = timeElapsed / totalTime;
        return -goal * aux*(aux-2) + actual;
    }
}
