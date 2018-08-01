package dalker.cmtruong.com.app.utilities;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtilities {

    public static URL buildURL(String mQuery) {
        Uri uri = Uri.parse(mQuery).buildUpon().build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
