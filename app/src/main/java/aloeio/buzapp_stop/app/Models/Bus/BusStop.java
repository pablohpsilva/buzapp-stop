package aloeio.buzapp_stop.app.Models.Bus;

import aloeio.buzapp_stop.app.Interfaces.IBackendJSON;
import aloeio.buzapp_stop.app.Services.Overrides.MyMarker;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.Serializable;

/**
 * Created by pablohenrique on 4/16/15.
 */
public class BusStop implements Cloneable, Serializable, IBackendJSON{

    private String id;
    private String name;
    private String description;
    private String capacity;
    private GeoPoint position = new GeoPoint(0,0);
    private MyMarker marker = null;

    /**
     * HOW TO USE:
     * Configurations:
     *      Create a MyMarker in some class ( class X ).
     *      Create an instance of this object on class X.
     * Usage:
     *      Create a clone from this class and pass the arguments using setData(). MyMarker must be also a clone.
     *      Use as you please.
     */

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        if(isValid()) {
            JSONObject json = new JSONObject();
            JSONObject position = new JSONObject();

            position.accumulate("latitude", getPosition().getLatitude());
            position.accumulate("longitude", getPosition().getLongitude());

            json.accumulate("id", getId());
            json.accumulate("name", getName());
            json.accumulate("description", getDescription());
            json.accumulate("capacity", getCapacity());
            json.accumulate("position", position);
//            json.accumulate("marker", getMarker());
            return json;
        }
        return null;
    }

    /**
     * Class constructors
     */

    public BusStop() {}

    public BusStop(MyMarker marker) {
        this.marker = marker;
    }

    public BusStop(GeoPoint position, MyMarker marker) {
        setPosition(position);
        setMarker(marker);
    }

    public BusStop(String id, String name, String description, String capacity, GeoPoint position) {
        this(id, name, description, capacity, position, null);
    }

    public BusStop(String id, String name, String description, String capacity, GeoPoint position, MyMarker marker) {
        super();
        setObjectData(id, name, description, capacity, position, marker);
    }

    public BusStop(JSONObject json, MyMarker marker) throws JSONException{
        super();
        setObjectData(json, marker);
    }

    /**
     * Class modifiers
     */

    public void setObjectData(String id, String name, String description, String capacity, GeoPoint position){
        setObjectData(id, name, description, capacity, position, null);
    }

    public void setObjectData(JSONObject json, MyMarker marker) throws JSONException{
        setObjectData(
                json.getString("id"),
                json.getString("name"),
                json.getString("description"),
                json.getString("capacity"),
                extractGeoPoint(json),
                marker
        );
    }

    public void setObjectData(String id, String name, String description, String capacity, GeoPoint position, MyMarker marker){
        setId(id);
        setName(name);
        setDescription(description);
        setCapacity(capacity);
        setPosition(position);
        setMarker(marker);
    }

    /**
     * Class getters and setters
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id != null ? id : "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity != null ? capacity : "";
    }

    public GeoPoint getPosition() {
        return position;
    }

    public void setPosition(GeoPoint position) {
        this.position = position;
    }

    public void setMarker(MyMarker marker){
        if(marker != null) {
            this.marker = marker;
            if (this.position != null)
                this.marker.setPosition(this.position);
            this.marker.setTitle(this.getName());
//            this.marker.setSubDescription(this.getId());
            this.marker.setSnippet(this.getDescription());
        } else
            this.marker = null;
    }

    public MyMarker getMarker(){
        return this.marker;
    }

    /**
     * Class helpers
     */

    public BusStop copy() throws CloneNotSupportedException{
        return (BusStop) this.clone();
    }

    public boolean isValid(){
        return (!getName().equals("") && !getId().equals("") && !getDescription().equals("") && !getCapacity().equals("") && getPosition() != null /*&& getMarker() != null*/ );

    }

    private GeoPoint extractGeoPoint(JSONObject json) throws JSONException{
        JSONObject position = json.getJSONObject("position");
//        getPosition().setLatitudeE6( (int) (position.getDouble("latitude") * 1000000.0D) );
//        getPosition().setLongitudeE6( (int) (position.getDouble("longitude") * 1000000.0D) );
        this.setPosition(new GeoPoint(position.getDouble("latitude"), position.getDouble("longitude")));
        return getPosition();
    }

}
