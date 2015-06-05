package aloeio.buzapp_stop.app.Utils.Constants;

/**
 * Created by pablohenrique on 2/6/15.
 */
public class UrlConstants {
    private static final String BACKEND_PROTOCOL        = "http://";
    private static final String BACKEND_SERVICE_IP      = "buzapp-services.aloeio.com";
    private static final String BACKEND_SERVICE_PORT    = ":80/";
    private static final String BACKEND_SERVICE_URL     = BACKEND_PROTOCOL + BACKEND_SERVICE_IP + BACKEND_SERVICE_PORT;
    private static final String BACKEND_ROUTE_PATH_URL  = "busweb/route/get/";
    private static final String BACKEND_BUS_PATH_URL    = "busweb/bus/get/";
    private static final String BACKEND_SEARCH_PATH_URL = "busweb/search/";
    public static final String BUS_T131_TEST1   = BACKEND_SERVICE_URL + BACKEND_BUS_PATH_URL + "T131/1";
    public static final String BUS_T131_TEST2   = BACKEND_SERVICE_URL + BACKEND_BUS_PATH_URL + "T131/2";
    public static final String BUS_T131_TEST3   = BACKEND_SERVICE_URL + BACKEND_BUS_PATH_URL + "T131/3";

    public static final String BUS_T132_TEST1   = BACKEND_SERVICE_URL + BACKEND_BUS_PATH_URL + "T132/1";

    public static final String ROUTE_T131   = BACKEND_SERVICE_URL + BACKEND_ROUTE_PATH_URL + "T131";
    public static final String ROUTE_T132   = BACKEND_SERVICE_URL + BACKEND_ROUTE_PATH_URL + "T132";

//    public static final String STOP_T131_1 = "http://54.69.229.42:8080/busweb/bus/get/T131/1";
//    public static final String STOP_T131_1 = "http://54.69.229.42:8080/busweb/stop/get/T131/1";
//    public static final String STOP_T131_1 = "http://54.69.229.42:8080/busweb/route/get/T131";



    public static final String SEARCH_EXAMPLE   = BACKEND_SERVICE_URL + BACKEND_SEARCH_PATH_URL + "T131";

    public static final String SEARCH_MODEL = BACKEND_SERVICE_URL + BACKEND_SEARCH_PATH_URL;
    public static final String BUS_MODEL    = BACKEND_SERVICE_URL + BACKEND_BUS_PATH_URL;
    public static final String ROUTE_MODEL  = BACKEND_SERVICE_URL + BACKEND_ROUTE_PATH_URL;
    public static final String STOP_MODEL   = BACKEND_SERVICE_URL + BACKEND_ROUTE_PATH_URL;
    public static final String SEARCH_ALL_IDS = BACKEND_SERVICE_URL + BACKEND_ROUTE_PATH_URL+"allids";


//    public static final String JSON_TEST = "http://ip.jsontest.com/?callback=showMyIP";
//    public static final String GSON_TEST = "https://ajax.googleapis.com/ajax/services/search/web?v=1.0&q={query}";
//    public static final String GSON_TEST2 = "https://ajax.googleapis.com/ajax/services/search/web?v=1.0";
}
