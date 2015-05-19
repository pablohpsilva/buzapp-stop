package aloeio.buzapp_stop.app.Services;

import android.util.Log;
import aloeio.buzapp_stop.app.Models.Report.Report;
import aloeio.buzapp_stop.app.Models.Report.ReportException;
import org.json.JSONException;

/**
 * Created by pablohenrique on 4/8/15.
 */
public class ExceptionService {

    public final static ExceptionService instance = new ExceptionService();

    private ExceptionService(){}

    public static ExceptionService getInstance(){
        return instance;
    }

    private void sendErrorReport(Class mclass, Exception exception) throws JSONException {
        Log.i("ExceptionControllerS", new Report(new ReportException(exception)).toJSONString());
//        ReportSender sender = new ReportSender();
//        sender.sendReport(new report(new ReportException(exception)), UrlConstants.POST_EXCEPTION);
    }

    private void dispatchEvent(String tag){
//        Dispatcher.getInstance().dispatchEvent(new Event(tag));
    }

    private void processException(Class mclass, Exception exception, String tag){
        try {
            sendErrorReport(mclass, exception);
//            dispatchEvent(tag);
        } catch(Exception e){
            processException(ExceptionService.class, e, "exception");
        }
    }

    public void catchException(Class mclass, Exception exception){
        processException(mclass, exception, "exception");
    }

    public void catchException(Class mclass, Exception exception, String tag){
        processException(mclass, exception, tag);
    }
}

