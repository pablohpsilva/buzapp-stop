package aloeio.buzapp_stop.app.Services.Overrides;


import aloeio.buzapp_stop.app.Fragments.MapFragment;
import aloeio.buzapp_stop.app.Models.Bus.BusRoute;
import aloeio.buzapp_stop.app.Services.NearAreaService;
import aloeio.buzapp_stop.app.Services.SpeakingService;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.NetworkLocationIgnorer;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;

import java.util.ArrayList;

/**
 * Created by pablohenrique on 4/22/15.
 */
public class MyLocationProvider implements IMyLocationProvider, LocationListener {

    private final LocationManager mLocationManager;
    private final NetworkLocationIgnorer mIgnorer = new NetworkLocationIgnorer();
    private final Fragment activity;

    private Location mLocation;
    private IMyLocationConsumer mMyLocationConsumer;
    private NearAreaService nearAreaService = null;
    private Road road = null;
    private ArrayList<GeoPoint> road2;
    private BusRoute busRoute = null;
    private GeoPoint nearestLocation;
    private SpeakingService speakingService = null;


    /**
     * Params - User navigation though bus service
     */
    private int routeStepCounter = 0;
    private boolean alreadySpoken = false;

    /**
     * Params - User navigation to nearest busStop
     */
    private int pathStepCounter = 0;
    private String lastInstruction = "";

    public MyLocationProvider(Fragment activity){
        this.activity = activity;
        mLocationManager = (LocationManager) activity.getActivity().getSystemService(Context.LOCATION_SERVICE);
        Log.i(this.getClass().getName(), "criada");
    }

    @Override
    public boolean startLocationProvider(IMyLocationConsumer iMyLocationConsumer) {
        boolean result = false;
        mMyLocationConsumer = iMyLocationConsumer;

        for(final String provider : mLocationManager.getProviders(true))
            if(LocationManager.GPS_PROVIDER.equals(provider) || LocationManager.NETWORK_PROVIDER.equals(provider)){
                result = true;
                float mLocationUpdateMinDistance = 0.0f;
                long mLocationUpdateMinTime = 0;
                mLocationManager.requestLocationUpdates(provider, mLocationUpdateMinTime, mLocationUpdateMinDistance, this);
            }

        Log.i(this.getClass().getName(), "started");

        return result;
    }

    public boolean startLocationProvider(){
        if(this.mMyLocationConsumer != null)
            return this.startLocationProvider(this.mMyLocationConsumer);
        return false;
    }

    @Override
    public void stopLocationProvider() {
//        mMyLocationConsumer = null;
        mLocationManager.removeUpdates(this);
    }

    @Override
    public Location getLastKnownLocation() {
        return mLocation;
    }

    @Override
    public void onLocationChanged(final Location location) {
        if(mIgnorer.shouldIgnore(location.getProvider(), System.currentTimeMillis()))
            return;

        mLocation = location;

        this.walkOnPath();
//        this.walkOnPath2();

        if(mMyLocationConsumer != null)
            mMyLocationConsumer.onLocationChanged(mLocation, this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * Methods below are used to create the navigation.
     */

    public void setRoad(Road road){
        this.road = road;
        Log.i("MyLocationProvider", "Road added successfully");
    }

    public void setBusRoute(BusRoute busRoute){
        this.busRoute = busRoute;
        Log.i("MyLocationProvider", "BusRoute added successfully");
    }

    public void setRoad2(ArrayList<GeoPoint> points){
        this.road2 = points;
    }

    /*
        Navigate user from his path to the nearest busStop.
     */
    private void walkOnPath(){
        if(this.road != null){
            if(this.nearAreaService == null)
                this.nearAreaService = new NearAreaService();
            if(this.speakingService == null)
//                this.speakingService = new SpeakingService((MainActivity)this.activity);
                this.speakingService = new SpeakingService((MapFragment) this.activity);

//            RoadNode nearestStep = this.nearAreaService.nearestRoadNode(this.getLastKnownLocation(), this.road.mNodes);
//            int nearestRoadNode = this.road.mNodes.indexOf(nearestStep);
//            this.pathStepCounter = this.road.mNodes.size() - (this.road.mNodes.indexOf(nearestStep) + 1);

            String instruction = this.road.mNodes.get(this.pathStepCounter).mInstructions;

            if( (this.pathStepCounter + 1) > this.road.mNodes.size() && !this.checkLastInstructionSend(instruction)){
//                Toast.makeText(this.activity.getApplicationContext(), "You have reached your destination.", Toast.LENGTH_SHORT).show();
                this.speakingService.speakWords("Você está próximo do ponto de ônibus mais próximo. A navegação até o ponto mais próximo foi encerrada.");
                ((MapFragment) this.activity).mapManagerService.removeUserRoad();
                this.road = null;
                this.pathStepCounter = 0;
                this.lastInstruction = "";
            }
            else {
                Location myLocation = this.getLastKnownLocation();
                GeoPoint roadLocation = road.mNodes.get(this.pathStepCounter).mLocation;

                double distance = this.nearAreaService.getdistance(myLocation, roadLocation);
//                ((MainActivity) this.activity).changeDistance(distance);

                if (this.nearAreaService.verify(distance) && !this.checkLastInstructionSend(instruction)) {
                    this.speakingService.speakWords(instruction);
                    this.lastInstruction = instruction;
                    this.pathStepCounter++;
                }
            }
        }
    }

    private void walkOnPath2(){
        if(this.road2 != null){
            if(this.nearAreaService == null)
                this.nearAreaService = new NearAreaService();
            if(this.speakingService == null)
                this.speakingService = new SpeakingService((MapFragment)this.activity);

//            GeoPoint nearestStep = this.nearAreaService.nearestGeoPoint(this.getLastKnownLocation(), this.road2);
//            this.pathStepCounter = this.road2.size() - (this.road2.indexOf(nearestStep) + 1);

            String instruction = "This is the step number " + ( this.pathStepCounter + 1 ) + ". Keep on going.";

//            if(this.pathStepCounter == 0){
            if(( this.pathStepCounter + 1 ) >= this.road2.size() && !this.checkLastInstructionSend(instruction)){
                this.speakingService.speakWords("You've reached your final destination. Congratulations, Pablo.");
                this.road2 = null;
                this.pathStepCounter = 0;
                this.lastInstruction = "";
            }
            else {
                double distance = this.nearAreaService.getdistance(this.getLastKnownLocation(), road2.get(this.pathStepCounter));
//                ((MainActivity) this.activity).changeDistance(distance);
                if (this.nearAreaService.verify(distance) && !this.checkLastInstructionSend(instruction)) {
                    this.speakingService.speakWords("This is the step number " + ( this.pathStepCounter + 1 ) + ". Keep on going." );
                    this.lastInstruction = instruction;
                    this.pathStepCounter++;
                }
            }
        }
    }

    /*
        Navigate user inside the bus
     */
    private void walkOnRoad(){
        if(this.busRoute != null){
            if(this.nearAreaService == null)
                this.nearAreaService = new NearAreaService();
            if(this.speakingService == null)
                this.speakingService = new SpeakingService((MapFragment)this.activity);

            GeoPoint nearestBusStop = this.nearAreaService.nearestGeoPoint(this.getLastKnownLocation(), this.busRoute.getBusStopsGeoPoints());
            int nearestBusStopIndex = this.busRoute.getBusStopsGeoPoints().indexOf(nearestBusStop);
            this.routeStepCounter = this.busRoute.getBusStopsGeoPoints().size() - (nearestBusStopIndex + 1);
            //TODO tell the user where is the nearest bus stop
            //TODO tell the user which bus stop is he in
            //TODO tell the user the next bustop on the fly
            if(this.routeStepCounter == 0){
//                Toast.makeText(this.activity.getApplicationContext(), "You have reached the last bus stop", Toast.LENGTH_SHORT).show();
                this.speakingService.speakWords("Você chegou na última parada.");
                this.busRoute = null;
            } else {
                double distance = this.nearAreaService.getdistance(this.getLastKnownLocation(), nearestBusStop);
//                ((HomeActivity) this.activity).changeDistance(distance);
                if(this.nearAreaService.verifyBusStop(distance)) {
//                    Toast.makeText(this.activity.getApplicationContext(), "You are 50 meters away from the next stop.", Toast.LENGTH_SHORT).show();
                    if(!this.alreadySpoken){
                        this.speakingService.speakWords("Você chegou a cinquenta metros da parada " + this.busRoute.getBusStops().get(nearestBusStopIndex).getName());
                        this.alreadySpoken = true;
                    }
                }
                if (this.nearAreaService.verify(distance)) {
//                    Toast.makeText(this.activity.getApplicationContext(), "Step" + this.routeStepCounter + " was reached. Proceed to next step.", Toast.LENGTH_SHORT).show();
                    this.speakingService.speakWords("Você chegou na parada " + this.busRoute.getBusStops().get(nearestBusStopIndex).getName());
                }
            }
        }
    }

    private boolean checkLastInstructionSend(String instruction){
        return (this.lastInstruction.equals(instruction));
    }
}
