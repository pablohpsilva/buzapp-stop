package aloeio.buzapp_stop.app.Models.Report;

import aloeio.buzapp_stop.app.Interfaces.IBackendJSON;
import aloeio.buzapp_stop.app.Models.Ads.AAd;
import aloeio.buzapp_stop.app.Models.Ads.BannerAd;
import aloeio.buzapp_stop.app.Models.Ads.InterstitialAd;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by pablohenrique on 5/22/15.
 */
public class ReportAds implements IBackendJSON {

    private ArrayList<AAd> adsArrayList;
    private String busStopIdentifier;

    public ReportAds(ArrayList<AAd> adsArrayList){
        this(adsArrayList, "");
    }
    public ReportAds(ArrayList<AAd> adsArrayList, String busStopIdentifier){
        this.adsArrayList = adsArrayList;
        this.busStopIdentifier = busStopIdentifier;
    }

    public String toJSONString() throws JSONException {
        return toJSON().toString();
    }

    @Override
    public JSONObject toJSON() throws JSONException{
        if(!this.adsArrayList.isEmpty()) {
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            for (AAd object : adsArrayList)
                array.put(object.toJSON());

//            if (!this.busStopIdentifier.equals(""))
//                json.accumulate("id", this.busStopIdentifier);
            json.accumulate("list", array);
            return json;
        }
        return null;
    }
}
