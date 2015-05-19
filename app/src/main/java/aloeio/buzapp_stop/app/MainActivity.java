package aloeio.buzapp_stop.app;

import aloeio.buzapp_stop.app.Fragments.MapFragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

//        this.mapManagerService = new MapManagerService(this);

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
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_in)
                    .add(R.id.fragment_container, new MapFragment())
                    .addToBackStack(null)
                    .commit();
//            this.setMainActivityDefaults();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
