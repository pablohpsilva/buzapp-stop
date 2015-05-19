package aloeio.buzapp_stop.app.Services;

import aloeio.buzapp_stop.app.Fragments.MapFragment;
import aloeio.buzapp_stop.app.Utils.Constants.UrlConstants;
import aloeio.buzapp_stop.app.Utils.HttpUtils;
import aloeio.buzapp_stop.app.Utils.Utils;
import android.support.v4.app.Fragment;
import org.apache.http.HttpException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by eduardo on 3/15/15.
 */
public class SearchService {


    private JSONArray jsonArray = null;
    private HttpUtils http = null;
    private ArrayList<String> searchResultValues = null;
    private String lineName = null;
    private static String routeURL = null;
    private static String stopURL = null;
    private static String busURL = null;
    private static Utils util = null;
    private ExceptionService exceptionControllerSingleton = ExceptionService.getInstance();

    private static final String SEARCH_NO_RESULTS = "Ops, nenhum resultado encontrado =( ";
    private static final String SEARCH_NO_CONNECTION = "Falha de conexão com a internet. Por favor, tente mais tarde. ";

    public SearchService(){
        util = new Utils();
        searchResultValues = new ArrayList<String>();
        http = new HttpUtils();
    }

//    public ArrayList<String> doSearch(String term, Activity activity){
//        clearSearchResults(searchResultValues);
//        if(!util.checkConnection(activity)){
//            searchResultValues.add(0, SEARCH_NO_CONNECTION);
//            return searchResultValues;
//        } else {
//            try {
//                jsonArray = new JSONArray(http.getRequest(UrlConstants.SEARCH_MODEL + term));
//
//                if (jsonArray.isNull(0)) {
//                    searchResultValues.add(0, SEARCH_NO_RESULTS);
//                } else {
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        searchResultValues.add(i, jsonArray.getJSONObject(i).get("name").toString());
//                    }
//                }
//
//                return searchResultValues;
//
//            } catch (HttpException e){
//                exceptionControllerSingleton.catchException(SearchService.class, e, "bad backend");
//            } catch (IOException e){
//                exceptionControllerSingleton.catchException(SearchService.class, e, "bad backend");
//            } catch (URISyntaxException e){
//                exceptionControllerSingleton.catchException(SearchService.class, e, "bad backend");
//            } catch (JSONException e){
//                exceptionControllerSingleton.catchException(SearchService.class, e, "no route");
//            } catch (Exception e){
//                exceptionControllerSingleton.catchException(SearchService.class, e);
//            }
//
//            return searchResultValues;
//        }
//    }

    public void clearSearchResults(ArrayList<String> searchResultValues) {
        if(searchResultValues != null){
            searchResultValues.clear();
        }
    }

    private void showAllBuses(final android.support.v4.app.Fragment activity, final String URL){
        new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(http.getRequest(routeURL));
                    JSONArray busList = json.getJSONArray("listBus");

                    ((MapFragment)activity).mapManagerService.drawBusStops(json);
                    ((MapFragment)activity).mapManagerService.drawBuses(busList);
//            activity.mapManagerService.drawBus(URL + busList.getJSONObject(0).getString("id"));
                    //activity.mapManagerService.testNavigation();

//            BusManagerService.clear();
//            for (int i = 0; i < busList.length(); i++)
//                BusManagerService.addBusService(((BusService) busClonable.clone()).setup(activity, URL + ((JSONObject) busList.get(i)).get("id")));
                } catch (Exception e) {
                    exceptionControllerSingleton.catchException(SearchService.class, e, "no connection");
                    //e.printStackTrace();
                }
            }
        }.run();
    }


    public void searchLine(String lineID, android.support.v4.app.Fragment fragment){
        try {
            lineName = lineID;
            if(lineName != null && !lineName.equals("")){
//                if(map.getOverlays() != null && !map.getOverlays().isEmpty()){
//                    mapmgn.clearBusStops();
//                    mapmgn.clearMapOverlays();
//                }
                lineName = lineID;
                routeURL = UrlConstants.ROUTE_MODEL + lineName;
                stopURL = UrlConstants.STOP_MODEL + lineName;
                final String busURLDefault = UrlConstants.BUS_MODEL + lineName + "/";
//                String busURL1 = busURLDefault + "/1";
//                String busURL2 = busURLDefault + "/2";
//                String busURL3 = busURLDefault + "/3";
                this.showAllBuses(fragment, busURLDefault);
            }

        //}
        //catch (JSONException e) {
        //    exceptionControllerSingleton.catchException(SearchService.class, e, "no route");
        } catch (Exception e) {
            exceptionControllerSingleton.catchException(SearchService.class, e, "no connection");



        }
    }

//    public String[] getAllRouteIDS(Context context) {
//
////        RouteIDDAO routeIDDAO = new RouteIDDAO(context);
////        List<String> ids = routeIDDAO.getAllRouteIDs();
//        String[] lines = null;
////
////
////        if(ids.size()>0)
////        {
////
////            lines = new String[ids.size()];
////            lines = ids.toArray(lines);
////            return lines;
////        }
////        else
////        {
////
////            ArrayList<String> lines1 = new ArrayList<String>(Arrays.asList(GeneralConstants.ALL_ROUTE_IDS));;
////            routeIDDAO.post(lines1);
////
////            ids=routeIDDAO.getAllRouteIDs();
////            lines = new String[ids.size()];
////            lines = ids.toArray(lines);
////            //TODO: BUSCAR NO BACKEND UM JSON COM TODAS AS SILGAS EM ARRAY
//////            activity.mapManagerService.drawBusStops(json);
//////            activity.mapManagerService.drawBus(URL + busList.getJSONObject(0).getString("id"));
////
//////            Log.d("Banco de dados não foi"," OBTENDO JSON");
//////            try {
//////                jsonArray = new JSONArray(http.getRequest(UrlConstants.ROUTE_MODEL + "T131"));
//////
//////                if (jsonArray.isNull(0)) {
//////                    searchResultValues.add(0, SEARCH_NO_RESULTS);
//////                }
//////                else
//////                {
//////                    lines = new String[jsonArray.length()];
//////
//////                    for (int i = 0; i < jsonArray.length(); i++)
//////                        lines[i]=jsonArray.getJSONObject(i).get("name").toString();
//////
//////                }
//////
//////            } catch (HttpException | URISyntaxException | IOException e){
//////                exceptionControllerSingleton.catchException(SearchService.class, e, "bad backend");
//////            } catch (JSONException e){
//////                exceptionControllerSingleton.catchException(SearchService.class, e, "no route");
//////            } catch (Exception e){
//////                exceptionControllerSingleton.catchException(SearchService.class, e);
//////            }
////
////
////
////            return lines;
////        }
//    return lines;
//    }

}
