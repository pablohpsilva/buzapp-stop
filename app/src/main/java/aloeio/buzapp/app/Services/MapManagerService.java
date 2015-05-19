package aloeio.buzapp.app.Services;

import aloeio.buzapp.app.Fragments.MapFragment;
import aloeio.buzapp.app.Models.Bus.BusCar;
import aloeio.buzapp.app.Models.Bus.BusRoute;
import aloeio.buzapp.app.Models.Bus.BusStop;
import aloeio.buzapp.app.Services.Overrides.MyLocationProvider;
import aloeio.buzapp.app.Services.Overrides.MyMarker;
import aloeio.buzapp.app.Services.Overrides.MyMarkerInfoWindow;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import aloeio.buzapp.app.R;
import android.support.v4.app.Fragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

/**
 * Created by pablohenrique on 4/16/15.
 */
public class MapManagerService {
    private final MapFragment fragment;
    private BusCar busCar;
    private BusManagerService busManagerService;
    private BusRoute busRoute;
    private MyMarkerInfoWindow infoWindow;
    private MyMarker busMarker, busStopMarker, stepMarker;
    private MyLocationProvider buzappMyLocationProvider;
    private ExceptionService exceptionControllerSingleton = ExceptionService.getInstance();
    private MapView mapView;
    private MyLocationService myLocationService;
    private NewBusService newBusService;
    private Polyline busRoadOverlay = null;
    private Polyline userRoadOverlay = null;
    private Road userRoadObject = null;
    private ArrayList<MyMarker> MyMarkerArrayList;

    final private int CAMERA_ZOOM = 16;
//    final private int CAMERA_ZOOM = 15;
    final private int LINE_WIDTH = 5;
    final private int LINE_DEFAULT_COLOR = Color.parseColor("#4A34B5");
    final private int LINE_USER_COLOR = Color.parseColor("#6495ED");
    final private String MAPQUEST_API_KEY = "Fmjtd%7Cluu82quznu%2C2w%3Do5-94tgg4";

    public MapManagerService(final MapFragment fragment){
        this.fragment = fragment;
        this.mapView = (MapView) fragment.getActivity().findViewById(R.id.home_mapview);
        this.setOSMDefaults();
    }

    public MyLocationNewOverlay getUserLocationOverlay(){
        return this.myLocationService.getMyLocationOverlay();
    }

    public void drawUserLocation(){
        if(buzappMyLocationProvider == null)
            buzappMyLocationProvider = new MyLocationProvider(fragment);

        if(this.myLocationService == null)
            this.myLocationService = new MyLocationService(fragment,buzappMyLocationProvider);
        else
            this.myLocationService.centerMyLocation();
    }

    public void drawBusStops(JSONObject json){
        try {
            createStopsMarker();
            createBubbleInfo();
            createArrayListSteps();

            this.removeUserRoad();

            if(this.busRoute == null) {
                this.busRoute = new BusRoute(json, this.busStopMarker);
                for (BusStop stop : this.busRoute.getBusStops())
                    mapView.getOverlays().add(stop.getMarker());
            } else {
                removeRouteObjects();
                System.gc();
//                this.busStopMarker = null;
//                createStopsMarker();
                this.busRoute.setObjectData(json, this.busStopMarker);
                for (BusStop stop : this.busRoute.getBusStops())
                    mapView.getOverlays().add(stop.getMarker());
            }
            drawPathNearestBusStop();

            mapView.postInvalidate();
        } catch (JSONException e) {
            exceptionControllerSingleton.catchException(MapManagerService.class, e, "bad backend");
        } catch (NullPointerException e){
            exceptionControllerSingleton.catchException(MapManagerService.class, e, "bad backend");
        } catch (Exception e) {
            exceptionControllerSingleton.catchException(MapManagerService.class, e);
        }
    }

    public void drawPathNearestBusStop(){
        try {
            if(this.userRoadObject != null)
                this.removeUserRoad();

//            ArrayList<GeoPoint> points = this.busRoute.getBusStopsGeoPoints();
//            GeoPoint nearest = new NearAreaService().nearestGeoPoint(this.myLocationService.getLastKnownLocation(), points);
//
//            points.clear();
//            points.add(this.myLocationService.getLastKnownLocation());
//            points.add(nearest);

            new Runnable(){
                public void run() {
                    ArrayList<GeoPoint> points = busRoute.getBusStopsGeoPoints();
                    GeoPoint nearest = new NearAreaService().nearestGeoPoint(myLocationService.getLastKnownLocation(), points);

                    points.clear();
                    points.add(myLocationService.getLastKnownLocation());
                    points.add(nearest);

                    RoadManager roadManager = new MapQuestRoadManager(MAPQUEST_API_KEY);
                    roadManager.addRequestOption("routeType=pedestrian");

//            RoadManager roadManager = new OSRMRoadManager();

                    userRoadObject = roadManager.getRoad(points);
                    busRoadOverlay = RoadManager.buildRoadOverlay(userRoadObject, mapView.getContext());

                    busRoadOverlay.setColor(LINE_USER_COLOR);
                    busRoadOverlay.setWidth(LINE_WIDTH);

                    startNavigation(userRoadObject);

                    mapView.getOverlays().add(busRoadOverlay);
                    mapView.postInvalidate();
                }
            }.run();

//            RoadManager roadManager = new MapQuestRoadManager(this.MAPQUEST_API_KEY);
//            roadManager.addRequestOption("routeType=pedestrian");
//
////            RoadManager roadManager = new OSRMRoadManager();
//
//            this.userRoadObject = roadManager.getRoad(points);
//            busRoadOverlay = RoadManager.buildRoadOverlay(this.userRoadObject, mapView.getContext());
//
//            busRoadOverlay.setColor(LINE_USER_COLOR);
//            busRoadOverlay.setWidth(LINE_WIDTH);
//
//            this.startNavigation(this.userRoadObject);
//
//            mapView.getOverlays().add(busRoadOverlay);
//            mapView.postInvalidate();
        } catch (NullPointerException e) {
            exceptionControllerSingleton.catchException(MapManagerService.class, e);
        } catch (Exception e) {
            exceptionControllerSingleton.catchException(MapManagerService.class, e);
        }
    }

    public void drawBus(String url){
        try {
            createBubbleInfo();
            createBusManagerService();

            this.busManagerService.startBus(url);

        } catch (Exception e) {
            this.exceptionControllerSingleton.catchException(MapManagerService.this.getClass(), e);
        }
    }

    public void drawBuses(JSONArray json){
        try {
            createBubbleInfo();
            createBusManagerService();
            if(this.busManagerService.inUse())
                this.busManagerService.stopRemoveBuses();

            this.busManagerService.startBus(json);

        } catch (Exception e) {
            this.exceptionControllerSingleton.catchException(MapManagerService.this.getClass(), e);
        }
    }

    public void startNavigation(Road road){
        try {
            createStepMarker();
            removeMarkerSteps();
//            createBubbleInfo();
//            MarkerInfoWindow infoWindow = new MarkerInfoWindow(R.layout.template_bonuspack_bubble, mapView);
            for (int i = 0; i < road.mNodes.size(); i++) {
                MyMarker clone = this.stepMarker.copy();
                RoadNode node = road.mNodes.get(i);
                clone.setPosition(node.mLocation);
                clone.setTitle("Step " + i);
                clone.setSnippet(node.mInstructions);
                clone.setSubDescription(Road.getLengthDurationText(node.mLength, node.mDuration));
//                clone.setInfoWindow(this.infoWindow);
                this.MyMarkerArrayList.add(clone);
                this.mapView.getOverlays().add(clone);
            }
            this.myLocationService.getMyLocationProvider().setRoad(road);
        } catch(CloneNotSupportedException e){
            exceptionControllerSingleton.catchException(MapManagerService.class, e);
        }
    }

    public void stopBuses(){
        if(this.busManagerService != null)
            this.busManagerService.stopServices();
    }

    public void resumeBuses(){
        if(this.busManagerService != null)
            this.busManagerService.resumeServices();
    }

    public void removeBusRoad(){
        this.removeRoad("bus");
    }

    public void removeUserRoad(){
        this.removeRoad("user");
    }

    public void stopServices(){
        this.myLocationService.getMyLocationProvider().stopLocationProvider();
        this.stopBuses();
    }

    public void resumeServices(){
        this.myLocationService.getMyLocationProvider().startLocationProvider();
        this.resumeBuses();
    }

    /**
     * Private methods that handles the OSM
     */

    private void setOSMDefaults(){
        setOSMDefaults(new GeoPoint(-18.9106433, -48.3239163));
    }

    private void setOSMDefaults(GeoPoint startingPoint){
        mapView.setBuiltInZoomControls(false);
        mapView.setKeepScreenOn(true);
        mapView.setSaveEnabled(true);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(false);
        mapView.setTileSource(new XYTileSource("MapQuest",
                ResourceProxy.string.mapquest_osm, 0, 17, 256, ".jpg", new String[]{
                "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));

        mapView.getController().setZoom(CAMERA_ZOOM);
        setOSMCenter(startingPoint);
        mapView.postInvalidate();
    }

    private void setOSMCenter(GeoPoint startingPoint){
        mapView.getController().setCenter(startingPoint);
    }

    private void removeBuses(){
        if(this.busManagerService != null)
            this.busManagerService.stopRemoveBuses();
    }

    private void removeMarker(BusStop stop){
        if (stop.getMarker() != null)
            removeMarker(stop.getMarker());
    }

    private void removeMarker(BusCar car){
        if(car.getMarker() != null)
            removeMarker(car.getMarker());
    }

    private void removeRoad(String polyline){
        if(this.userRoadObject != null) {
            if (!this.userRoadObject.mNodes.isEmpty())
                for (RoadNode node : this.userRoadObject.mNodes)
                    this.mapView.getOverlays().remove(node);
            this.mapView.getOverlays().remove(this.userRoadObject);
        }
        if(polyline.equals("user"))
            if(this.userRoadOverlay != null) {
                this.mapView.getOverlays().remove(this.userRoadOverlay);
                this.userRoadOverlay = null;
            }
        else
            if(this.busRoadOverlay != null){
                this.mapView.getOverlays().remove(this.busRoadOverlay);
                this.busRoadOverlay = null;
            }
    }

    private void removeRouteObjects(){
        if(this.busRoute.getBusStops().size() > 0) {
            for (BusStop stop : this.busRoute.getBusStops())
                mapView.getOverlays().remove(stop.getMarker());
            this.busRoute.destroyRoute();
        }
    }

    private void removeMarker(MyMarker marker){
        this.mapView.getOverlays().remove(marker);
    }

    private void removeMarkerSteps(){
        if(this.MyMarkerArrayList != null) {
            if (this.MyMarkerArrayList.size() > 0)
                for (MyMarker marker : this.MyMarkerArrayList)
                    this.mapView.getOverlays().remove(marker);
            this.MyMarkerArrayList.clear();
        }
    }

    private void removeAllMarkers(boolean keepUser){
        Overlay userLocation = mapView.getOverlays().get(0);
        mapView.getOverlays().clear();
        if(keepUser)
            mapView.getOverlays().add(userLocation);
    }

    private void removeObject(Overlay obj){
        this.mapView.getOverlays().remove(obj);
    }

    private void removeObjects(ArrayList<Overlay> objs ){
        this.mapView.getOverlays().remove(objs);
    }

    /**
     * Private methods that handles Buzapp objects
     */

    private void createBusMarkerAndCar(){
        if(this.busCar == null)
            this.busCar = new BusCar();
        if(this.busMarker == null)
            createBusMarker();
        if(this.newBusService == null)
            newBusService = new NewBusService();
    }

    private void createStopsMarker(){
        if(this.busStopMarker == null)
            createStopMarker();
    }

    private void createBusMarker(){
        busMarker = new MyMarker(this.mapView);
        createMarkersDefault(busMarker, fragment.getResources().getDrawable(R.mipmap.ic_bus));
        System.gc();
    }

    private void createStopMarker(){
        busStopMarker = new MyMarker(this.mapView);
        createMarkersDefault(busStopMarker, fragment.getResources().getDrawable(R.mipmap.ic_stop_sign));
        System.gc();
    }

    private void createStepMarker(){
        stepMarker = new MyMarker(this.mapView);
        createMarkersDefault(stepMarker, fragment.getResources().getDrawable(R.mipmap.ic_step));
        System.gc();
    }

    private void createMarkersDefault(MyMarker marker, Drawable icon){
        createBubbleInfo();
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setInfoWindow(this.infoWindow);
        marker.setIcon(icon);
    }

    private void createBubbleInfo(){
        if(this.infoWindow == null)
            this.infoWindow = new MyMarkerInfoWindow(mapView);
    }

    private void createBusManagerService(){
        if(this.busManagerService == null)
            this.busManagerService = new BusManagerService(this.fragment, this.infoWindow);
    }

    private void createArrayListSteps(){
        if(this.MyMarkerArrayList == null)
            this.MyMarkerArrayList = new ArrayList<MyMarker>();
    }
}
