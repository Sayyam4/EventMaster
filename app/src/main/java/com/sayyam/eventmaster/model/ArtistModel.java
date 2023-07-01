package com.sayyam.eventmaster.model;

import java.util.ArrayList;

public class ArtistModel {
    public ArrayList<Album> albums;
    public Artist artist;

    public class Album {
        public String album_group;
        public String album_type;
        public ArrayList<Artist> artists;
        public ArrayList<String> available_markets;
        public ExternalUrls external_urls;
        public String href;
        public String id;
        public ArrayList<Image> images;
        public String name;
        public String release_date;
        public String release_date_precision;
        public int total_tracks;
        public String type;
        public String uri;
    }

    public class Artist {
        public ExternalUrls external_urls;
        public Followers followers;
        public ArrayList<String> genres;
        public String href;
        public String id;
        public ArrayList<Image> images;
        public String name;
        public int popularity;
        public String type;
        public String uri;
    }


    public class ExternalUrls {
        public String spotify;
    }

    public class Followers {
        public Object href;
        public int total;
    }

    public class Image {
        public int height;
        public String url;
        public int width;
    }
}
