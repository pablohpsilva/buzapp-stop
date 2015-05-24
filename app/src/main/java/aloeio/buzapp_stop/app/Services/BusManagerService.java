package aloeio.buzapp_stop.app.Services;

import aloeio.buzapp_stop.app.Fragments.MapFragment;
import aloeio.buzapp_stop.app.MainActivity;
import aloeio.buzapp_stop.app.Models.Bus.BusCar;
import aloeio.buzapp_stop.app.Services.Overrides.MyMarker;
import aloeio.buzapp_stop.app.Services.Overrides.MyMarkerInfoWindow;
import android.graphics.drawable.Drawable;
import aloeio.buzapp_stop.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

/**
 * Created by pablohenrique on 5/8/15.
 */
public class BusManagerService {
    private ArrayList<BusService> busServiceArrayList;
    private ExceptionService exceptionControllerSingleton = ExceptionService.getInstance();
    private MyMarker busMarker;
    private MapFragment activity;
    private MapView mapView;
    private BusCar busCar;
    private BusService busService;
    private GeoPoint userGeoPoint;

    /*
     * Manage the duration to a bus get to
     */
    private static int firstRouteDuration = 10000;
    private static int secondRouteDuration = 10000;

    /**
     *
     */

    public BusManagerService(final MapFragment fragment, MyMarkerInfoWindow infoWindow, GeoPoint userGeoPoint){
        this.activity = fragment;
        this.mapView = (MapView) fragment.getActivity().findViewById(R.id.home_mapview);

        createBusMarker(infoWindow);
        createDefaultBus();
        createDefaultBusService();
    }

    public void startBus(JSONArray json){
        try {
            if(this.busServiceArrayList == null)
                this.busServiceArrayList = new ArrayList<BusService>();
            for (int i = 0; i < json.length(); i++)
                this.drawBuses(json.getJSONObject(i));
        } catch (JSONException exception){
            exceptionControllerSingleton.catchException(MapManagerService.class, exception, "bad json");
        } catch (CloneNotSupportedException exception){
            exceptionControllerSingleton.catchException(MapManagerService.class, exception, "bad json");
        } catch (Exception exception){
            exceptionControllerSingleton.catchException(MapManagerService.class, exception, "bad");
        }
    }

    public void startBus(String url){
        if(this.busServiceArrayList == null)
            this.busServiceArrayList = new ArrayList<BusService>();
        drawSingleBus(url);
    }

    public void stopServices(){
        if(this.busServiceArrayList.size() > 0)
            for(BusService service : this.busServiceArrayList)
                service.stopBusActivity();
    }

    public void resumeServices(){
        if(this.busServiceArrayList.size() > 0)
            for(BusService service : this.busServiceArrayList)
                service.resumeBusTracking();
    }

    public void stopRemoveBuses(){
        for(BusService service : this.busServiceArrayList){
            this.mapView.getOverlays().remove(service.getBusCar().getMarker());
            service.stopBusActivity();
        }
        this.busServiceArrayList.clear();
        System.gc();
    }

    public void stopRemoveBus(BusService service){
        int index = this.busServiceArrayList.indexOf(service);
        this.mapView.getOverlays().remove(this.busServiceArrayList.get(index).getBusCar().getMarker());
        this.busServiceArrayList.remove(this.busServiceArrayList.get(index));
    }

    public boolean inUse(){
        if(this.busServiceArrayList == null)
            return false;
        else
            return (this.busServiceArrayList.size() > 0);
    }


    public static void getSmallestDuration(String route, double duration){
        if(route.contains("123")){
            if((int)duration < firstRouteDuration) {
                firstRouteDuration = (int)duration;
                MapManagerService.getSmallestDuration(0, firstRouteDuration);
                if(firstRouteDuration <= 2)
                    firstRouteDuration = 1000;
            }
        } else {
            if((int)duration < secondRouteDuration) {
                secondRouteDuration = (int)duration;
                MapManagerService.getSmallestDuration(1, secondRouteDuration);
                if(secondRouteDuration <= 2)
                    secondRouteDuration = 1000;
            }
        }
    }


    private void drawBuses(JSONObject json) throws JSONException, CloneNotSupportedException{
        MyMarker cloneMarker = this.busMarker.copy();
        BusCar cloneBus = this.busCar.copy();
        BusService cloneBusService = this.busService.copy();

        cloneBus.setObjectData(json, cloneMarker);
        cloneBusService.setObjectData(cloneBus, cloneBus.getUrl());

        mapView.getOverlays().add(cloneBus.getMarker());
        mapView.postInvalidate();

        cloneBusService.startBusTracking(mapView);
        this.busServiceArrayList.add(cloneBusService);
    }

    private void drawSingleBus(String url){
        try {
            MyMarker cloneMarker = this.busMarker.copy();
            BusCar cloneBus = this.busCar.copy();
            BusService cloneBusService = this.busService.copy();

            cloneBus.setMarker(cloneMarker);
            cloneBusService.setObjectData(cloneBus, url);

            mapView.getOverlays().add(cloneMarker);
            mapView.postInvalidate();

            cloneBusService.startBusTracking(mapView);

            this.busServiceArrayList.add(cloneBusService);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void createBusMarker(MyMarkerInfoWindow infoWindow){
        if(this.busMarker == null) {
            busMarker = new MyMarker(this.mapView);
            this.createMarkersDefault(busMarker, activity.getResources().getDrawable(R.mipmap.ic_bus), infoWindow);
            System.gc();
        }
    }

    private void createMarkersDefault(MyMarker marker, Drawable icon, MyMarkerInfoWindow infoWindow){
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setInfoWindow(infoWindow);
        marker.setIcon(icon);
    }

    private void createDefaultBus(){
        if(this.busCar == null)
            this.busCar = new BusCar();
    }

    private void createDefaultBusService(){
        if(this.busService == null)
            this.busService = new BusService();
    }
}
