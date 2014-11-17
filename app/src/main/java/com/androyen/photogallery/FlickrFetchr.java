package com.androyen.photogallery;

import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rnguyen on 11/17/14.
 */
public class FlickrFetchr {
    private static final String TAG = FlickrFetchr.class.getSimpleName();

    private static final String ENDPOINT = "http://api.flickr.com/services/rest/";
    private static final String API_KEY = "93f326b4268077daea2d034d51be6434";
    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    private static final String PARAM_EXTRAS = "extras";
    private static final String EXTRA_SMALL_URL = "url_s";

    //Get raw bytes from Flickr
    byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null; //If HTTP query had errors, quit
            }

            //Basic reading bytes from output stream
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read()) > 0) {
                out.write(buffer, 0, bytesRead);
            }

            //Close stream
            out.close();

            //Return raw bytes
            return out.toByteArray();
        }
        finally {
            connection.disconnect();
        }
    }

    //Convert from bytes to String
    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    //URL for fetching Flickr items
    public void fetchItems() {
        try {
            String url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_GET_RECENT)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
                    .build().toString();

            String xmlString = getUrl(url);
            Log.i(TAG, "Received xml: " + xmlString);
        }
        catch (IOException e) {
            Log.e(TAG, "Failed to fetch items", e);
        }
    }
}
