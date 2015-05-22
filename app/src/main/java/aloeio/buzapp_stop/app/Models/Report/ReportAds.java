package aloeio.buzapp_stop.app.Models.Report;

import aloeio.buzapp_stop.app.Interfaces.IBackendJSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pablohenrique on 5/22/15.
 */
public class ReportAds implements IBackendJSON {

    private ArrayList<Object> adsArrayList;

    public ReportAds(ArrayList<Object> adsArrayList){
        this.adsArrayList = adsArrayList;
    }

    public String toJSONString() throws JSONException {
        return toJSON().toString();
    }

    @Override
    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        for(Object object : adsArrayList)
            array.put(object);
        json.accumulate("list", array);
        return json;
    }
}
