package aloeio.buzapp_stop.app.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import aloeio.buzapp_stop.app.R;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InterstitialAdsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InterstitialAdsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InterstitialAdsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private WebView contentView;
    private View rootView;
    private Timer timer = null;
    private TimerTask timerTask = null;
    private boolean change = true;
    private boolean goAway = true;
    private String summary = "";
    private FrameLayout interstitialFrameLayout;
    private int timeControler = 0;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InterstitialAdsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InterstitialAdsFragment newInstance(String param1, String param2) {
        InterstitialAdsFragment fragment = new InterstitialAdsFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public InterstitialAdsFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_ads, container, false);
        interstitialFrameLayout = (FrameLayout) rootView.findViewById(R.id.interstitial_frame_layout);
        setBannerFragmentDefaults();
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

    public void setContentView(WebView contentView) {
        this.contentView = contentView;
    }

    private void setBannerFragmentDefaults() {
        contentView = (WebView) rootView.findViewById(R.id.interstitial_img_ads);
        contentView.setBackgroundColor(Color.TRANSPARENT);
        WebSettings webSettings = contentView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        setContentView(contentView);

        // disable scroll on touch
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        contentView.post(new Runnable() {
            @Override
            public void run() {
                changeInterstitialAd();
            }
        });
        change = !change;
        renderInterstitialAd(getInterstitialAd(change));

    }

    // Gets banner from service. This is only a test function and will be modified with a service call.
    private String getInterstitialAd(Boolean test){
        summary = (test) ? "https://cloud.githubusercontent.com/assets/2090635/7781881/c369bcbc-00d1-11e5-8dcf-2ac94107bef0.jpg" : "http://api.dailysocial.net/en/wp-content/uploads/2013/10/mobileads_shutterstock_140311663.jpg";
        return summary;
    }

    // Render banner on screen with a webview.
    private void renderInterstitialAd(String imageUrl){
        summary = "<html><body style=\"display:block;overflow:hidden;margin-top:0px;margin-left:0px;margin-right:0px;max-width:500dp\"><img style=\"display: block;margin-left: auto;margin-right: auto\" src=\""+imageUrl+"\" height=\"100%\" width=\"100%\"></body></html>";
        contentView.loadData(summary, "text/html", null);
    }

    // Fetchs and render a new banner at every 10 seconds.
    public void changeInterstitialAd() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //timeControler is used to check when the ad show pop up.
                        timeControler += 1;
                        switch (timeControler){
                            //If it has been 120 second since the last time the interstitial pop up, show it again.
                            case 12:
                                timeControler = 0;
                                rootView.setVisibility(View.VISIBLE);
                                break;
                            //If 40 seconds has passed, load and prepare the ad.
                            case 4:
                                //Prepare the ad
                                change = !change;
                                renderInterstitialAd(getInterstitialAd(change));
                                break;
                            //Otherwise, just hide the ad, please.
                            default:
                                rootView.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 10000);
    }

}
