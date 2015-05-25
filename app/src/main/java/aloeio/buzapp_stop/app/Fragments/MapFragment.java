package aloeio.buzapp_stop.app.Fragments;

import aloeio.buzapp_stop.app.Services.MapManagerService;
import aloeio.buzapp_stop.app.Services.SearchService;
import aloeio.buzapp_stop.app.Services.SpeakingService;
import aloeio.buzapp_stop.app.Utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aloeio.buzapp_stop.app.R;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import org.osmdroid.views.MapView;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment{

    public MapManagerService mapManagerService;
    private ArrayAdapter<String> searchLinesAdapter = null;
    private ImageButton locationImageButton;
    private ImageButton settingsImageButton;
    private ImageButton scheduleImageButton;
    private Button pronaucementImageButton;
    private Button searchButton;
    private Utils utils;
    private SpeakingService speakingService;
    private MapView map;
    private SearchService searchService;
    private String[] LINES = new String[] { "T131", "T132","A339" };
    private LinearLayout loadingLinearLayout;
    private LinearLayout pronaucementLinearLayout;
    private LinearLayout baloonTipLinearLayout;
    private AutoCompleteTextView searchAutoCompleteTextView;
    private TextView loadingTextView;
    private View rootView;
    private MapView mapView;
    private Handler search;
    private Runnable searchRunnable;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        setMainActivityDefaults();
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart(){
        super.onStart();
        if(this.mapView == null) {
            this.mapView = (MapView) MapFragment.this.getActivity().findViewById(R.id.home_mapview);
            startServices();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void startServices(){
        mapManagerService = new MapManagerService(MapFragment.this);
        speakingService = new SpeakingService(MapFragment.this);
        searchService = new SearchService();
        utils = new Utils();
        if(utils.checkGPS(MapFragment.this.getActivity()))
            mapManagerService.drawUserLocation();
    }

    private void setMainActivityDefaults() {
//        locationImageButton = (ImageButton) rootView.findViewById(R.id.menu_bar_btn_location);
//        settingsImageButton = (ImageButton) rootView.findViewById(R.id.menu_bar_btn_settings);
//        scheduleImageButton = (ImageButton) rootView.findViewById(R.id.menu_bar_btn_schedule);
//        pronaucementImageButton = (Button) rootView.findViewById(R.id.home_btn_report_bus_issues);
        searchButton = (Button) rootView.findViewById(R.id.home_btn_search);
//
//        pronaucementLinearLayout = (LinearLayout) rootView.findViewById(R.id.home_layout_report_bus_issue);
        loadingLinearLayout = (LinearLayout) rootView.findViewById(R.id.loading_template);
        baloonTipLinearLayout = (LinearLayout) rootView.findViewById(R.id.home_balloon_tip);

        searchAutoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.home_edt_autocomplete);

        loadingTextView = (TextView) rootView.findViewById(R.id.loading_text);
        loadingTextView.setText(R.string.home_txt_loading);

//        LINES = searchService.getAllRouteIDS(this.getApplicationContext());
        searchLinesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_dropdown_item_line, LINES);
        searchAutoCompleteTextView.setAdapter(searchLinesAdapter);

        searchAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toggleLoadingScreen(true);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchAutoCompleteTextView.getWindowToken(), 0);
                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                Log.d("AUTOCOMPLETE: ", searchAutoCompleteTextView.getText().toString());
                                searchService.searchLine(searchAutoCompleteTextView.getText().toString(), MapFragment.this);
                                searchAutoCompleteTextView.setText("");
                                searchAutoCompleteTextView.setVisibility(View.INVISIBLE);
                                toggleLoadingScreen(false);
                            }
                        }, 500);
            }
        });

        this.setDefaultClickListener();
    }

    private void setDefaultClickListener() {
//        locationImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (utils.checkGPS(getActivity()))
//                    mapManagerService.drawUserLocation();
//                else
//                    utils.buildAlertMessageNoGps(getActivity());
//            }
//        });
//
//        settingsImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                callFragment(1, null);
//            }
//        });
//
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchAutoCompleteTextView.getVisibility() == View.VISIBLE)
                    searchAutoCompleteTextView.setVisibility(View.INVISIBLE);
                else
                    searchAutoCompleteTextView.setVisibility(View.VISIBLE);
            }
        });
        baloonTipLinearLayout.setVisibility(View.GONE);
//        callSearch();

//
//
//
//        scheduleImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                callFragment(2, null);
//                utils.hideSoftKeyboard(getActivity());
//            }
//        });
//
//
//        baloonTipLinearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                baloonTipLinearLayout.setVisibility(View.INVISIBLE);
//            }
//        });
//
//        this.changeVisibility();
//
//        pronaucementImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(getActivity(), ReportBusIssueActivity.class));
//            }
//        });
    }

    private void toggleLoadingScreen(boolean toggle){
        loadingLinearLayout.setVisibility((toggle) ? View.VISIBLE : View.INVISIBLE);
        Log.d("LOADING: ", "DONE");
    }

    private void changeVisibility(){
//        layout_report_bus_issue.setVisibility((AuthUserSingletonController.hasUserLoggedOn()) ? View.VISIBLE : View.INVISIBLE);
//        layout_report_bus_issue.setVisibility((userAuthentificator.hasUserLoggedOn()) ? View.VISIBLE : View.INVISIBLE);
    }

    private void callFragment(int fragment, Bundle bundle){
//        switch (fragment){
//            case 0:
//                getFragmentManager().beginTransaction()
////                        .add(R.id.fragment_container, ((android.support.v4.app.Fragment) ((bundle == null) ? new LoginFragment() : new LoginFragment().setArguments(bundle))))
//                        .add(R.id.fragment_container, new LoginFragment())
//                        .addToBackStack(null)
//                        .commit();
//                break;
//            case 1:
//                getFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_top)
//                        .add(R.id.fragment_container, new ConfigFragment())
//                        .addToBackStack(null)
//                        .commit();
//                break;
//            case 2:
//                getFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom)
//                        .add(R.id.fragment_container, new ScheduleFragment())
//                        .addToBackStack(null)
//                        .commit();
//                break;
//            default:
//                break;
//        }
    }

    public void changeDistance(double d){

    }

    public void callSearch(){
        search = new Handler();
        searchRunnable = new Runnable(){
            @Override
            public void run(){
                SearchService service = new SearchService();
                service.searchLine("T131",MapFragment.this);
                System.out.println("Route");
                search.removeCallbacks(searchRunnable);
                searchRunnable = null;
                search = null;
            }
        };
        search.post(searchRunnable);
    }

}
