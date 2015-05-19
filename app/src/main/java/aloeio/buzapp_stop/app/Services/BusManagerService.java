package aloeio.buzapp_stop.app.Services;

import aloeio.buzapp_stop.app.Fragments.MapFragment;
import aloeio.buzapp_stop.app.Models.Bus.BusCar;
import aloeio.buzapp_stop.app.Services.Overrides.MyMarker;
import aloeio.buzapp_stop.app.Services.Overrides.MyMarkerInfoWindow;
import android.support.v4.app.Fragment;
import android.graphics.drawable.Drawable;
import aloeio.buzapp_stop.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

/**
 * Created by pablohenrique on 5/8/15.
 */
public class BusManagerService {
    private ArrayList<NewBusService> newBusServiceArrayList;
    private ExceptionService exceptionControllerSingleton = ExceptionService.getInstance();
    private MyMarker busMarker;
    private MapFragment activity;
    private MapView mapView;
    private BusCar busCar;
    private NewBusService newBusService;

    public BusManagerService(final MapFragment fragment, MyMarkerInfoWindow infoWindow){
        this.activity = fragment;
        this.mapView = (MapView) fragment.getActivity().findViewById(R.id.home_mapview);
        createBusMarker(infoWindow);
        createDefaultBus();
        createDefaultBusService();
    }

    public void startBus(JSONArray json){
        try {
            if(this.newBusServiceArrayList == null)
                this.newBusServiceArrayList = new ArrayList<NewBusService>();
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
        if(this.newBusServiceArrayList == null)
            this.newBusServiceArrayList = new ArrayList<NewBusService>();
        drawSingleBus(url);
    }

    public void stopServices(){
        if(this.newBusServiceArrayList.size() > 0)
            for(NewBusService service : this.newBusServiceArrayList)
                service.stopBusActivity();
    }

    public void resumeServices(){
        if(this.newBusServiceArrayList.size() > 0)
            for(NewBusService service : this.newBusServiceArrayList)
                service.resumeBusTracking();
    }

    public void stopRemoveBuses(){
        for(NewBusService service : this.newBusServiceArrayList){
            this.mapView.getOverlays().remove(service.getBusCar().getMarker());
            service.stopBusActivity();
        }
        this.newBusServiceArrayList.clear();
        System.gc();
    }

    public void stopRemoveBus(NewBusService service){
        int index = this.newBusServiceArrayList.indexOf(service);
        this.mapView.getOverlays().remove(this.newBusServiceArrayList.get(index).getBusCar().getMarker());
        this.newBusServiceArrayList.remove(this.newBusServiceArrayList.get(index));
    }

    public boolean inUse(){
        if(this.newBusServiceArrayList == null)
            return false;
        else
            return (this.newBusServiceArrayList.size() > 0);
    }

    private void drawBuses(JSONObject json) throws JSONException, CloneNotSupportedException{
        MyMarker cloneMarker = this.busMarker.copy();
        BusCar cloneBus = this.busCar.copy();
        NewBusService cloneNewBusService = this.newBusService.copy();

        cloneBus.setObjectData(json, cloneMarker);
        cloneNewBusService.setObjectData(cloneBus, cloneBus.getUrl());

        mapView.getOverlays().add(cloneBus.getMarker());
        mapView.postInvalidate();

        cloneNewBusService.startBusTracking(mapView);
        this.newBusServiceArrayList.add(cloneNewBusService);
    }

    private void drawSingleBus(String url){
        try {
            MyMarker cloneMarker = this.busMarker.copy();
            BusCar cloneBus = this.busCar.copy();
            NewBusService cloneNewBusService = this.newBusService.copy();

            cloneBus.setMarker(cloneMarker);
            cloneNewBusService.setObjectData(cloneBus, url);

            mapView.getOverlays().add(cloneMarker);
            mapView.postInvalidate();

            cloneNewBusService.startBusTracking(mapView);

            this.newBusServiceArrayList.add(cloneNewBusService);
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
        if(this.newBusService == null)
            this.newBusService = new NewBusService();
    }
}
