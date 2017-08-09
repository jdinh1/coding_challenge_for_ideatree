package com.tuturucoding.mjplaylist.adapters;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuturucoding.mjplaylist.R;
import com.tuturucoding.mjplaylist.data.Song;

import java.io.InputStream;

public class SongAdapter extends BaseAdapter {
    private Context mContext;
    private Song[] mSongs;
    public final LruCache<String, Bitmap> memoryCache;

    public SongAdapter(Context context, Song[] songs) {
        mContext = context;
        mSongs = songs;
        final int memClass = ((ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();
        // 1/8 of the available mem
        final int cacheSize = 1024 * 1024 * memClass / 8;
        memoryCache = new LruCache<>(cacheSize);
    }

    @Override
    public int getCount() {
        return mSongs.length;
    }

    @Override
    public Object getItem(int position) {
        return mSongs[position];
    }

    @Override
    public long getItemId(int position) {
        return 0; // we aren't going to use this. Tag items for easy reference
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.song_item, null);
            holder = new ViewHolder();
            holder.albumImage = (ImageView) convertView.findViewById(R.id.coverLabel);
            holder.titleLabel = (TextView) convertView.findViewById(R.id.titleLabel);
            holder.artistLabel = (TextView) convertView.findViewById(R.id.artistLabel);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Song song = mSongs[position];

        // populating views with data
        new DownloadImageTask(holder.albumImage)
                .execute(song.getIconUrl());
        holder.titleLabel.setText(song.getSongName());
        holder.artistLabel.setText(song.getArtistName());

        return convertView;
    }

    private static class ViewHolder {
        ImageView albumImage;
        TextView titleLabel;
        TextView artistLabel;
    }

    // https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap mBitmap = null;
            String urldisplay = urls[0];
            if (urldisplay != null && urldisplay.length() > 0) {
                // begin testing for cache
                mBitmap = (Bitmap) memoryCache.get(urldisplay);
                if (mBitmap != null) {  // bitmap exists in memory cache
                    return mBitmap;
                } else {    // bitmap does not exist
                    try {
                        InputStream in = new java.net.URL(urldisplay).openStream();
                        mBitmap = BitmapFactory.decodeStream(in);
                        memoryCache.put(urldisplay, mBitmap);
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            return mBitmap;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
