package aloeio.buzapp_stop.app.Models.Bus;

import aloeio.buzapp_stop.app.Services.Overrides.MyMarker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * Created by pablohenrique on 4/16/15.
 */
public class BusRoute {

    private String id;
    private String name;
    private String description;
    private BusStop busStop = null;
    private ArrayList<BusStop> busStops;
    private ArrayList<String> busOnRoute;
    private ArrayList<GeoPoint> busStopsGeoPoints;

    /**
     * Class constructos
     */

    public BusRoute(JSONObject json, MyMarker marker) throws JSONException, CloneNotSupportedException{
        setObjectData(json, marker);
    }

    /**
     * Class modifiers
     */



    public void setObjectData(JSONObject json, MyMarker marker) throws JSONException, CloneNotSupportedException{
        busStops = new ArrayList<BusStop>();
        busOnRoute = new ArrayList<String>();
        setBusStopsGeoPoints(new ArrayList<GeoPoint>());

        setBusStop(new BusStop());
        BusStop clone;

        setId(json.getString("id"));
        setName(json.getString("name"));
        setDescription(json.getString("description"));

        JSONArray busOnRoute = json.getJSONArray("listBus");
        JSONArray busStops = json.getJSONObject("route").getJSONArray("stops");

        // create the ArrayList<BusStop>
        for(int i = 0; i < busStops.length(); i++) {
            clone = getBusStop().copy();
            clone.setObjectData(busStops.getJSONObject(i), marker.copy());
            getBusStops().add( clone );
            //TODO change first and last bustops if they are TERMINAL!!! FUCK!!!!
//            if(i == 0 || i == busStops.length() - 1)
//                getBusStopsGeoPoints().add(clone.getPosition());
            getBusStopsGeoPoints().add(clone.getPosition());
        }

        for(int i = 0; i < busOnRoute.length(); i++) {
            getBusOnRoute().add( busOnRoute.getJSONObject(i).getString("id") );
        }
    }

    public void destroyRoute(){
        getBusOnRoute().clear();
        getBusStops().clear();
        getBusStopsGeoPoints().clear();
//        setBusOnRoute(new ArrayList<String>());
//        setBusStops(new ArrayList<BusStop>());
//        setBusStopsGeoPoints(new ArrayList<GeoPoint>());
    }

    public void removeBusStop(BusStop stop){
        getBusStops().remove(stop);
    }

    /**
     * Class getters and setters
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<BusStop> getBusStops() {
        return busStops;
    }

    public void setBusStops(ArrayList<BusStop> busStops) {
        this.busStops = busStops;
    }

    public ArrayList<String> getBusOnRoute() {
        return busOnRoute;
    }

    public void setBusOnRoute(ArrayList<String> busOnRoute) {
        this.busOnRoute = busOnRoute;
    }

    public BusStop getBusStop() {
        return busStop;
    }

    public void setBusStop(BusStop busStop) {
        this.busStop = busStop;
    }

    public ArrayList<GeoPoint> getBusStopsGeoPoints() {
        return busStopsGeoPoints;
    }

    public void setBusStopsGeoPoints(ArrayList<GeoPoint> busStopsGeoPoints) {
        this.busStopsGeoPoints = busStopsGeoPoints;
    }

    /**
     * Class helpers
     */
}
