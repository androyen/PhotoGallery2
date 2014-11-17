package com.androyen.photogallery;

/**
 * Created by rnguyen on 11/17/14.
 */
public class GalleryItem {

    private String mCaption;
    private String mId;
    private String mUrl;

    //Override toString to return photo captions
    public String toString() {
        return mCaption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String mCaption) {
        this.mCaption = mCaption;
    }
}
