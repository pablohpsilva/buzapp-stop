package aloeio.buzapp_stop.app.Models.Ads;

import android.media.Image;

import java.sql.Date;

/**
 * Created by pablohenrique on 5/22/15.
 */
public class BannerAd extends AAd implements Cloneable{

    public BannerAd(String identifier, Image image, Date expirationDate, int expirationCounter){
        super(identifier, image, "banner", expirationDate, expirationCounter);
    }

    public BannerAd(String identifier, Image image, int expirationCounter){
        super(identifier, image, "banner", expirationCounter);
    }

    public BannerAd(String identifier, Image image, Date expirationDate){
        super(identifier, image, "banner", expirationDate);
    }

    public BannerAd(String identifier, Image image, String width, String height, Date expirationDate, int expirationCounter) {
        super(identifier, image, width, height, "banner", expirationDate, expirationCounter);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}