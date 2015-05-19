package aloeio.buzapp_stop.app.Services;

import aloeio.buzapp_stop.app.Fragments.MapFragment;
import android.support.v4.app.Fragment;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.Locale;

/**
 * Created by root on 24/04/15.
 */
public class SpeakingService implements OnClickListener, TextToSpeech.OnInitListener{

    public static TextToSpeech mtts;
    private int MY_DATA_CHECK_CODE = 0;
    public MapFragment activity;

    public SpeakingService(final MapFragment fragment){
        this.activity = fragment;

        mtts = new TextToSpeech(activity.getActivity().getApplicationContext(), this);
    }

    public void onClick(View v) {

    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS)
        {
            if(mtts.isLanguageAvailable(Locale.getDefault())==TextToSpeech.LANG_AVAILABLE)
                mtts.setLanguage(Locale.getDefault());
        }
    }
    public void speakWords(final String speech) {
        new Runnable() {
            public void run() {
                mtts.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
            }
        }.run();
    }
}
