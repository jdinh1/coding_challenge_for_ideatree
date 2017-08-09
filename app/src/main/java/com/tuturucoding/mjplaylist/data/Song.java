package com.tuturucoding.mjplaylist.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class Song implements Parcelable {
    private String songName;
    private String artistName;
    private String releaseDate;
    private String iconUrl;
    private String genre;
    private String length;

    public Song() {
    }

    private Song(Parcel in) {
        songName = in.readString();
        artistName = in.readString();
        releaseDate = in.readString();
        iconUrl = in.readString();
        genre = in.readString();
        length = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songName);
        dest.writeString(artistName);
        dest.writeString(releaseDate);
        dest.writeString(iconUrl);
        dest.writeString(genre);
        dest.writeString(length);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = formatlength(length);
        Log.v("time", this.length);
    }

    private String formatlength(long l) {

        String time;

        time = String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(l),
                TimeUnit.MILLISECONDS.toSeconds(l) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))
        );

        return time;
    }

    private String formatReleaseDate(String d) {
        String date = null;
        String[] temp = d.split("T");
        date = temp[0];
        return date;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = formatReleaseDate(releaseDate);
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
