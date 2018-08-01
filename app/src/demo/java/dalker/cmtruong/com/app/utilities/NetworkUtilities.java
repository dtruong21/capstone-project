package dalker.cmtruong.com.app.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Utilities used to help to generate fake users to implements in this app
 *
 * @author davidetruong
 * @version 1.0
 * @since 1st August, 2018
 */
public class NetworkUtilities {

    /**
     * Function to build the URL
     *
     * @param mQuery
     * @return
     */
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

    /**
     * Function to get the response from the http request
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String getResultFromHttp(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
