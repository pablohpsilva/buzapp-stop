package aloeio.buzapp_stop.app.Models.Ads;

import android.media.Image;

import java.sql.Date;

/**
 * Created by pablohenrique on 5/22/15.
 */
public class InterstitialAd extends AAd implements Cloneable{

    public InterstitialAd(String identifier, Image image, Date expirationDate, int expirationCounter){
        super(identifier, image, "interstitial", expirationDate, expirationCounter);
    }

    public InterstitialAd(String identifier, Image image, int expirationCounter){
        super(identifier, image, "interstitial", expirationCounter);
    }

    public InterstitialAd(String identifier, Image image, Date expirationDate){
        super(identifier, image, "interstitial", expirationDate);
    }

    public InterstitialAd(String identifier, Image image, String width, String height, Date expirationDate, int expirationCounter){
        super(identifier, image, width, height, "interstitial", expirationDate, expirationCounter);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
