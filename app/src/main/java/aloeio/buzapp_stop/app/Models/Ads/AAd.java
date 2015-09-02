package aloeio.buzapp_stop.app.Models.Ads;

import aloeio.buzapp_stop.app.Interfaces.IBackendJSON;
import android.media.Image;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;

/**
 * Created by pablohenrique on 5/22/15.
 */
public abstract class AAd implements Cloneable, IBackendJSON {
    private Date expirationDate;
    private Image image;
    private int expirationCounter = -1;
    private int timesShownCounter = 0;
    private int durationInMiliseconds;
    private int durationInSeconds;
    private JSONObject jsonRepresentation;
    private String imageWidth;
    private String imageHeight;
    private String identifier;
    private String type;

    private static final String AD_FORMAT_0 = "banner";
    private static final String AD_FORMAT_1 = "interstitial";
    private static final String IMAGE_WIDTH_FORMAT_0 = "500dp";
    private static final String IMAGE_HEIGHT_FORMAT_0 = "80dp";
    private static final String IMAGE_WIDTH_FORMAT_1 = "fill_parent";
    private static final String IMAGE_HEIGHT_FORMAT_1 = "fill_parent";
    private static final int TIME_SHOWN_MILISECOND_FORMAT_0 = 60000;
    private static final int TIME_SHOWN_MILISECOND_FORMAT_1 = 20000;

//    public AAd(JSONObject jsonObject){
//        String id = jsonObject.getString('identifier');
//    }

    public AAd(String identifier, Image image, String type, Date expirationDate, int expirationCounter){
        this.setObjectData(identifier, image, this.getWidthByType(type), this.getHeightByType(type), type, expirationDate, expirationCounter);
    }

    public AAd(String identifier, Image image, String type, int expirationCounter){
        this.setObjectData(identifier, image, this.getWidthByType(type), this.getHeightByType(type), type, null, expirationCounter);
    }

    public AAd(String identifier, Image image, String type, Date expirationDate){
        this.setObjectData(identifier, image, this.getWidthByType(type), this.getHeightByType(type), type, expirationDate, this.expirationCounter);
    }

    public AAd(String identifier, Image image, String width, String height, String type, Date expirationDate, int expirationCounter){
        this.setObjectData(identifier, image, width, height, type, expirationDate, expirationCounter);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setObjectData(String identifier, Image image, String width, String height, String type, Date expirationDate, int expirationCounter){
        this.setIdentifier(identifier);
        this.setImage(image);
        this.setImageWidth(width);
        this.setImageHeight(height);
        this.setType(type);
        this.setExpirationDate(expirationDate);
        this.setExpirationCounter(expirationCounter);
        this.setDurationInMiliseconds(getDurationByType(type));
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getExpirationCounter() {
        return expirationCounter;
    }

    public void setExpirationCounter(int expirationCounter) {
        this.expirationCounter = expirationCounter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = this.getAdFormat(type);
    }

    public int getTimesShownCounter() {
        return timesShownCounter;
    }

    public void setTimesShownCounter(int timesShownCounter) {
        this.timesShownCounter = timesShownCounter;
    }

    public JSONObject getJsonRepresentation() {
        return jsonRepresentation;
    }

    public void setJsonRepresentation(JSONObject jsonRepresentation) {
        this.jsonRepresentation = jsonRepresentation;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    //TODO Date verification.
    public boolean canShow(){
        return getTimesShownCounter() < getExpirationCounter();
    }

    // Count how many times an ad was shown.
    public void wasShowed(){
        setTimesShownCounter(getTimesShownCounter() + 1);
    }

    public int getDurationInMiliseconds() {
        return durationInMiliseconds;
    }

    public void setDurationInMiliseconds(int durationInMiliseconds) {
        this.durationInMiliseconds = durationInMiliseconds;
        this.setDurationInSeconds(durationInMiliseconds/1000);
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public JSONObject toJSON() throws JSONException{
        this.jsonRepresentation = new JSONObject();

        this.jsonRepresentation.accumulate("identifier", this.getIdentifier());
        this.jsonRepresentation.accumulate("timesShownCounter", this.getTimesShownCounter());
        this.jsonRepresentation.accumulate("expired", this.canShow());

        return this.jsonRepresentation;
    }

    private void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    private int getDurationByType(String type){
        return (this.getAdFormat(type).equals(AD_FORMAT_0)) ? TIME_SHOWN_MILISECOND_FORMAT_0 : TIME_SHOWN_MILISECOND_FORMAT_1;
    }

    private String getWidthByType(String type){
        return (getAdFormat(type).equals(AD_FORMAT_0)) ? IMAGE_WIDTH_FORMAT_0 : IMAGE_WIDTH_FORMAT_1;
    }

    private String getHeightByType(String type){
        return (getAdFormat(type).equals(AD_FORMAT_0)) ? IMAGE_HEIGHT_FORMAT_0 : IMAGE_HEIGHT_FORMAT_1;
    }

    private String getAdFormat(String type){
        return (type == null || type.equals("") || type.toLowerCase().equals(AD_FORMAT_0)) ? AD_FORMAT_0 : AD_FORMAT_1;
    }
}
