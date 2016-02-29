package weatherwear.weatherwear.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

/**
 * Created by alexbeals on 2/27/16.
 */
public class ClothingItem {
    private Long id;            // The id for use in the database
    private String type;        // The type that the clothing item is e.g. 'Long Sleeve', 'Shorts'
    private int cycleLength;    // The number of days in between available uses
    private int lastUsed;       // The unix timestamp for the last time it was used
    private boolean[] seasons;  // An array of the valid seasons (0th index is fall, 1 is winter, 2 is spring, 3 is summer)
    private Bitmap image;       // The image of the clothing item

    public ClothingItem() {
        this.id = 0L;
        this.type = "";
        this.cycleLength = 0;
        this.lastUsed = 0;
        this.seasons = new boolean[] {false, false, false, false};
    }

    public ClothingItem(Long id, String type, int cycleLength, int lastUsed, boolean[] seasons, Bitmap image) {
        this.id = id;
        this.type = type;
        this.cycleLength = cycleLength;
        this.lastUsed = lastUsed;
        this.seasons = seasons;
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

    public boolean[] getSeasons() {
        return seasons;
    }

    public String getSeasonsString() {
        String seasonsString = "";
        for (boolean season : seasons) {
            seasonsString += (season ? "1" : "0") + ",";
        }

        if (seasonsString.length() > 0) {
            seasonsString = seasonsString.substring(0, seasonsString.length()-1);
        }
        return seasonsString;
    }

    public void setSeasons(boolean[] seasons) {
        this.seasons = seasons;
    }

    public void setSeasonsFromString(String seasons) {
        String[] s = seasons.split(",");
        for (int i = 0; i < s.length; i++) {
            this.seasons[i] = (s[i].equals("1"));
        }
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
