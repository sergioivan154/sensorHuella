package sagarpa.planetmedia.com.sagarpapp.Model.Adapter;

import java.io.File;

import sagarpa.planetmedia.com.sagarpapp.Utility.Constants;

public class MobiStegoItem {

    private String message;
    private File bitmap;
    private File bitmapCompressed;
    private boolean encoded;
    private String uuid;
    private String password;

    private MobiStegoItem() {
        super();

    }

    public MobiStegoItem(String message, File bitmap, String uuid, boolean encoded,String password) {
        this();
        this.bitmap = bitmap;
        String tmp = bitmap.getAbsolutePath();
        tmp = tmp.substring(0, tmp.length() - 4);
        this.bitmapCompressed = new File(tmp + Constants.FILE_JPG_EXT);
        this.encoded = encoded;
        this.message = message;
        this.password=password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public File getBitmap() {
        return bitmap;
    }

    public void setBitmap(File bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isEncoded() {
        return encoded;
    }

    public void setEncoded(boolean encoded) {
        this.encoded = encoded;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public File getBitmapCompressed() {
        return bitmapCompressed;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
