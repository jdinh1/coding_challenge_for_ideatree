package com.tuturucoding.mjplaylist.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tuturucoding.mjplaylist.R;
import com.tuturucoding.mjplaylist.data.Song;

import java.io.InputStream;

public class SongDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        // get song details
        Intent intent = getIntent();
        Song selectedSong = intent.getExtras().getParcelable(SongListActivity.SONG_DETAIL_TAG);
        assert selectedSong != null;

        // get image bitmap from cache in adapter
        LruCache<String, Bitmap> imageCache = SongListActivity.mAdapter.memoryCache;
        Bitmap mIconBitmap = imageCache.get(selectedSong.getIconUrl());

        TextView title = (TextView) findViewById(R.id.titleLabel);
        TextView collection = (TextView) findViewById(R.id.collectionLabel);
        TextView genre = (TextView) findViewById(R.id.genreLabel);
        TextView duration = (TextView) findViewById(R.id.durationLabel);
        ImageView albumCover = (ImageView) findViewById(R.id.albumCover);
        ImageButton fabPlay = (ImageButton)findViewById(R.id.fab);


        title.setText(selectedSong.getSongName());
        collection.setText("Released - " + selectedSong.getReleaseDate());
        genre.setText("Genre - " + selectedSong.getGenre());
        duration.setText("Duration - " + selectedSong.getLength());

        if (mIconBitmap != null) {
            albumCover.setImageBitmap(mIconBitmap);
        } else {
            new DownloadImageTask(albumCover).execute(selectedSong.getIconUrl());
        }

        fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SongDetailActivity.this, "La la la la <3", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap mBitmap = null;
            String urldisplay = urls[0];
            if (urldisplay != null && urldisplay.length() > 0) {
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mBitmap = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
            return mBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
