package aloeio.buzapp_stop.app.Interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pablohenrique on 5/17/15.
 */
public interface IBackendJSON {
    public JSONObject toJSON() throws JSONException;
}