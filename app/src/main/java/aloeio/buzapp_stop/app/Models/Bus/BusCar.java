package aloeio.buzapp_stop.app.Models.Bus;

import aloeio.buzapp_stop.app.Services.Overrides.MyMarker;
import aloeio.buzapp_stop.app.Utils.Constants.UrlConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

/**
 * Created by pablohenrique on 4/16/15.
 */
public class BusCar implements Cloneable{

    private String id = "";
    private String route = "";
    private String capacity = "";
    private String url = UrlConstants.BUS_MODEL;
    private String title = "";
    private String snippet = "";
    private Double velocity = -100.0;
    private boolean accessibility = false;
    private GeoPoint position = new GeoPoint(0,0);
//    private BusStop lastStop;
    private MyMarker marker = null;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Class constructors
     */

    public BusCar(){ }

    public BusCar(MyMarker marker){
        this.marker = marker;
    }

    public BusCar(GeoPoint position, MyMarker marker){
        setPosition(position);
        setMarker(marker);
    }

    public BusCar(JSONObject json, MyMarker marker) throws JSONException{
        setObjectData(json, marker);
    }

    public BusCar(String id, String route, String capacity, Boolean accessibility, Double velocity, GeoPoint position){
        this(id, route, capacity, accessibility, velocity, position, null);
    }

    public BusCar(String id, String route, String capacity, Boolean accessibility, Double velocity, GeoPoint position, MyMarker marker){
        setObjectData(id, route, capacity, accessibility, velocity, position, marker);
    }

    /**
     * Class modifiers
     */

    public void setObjectData(JSONObject json, MyMarker marker) throws JSONException{
        setObjectData(
                json.getString("id"),
                json.getString("busRoute"),
                json.getString("capacity"),
                json.getBoolean("accessibility"),
                json.getDouble("velocity"),
                extractGeoPoint(json),
                marker
        );
    }

    public void setObjectData(String id, String route, String capacity, Boolean accessibility, Double velocity, GeoPoint position){
        setObjectData(id, route, capacity, accessibility, velocity, position, null);
    }

    public void setObjectData(String id, String route, String capacity, Boolean accessibility, Double velocity, GeoPoint position, MyMarker marker){
        setId(id);
        setRoute(route);
        setCapacity(capacity);
        setAccessibility(accessibility);
        setVelocity(velocity);
        setPosition(position);
        setMarker(marker);
        setUrl(getUrl() + getRoute() + "/" + getId());
        createTitle();
        createSnippet();
    }

    public void changeMarkerPosition(JSONObject json) throws JSONException{
        if(!isValid())
            setObjectData(json, null);
        else if (marker != null)
            changeMarkerPosition(extractGeoPoint(json));
    }

    public void changeMarkerPosition(GeoPoint newPosition){
        if(marker != null && newPosition != null)
            this.setPosition(newPosition);
    }

    /**
     * Class getters ans setters
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public Double getVelocity() {
        return velocity;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    public boolean getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(boolean accessibility) {
        this.accessibility = accessibility;
    }

    public GeoPoint getPosition() {
        return position;
    }

    public void setPosition(GeoPoint position) {
        this.position = position;
        if(marker != null)
            marker.setPosition(position);
    }

    public MyMarker getMarker() {
        return marker;
    }

    public void setMarker(MyMarker marker) {
        if (marker != null) {
            this.marker = marker;
            this.marker.setTitle(this.getTitle());
            this.marker.setSnippet(this.getSnippet());
        }

        if (getPosition() != null)
            this.marker.setPosition(getPosition());
    }

//    public BusStop getLastStop() {
//        return lastStop;
//    }
//
//    public void setLastStop(BusStop lastStop) {
//        this.lastStop = lastStop;
//    }

    /**
     * Class helpers
     */

    private GeoPoint extractGeoPoint(JSONObject json) throws JSONException{
        JSONObject position = json.getJSONObject("positon");
        getPosition().setLatitudeE6((int) (position.getDouble("latitude") * 1000000.0D));
        getPosition().setLongitudeE6((int) (position.getDouble("longitude") * 1000000.0D));
        return getPosition();
    }

    private Boolean isValid(){
        return (!getId().equals("") && !getRoute().equals("") && !getCapacity().equals("") && getVelocity() != -100.0 && getPosition() != null);
    }

    public BusCar copy() throws CloneNotSupportedException {
        return (BusCar) this.clone();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSnippet() {
        return snippet;
    }

    public void createSnippet() {
        String aux = "Acessibilidade: " + ((this.getAccessibility()) ? "Sim" : "Não") + "\n";
        aux += "Posição: " + this.getPosition().getLatitude() + "," + this.getPosition().getLongitude();
        this.snippet = aux;
    }

    public String getTitle() {
        return title;
    }

    public void createTitle() {
        this.title = "Ônibus: " + getRoute() + "/" + getId();
    }
}
