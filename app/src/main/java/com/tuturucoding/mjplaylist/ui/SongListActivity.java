package com.tuturucoding.mjplaylist.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListView;

import com.tuturucoding.mjplaylist.R;
import com.tuturucoding.mjplaylist.adapters.SongAdapter;
import com.tuturucoding.mjplaylist.data.Song;

import java.util.Arrays;

public class SongListActivity extends ListActivity {
    public static final String SONG_DETAIL_TAG = "SONG_DETAIL_TAG";
    private Song[] mSongs = null;
    public static SongAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.SONG_LIST);

        mSongs = Arrays.copyOf(parcelables,parcelables.length,Song[].class);

        if (mSongs != null) {
            mAdapter = new SongAdapter(this, mSongs);
            setListAdapter(mAdapter);

        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (mAdapter != null) {
            Song selected = (Song) mAdapter.getItem(position);
            Intent intent = new Intent(this, SongDetailActivity.class);
            intent.putExtra(SONG_DETAIL_TAG, selected);
            startActivity(intent);
        }
    }
}
