package aloeio.buzapp_stop.app;

import aloeio.buzapp_stop.app.Fragments.BannerAdsFragment;
import aloeio.buzapp_stop.app.Fragments.InterstitialAdsFragment;
import aloeio.buzapp_stop.app.Fragments.MapFragment;
import aloeio.buzapp_stop.app.Models.Ads.InterstitialAd;
import aloeio.buzapp_stop.app.Services.SearchService;
import aloeio.buzapp_stop.app.Utils.Constants.UrlConstants;
import aloeio.buzapp_stop.app.Utils.Utils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;


public class MainActivity extends FragmentActivity implements
        MapFragment.OnFragmentInteractionListener,
        BannerAdsFragment.OnFragmentInteractionListener,
        InterstitialAdsFragment.OnFragmentInteractionListener{
    private TextView redRouteTextView;
    private TextView blueRouteTextView;
    private TextView redRouteTimeLeftTextView;
    private TextView blueRouteTimeLeftTextView;
    private ImageView buzappLogoImageView;
    private Utils utils = null;
    final private static String MAPZIPNAME = "Uberlandia_2015-03-06_223449.zip";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        utils = new Utils();
        copyMapFileIfNeeded();

        this.createMainActivityDefaults();
        this.changeRouteData(0, 5, "T123");

        this.callAdsFragments();

//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragment_container, new MapFragment())
//                .commit();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
//            configFragment.setArguments(getIntent().getExtras());
//            this.callFragment(0, null);
//            this.setMainActivityDefaults();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void changeRouteData(int route, int timeLeft){
        changeRouteData(route, timeLeft, null);
    }

    public void changeRouteData(int route, int timeLeft, String routeName){
        if(route == 0){
            if(routeName != null)
                redRouteTextView.setText(routeName);
            redRouteTimeLeftTextView.setText(timeLeft + "min");
        } else {
            if(routeName != null)
                blueRouteTextView.setText(routeName);
            blueRouteTimeLeftTextView.setText(timeLeft + "min");
        }
    }

    private void createMainActivityDefaults(){
        redRouteTextView = (TextView) findViewById(R.id.main_txt_blue_route);
        blueRouteTextView = (TextView) findViewById(R.id.main_txt_green_route);
        redRouteTimeLeftTextView = (TextView) findViewById(R.id.main_txt_blue_route_time_left);
        blueRouteTimeLeftTextView = (TextView) findViewById(R.id.main_txt_green_route_time_left);
        buzappLogoImageView = (ImageView) findViewById(R.id.main_img_buzapp_corner_right);
    }

    private void callAdsFragments(){
        new Runnable(){
            @Override
            public void run(){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.map_banner_ads, new BannerAdsFragment())
                        .commit();
            }
        }.run();

        new Runnable(){
            @Override
            public void run(){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.map_interstitial_ads, new InterstitialAdsFragment())
                        .commit();
            }
        }.run();
    }

    private void copyMapFileIfNeeded(){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/osmdroid/"+MAPZIPNAME);
        if(!file.exists()){
            // If user does not have the map, create a copy on osmdroid folder, then.
            utils.copyMapFile("file://android_asset/", MAPZIPNAME, Environment.getExternalStorageDirectory().getAbsolutePath() + "/osmdroid/", MainActivity.this);
        }
    }
}
