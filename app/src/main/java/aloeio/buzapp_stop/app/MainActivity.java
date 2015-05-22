package aloeio.buzapp_stop.app;

import aloeio.buzapp_stop.app.Fragments.MapFragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements MapFragment.OnFragmentInteractionListener{

//    public MapManagerService mapManagerService;
//    private ArrayAdapter<String> searchLinesAdapter = null;
//    private ImageButton locationImageButton;
//    private ImageButton settingsImageButton;
//    private ImageButton scheduleImageButton;
//    private Button pronaucementImageButton;
//    private Button searchButton;
//    private Utils utils;
//    private SpeakingService speakingService;
//    private MapView map;
//    private SearchService searchService;
//    private String[] LINES = new String[] { "T131", "T132","A339" };
//    private LinearLayout loadingLinearLayout;
//    private LinearLayout pronaucementLinearLayout;
//    private LinearLayout baloonTipLinearLayout;
//    private AutoCompleteTextView searchAutoCompleteTextView;
//    private TextView loadingTextView;
    private TextView blueRouteTextView;
    private TextView greenRouteTextView;
    private TextView blueRouteTimeLeftTextView;
    private TextView greenRouteTimeLeftTextView;
    private ImageView buzappLogoImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

//        this.mapManagerService = new MapManagerService(this);
        this.createMainActivityDefaults();
        this.changeRouteData(0,5, "T123");

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
                blueRouteTextView.setText(routeName);
            blueRouteTimeLeftTextView.setText(timeLeft + "min");
        } else{
            if(routeName != null)
                greenRouteTextView.setText(routeName);
            greenRouteTimeLeftTextView.setText(timeLeft + "min");
        }
    }

    private void createMainActivityDefaults(){
        blueRouteTextView = (TextView) findViewById(R.id.main_txt_blue_route);
        greenRouteTextView = (TextView) findViewById(R.id.main_txt_green_route);
        blueRouteTimeLeftTextView = (TextView) findViewById(R.id.main_txt_blue_route_time_left);
        greenRouteTimeLeftTextView = (TextView) findViewById(R.id.main_txt_green_route_time_left);
        buzappLogoImageView = (ImageView) findViewById(R.id.main_img_buzapp_corner_right);
    }
}
