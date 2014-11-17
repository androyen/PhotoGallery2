package com.androyen.photogallery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rnguyen on 11/17/14.
 */
public class FlickrFetchr {

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
}
