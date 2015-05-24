package aloeio.buzapp_stop.app;

import aloeio.buzapp_stop.app.Fragments.BannerAdsFragment;
import aloeio.buzapp_stop.app.Fragments.InterstitialAdsFragment;
import aloeio.buzapp_stop.app.Fragments.MapFragment;
import aloeio.buzapp_stop.app.Models.Ads.InterstitialAd;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
        MapFragment.OnFragmentInteractionListener,
        BannerAdsFragment.OnFragmentInteractionListener,
        InterstitialAdsFragment.OnFragmentInteractionListener{
    private static TextView redRouteTextView;
    private static TextView blueRouteTextView;
    private static TextView redRouteTimeLeftTextView;
    private static TextView blueRouteTimeLeftTextView;
    private static ImageView buzappLogoImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

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

    public static void changeRouteData(int route, int timeLeft){
        changeRouteData(route, timeLeft, null);
    }

    public static void changeRouteData(int route, int timeLeft, String routeName){
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
}
