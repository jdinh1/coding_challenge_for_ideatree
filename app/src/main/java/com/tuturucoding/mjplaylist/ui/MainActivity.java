package com.tuturucoding.mjplaylist.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tuturucoding.mjplaylist.R;
import com.tuturucoding.mjplaylist.data.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String SONG_LIST = "SONG_LIST";
    private Song[] songList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.getListButton);
        new getSongData().execute();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SongListActivity.class);
                intent.putExtra(SONG_LIST, songList);
                startActivity(intent);
            }
        });
    }

    private class getSongData extends AsyncTask<String, Void, Void> {
        String data = null;

        @Override
        protected Void doInBackground(String... params) {

            try {
                String songUrl = "https://itunes.apple.com/search?term=Michael+jackson";
                URL getURL = new URL(songUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) getURL.openConnection();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        data = readStream(in);
                        // getting songs information
                        songList = populateSongs(data);
                    } finally {
                        urlConnection.disconnect();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    // parsing json data and populate songs array
    private Song[] populateSongs(String jsonData) {
        Song[] tempSongList = null;
        try {

            JSONObject songInfo = new JSONObject(jsonData);
            int size = songInfo.getInt("resultCount");
            //tempSongList = new Song[size];

            JSONArray songs = songInfo.getJSONArray("results");
            tempSongList = new Song[songs.length()];
            for (int i = 0; i < songs.length(); i++) {
                JSONObject temp = songs.getJSONObject(i);
                Song song = new Song();
                song.setArtistName(temp.getString("artistName"));
                song.setReleaseDate(temp.getString("releaseDate"));
                song.setGenre(temp.getString("primaryGenreName"));
                song.setIconUrl(temp.getString("artworkUrl100"));
                song.setSongName(temp.getString("trackName"));
                song.setLength(temp.getLong("trackTimeMillis"));

                tempSongList[i] = song;
                Log.v("12321", song.getSongName());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tempSongList;
    }

}
