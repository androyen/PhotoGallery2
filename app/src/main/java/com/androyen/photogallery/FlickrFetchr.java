package com.androyen.photogallery;

import android.net.Uri;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rnguyen on 11/17/14.
 */
public class FlickrFetchr {
    private static final String TAG = FlickrFetchr.class.getSimpleName();

    private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
    private static final String API_KEY = "fbd1e979da72724e32d78b8b724403de";
    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    private static final String PARAM_EXTRAS = "extras";
    private static final String EXTRA_SMALL_URL = "url_s";

    private static final String XML_PHOTO = "photo";

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
    public ArrayList<GalleryItem> fetchItems() {

        ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();

        try {
            String url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_GET_RECENT)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
                    .build().toString();

            String xmlString = getUrl(url);
            Log.i(TAG, "Received xml: " + xmlString);

            //Use XMlPullParser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));

            //Feed it an arraylist and a parser
            parseItems(items, parser);
        }
        catch (IOException e) {
            Log.e(TAG, "Failed to fetch items", e);
        }
        catch (XmlPullParserException e) {
            Log.e(TAG, "Failed to parse items", e);
        }

        return items;
    }

    //Parse the XML to get photo
    void parseItems(ArrayList<GalleryItem> items, XmlPullParser parser) throws XmlPullParserException, IOException {

        int eventType = parser.next();

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_TAG && XML_PHOTO.equals(parser.getName())) {

                //Extract id, caption, smallUrl of Photo data
                String id = parser.getAttributeValue(null, "id");
                String caption = parser.getAttributeValue(null, "title");
                String smallUrl = parser.getAttributeValue(null, EXTRA_SMALL_URL);

                //Create new GalleryItem
                GalleryItem item = new GalleryItem();
                item.setId(id);
                item.setCaption(caption);
                item.setUrl(smallUrl);
                items.add(item);


            }

            eventType = parser.next();
        }
    }
}
