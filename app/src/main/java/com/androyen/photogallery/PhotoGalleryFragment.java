package com.androyen.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by rnguyen on 11/17/14.
 */
public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = PhotoGalleryFragment.class.getSimpleName();

    ArrayList<GalleryItem> mItems;
    GridView mGridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //retain the fragment during rotation
        setRetainInstance(true);

        //Execute AsyncTask
        new FetchItemsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mGridView = (GridView)v.findViewById(R.id.gridView);

        //Implement adapter
        setupAdapter();

        return v;
    }

    //Set up adapter for GridView. Used to show captions
    void setupAdapter() {

        //If there is nothing, exit   Checking if fragment is attached to the activity
        if (getActivity() == null || mGridView == null) {
            return;
        }

        if (mItems != null) {
            //Create adapter for Gridview
            mGridView.setAdapter(new ArrayAdapter<GalleryItem>(getActivity(), android.R.layout.simple_gallery_item, mItems));

        }
        else {
            //If the arraylist is empty. do not set to the view
            mGridView.setAdapter(null);
        }
    }

    //AsyncTask to run networking calls
    private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>> {

        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... params) {
//            try {
//                String result = new FlickrFetchr().getUrl("http://www.google.com");
//                Log.i(TAG, "Fetched contents of URL: " + result);
//            }
//            catch (IOException e) {
//                Log.e(TAG, "Failed to fetch URL: ", e);
//            }
           return new FlickrFetchr().fetchItems();


        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items) {
            //Called on the UI thread.  Updating the view to show the photo captions
            mItems = items;
            setupAdapter();
        }

    }
}
