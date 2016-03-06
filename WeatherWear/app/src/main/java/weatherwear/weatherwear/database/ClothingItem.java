package weatherwear.weatherwear.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by alexbeals on 2/27/16.
 */
public class ClothingItem {
    private Long id;            // The id for use in the database
    private String type;        // The type that the clothing item is e.g. 'Long Sleeve', 'Shorts'
    private int cycleLength;    // The number of days in between available uses
    private int lastUsed;       // The unix timestamp for the last time it was used
    private boolean fall;
    private boolean winter;
    private boolean spring;
    private boolean summer;
    private Bitmap image;       // The image of the clothing item

    public ClothingItem() {
        this.id = 0L;
        this.type = "";
        this.cycleLength = 0;
        this.lastUsed = 0;
        fall = false;
        winter = false;
        spring = false;
        summer = false;
    }

    public ClothingItem(Long id, String type, int cycleLength, int lastUsed, boolean fall, boolean winter, boolean spring, boolean summer, Bitmap image) {
        this.id = id;
        this.type = type;
        this.cycleLength = cycleLength;
        this.lastUsed = lastUsed;
        this.fall = fall;
        this.winter = winter;
        this.spring = spring;
        this.summer = summer;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCycleLength() {
        return cycleLength;
    }

    public void setCycleLength(int cycleLength) {
        this.cycleLength = cycleLength;
    }

    public int getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(int lastUsed) {
        this.lastUsed = lastUsed;
    }

    public void setUsed() {
        this.lastUsed = (int)(System.currentTimeMillis() / 1000L);
    }

    public boolean getFall() {
        return fall;
    }

    public boolean getWinter() {
        return winter;
    }

    public boolean getSpring() {
        return spring;
    }

    public boolean getSummer() {
        return summer;
    }

    public void setFall(boolean fall) {
        this.fall = fall;
    }

    public void setWinter(boolean winter) {
        this.winter = winter;
    }

    public void setSpring(boolean spring) {
        this.spring = spring;
    }

    public void setSummer(boolean summer) {
        this.summer = summer;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public byte[] getImageByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public void setImageFromByteArray(byte[] image) {
        this.image = BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
