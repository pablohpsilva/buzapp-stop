package aloeio.buzapp.app.Utils;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by pablohenrique on 2/18/15.
 */
public class HttpUtils {
    private final String CODEPAGE = "UTF-8";
    private HttpResponse response = null;
    private final Integer TIMEOUT = 6500;

    private HttpParams createHttpParams(){
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT);
        return httpParameters;
    }

    public String getRequest(String url) throws HttpException, SocketException, URISyntaxException, ClientProtocolException, IOException{
        HttpGet request = new HttpGet();
        HttpClient client = new DefaultHttpClient(this.createHttpParams());

        request.setURI(new URI(url));
        response = client.execute(request);

        return convertStreamToString(response.getEntity().getContent());
    }

    public boolean postRequest(String url, JSONObject json) throws SocketException, UnsupportedEncodingException, JSONException, IOException {
        HttpPost request = new HttpPost(url);
        HttpClient client = new DefaultHttpClient(this.createHttpParams());

        request.setEntity(new StringEntity(json.toString().replaceAll("\"","'"), CODEPAGE));
        response = client.execute(request);

        if(response.getStatusLine().getStatusCode() == 200)
            return true;
        else
            return false;
    }

    public static String convertStreamToString(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),1024);
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }


}
