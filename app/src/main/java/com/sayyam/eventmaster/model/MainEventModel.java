package com.sayyam.eventmaster.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class MainEventModel {
    public Embedded _embedded;
    public Links _links;
    public Page page;

    public Embedded get_embedded() {
        return _embedded;
    }

    public void set_embedded(Embedded _embedded) {
        this._embedded = _embedded;
    }

    public Links get_links() {
        return _links;
    }

    public void set_links(Links _links) {
        this._links = _links;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public class Access {
        public Date startDateTime;
        public boolean startApproximate;
        public boolean endApproximate;
    }

    public class Accessibility {
        public int ticketLimit;
    }

    public class Address {
        public String line1;
        public String line2;
    }

    public class AgeRestrictions {
        public boolean legalAgeEnforced;
    }

    public class Attraction {
        public String href;
        public String name;
        public String type;
        public String id;
        public boolean test;
        public String url;
        public String locale;
        public ArrayList<Image> images;
        public ArrayList<Classification> classifications;
        public UpcomingEvents upcomingEvents;
        public Links _links;
        public ExternalLinks externalLinks;
        public ArrayList<String> aliases;
    }

    public class BoxOfficeInfo {
        public String phoneNumberDetail;
        public String openHoursDetail;
        public String acceptedPaymentDetail;
        public String willCallDetail;
    }

    public class City {
        public String name;
    }

    public class Classification {
        public boolean primary;
        public Segment segment;
        public Genre genre;
        public SubGenre subGenre;
        public Type type;
        public SubType subType;
        public boolean family;
    }

    public class Country {
        public String name;
        public String countryCode;
    }


    public class Dates {
        public Start start;
        public String timezone;
        public Status status;
        public boolean spanMultipleDays;
        public Access access;
        public End end;
    }

    public class Dma {
        public int id;
    }

    public class Embedded {
        public ArrayList<Event> events;
        public ArrayList<Venue> venues;
        public ArrayList<Attraction> attractions;
    }

    public class End {
        public boolean approximate;
        public boolean noSpecificTime;
    }

    public class Event {
        public Boolean isFavourite;
        public String name;
        public String type;
        public String id;
        public boolean test;
        public String url;
        public String locale;
        public ArrayList<Image> images;
        public double distance;
        public String units;
        public Sales sales;
        public Dates dates;
        public ArrayList<Classification> classifications;
        public Promoter promoter;
        public ArrayList<Promoter> promoters;
        public Seatmap seatmap;
        public Accessibility accessibility;
        public AgeRestrictions ageRestrictions;
        public Ticketing ticketing;
        public Links _links;
        public Embedded _embedded;
        public String info;
        public ArrayList<PriceRange> priceRanges;
        public TicketLimit ticketLimit;
        public String pleaseNote;
        public ArrayList<Product> products;
        public ArrayList<Outlet> outlets;
    }

    public class ExternalLinks {
        public ArrayList<Youtube> youtube;
        public ArrayList<Twitter> twitter;
        public ArrayList<Itune> itunes;
        public ArrayList<Facebook> facebook;
        public ArrayList<Spotify> spotify;
        public ArrayList<Instagram> instagram;
        public ArrayList<Homepage> homepage;
        public ArrayList<Musicbrainz> musicbrainz;
        public ArrayList<Lastfm> lastfm;
        public ArrayList<Wiki> wiki;
    }

    public class Facebook {
        public String url;
    }

    public class First {
        public String href;
    }

    public class GeneralInfo {
        public String generalRule;
        public String childRule;
    }

    public class Genre {
        public String id;
        public String name;
    }

    public class Homepage {
        public String url;
    }

    public class Image {
        public String ratio;
        public String url;
        public int width;
        public int height;
        public boolean fallback;
    }

    public class Instagram {
        public String url;
    }

    public class Itune {
        public String url;
    }

    public class Last {
        public String href;
    }

    public class Lastfm {
        public String url;
    }

    public class Links {
        public Self self;
        public ArrayList<Venue> venues;
        public ArrayList<Attraction> attractions;
        public First first;
        public Next next;
        public Last last;
    }

    public class Location {
        public String longitude;
        public String latitude;
    }

    public class Market {
        public String name;
        public String id;
    }

    public class Musicbrainz {
        public String id;
    }

    public class Next {
        public String href;
    }

    public class Outlet {
        public String url;
        public String type;
    }

    public class Page {
        public int size;
        public int totalElements;
        public int totalPages;
        public int number;
    }

    public class Presale {
        public Date startDateTime;
        public Date endDateTime;
        public String name;
    }

    public class PriceRange {
        public String type;
        public String currency;
        public double min;
        public double max;
    }

    public class Product {
        public String name;
        public String id;
        public String url;
        public String type;
        public ArrayList<Classification> classifications;
    }

    public class Promoter {
        public String id;
        public String name;
        public String description;
    }

    public class Promoter2 {
        public String id;
        public String name;
        public String description;
    }

    public class Public {
        public Date startDateTime;
        public boolean startTBD;
        public boolean startTBA;
        public Date endDateTime;
    }

    public class SafeTix {
        public boolean enabled;
    }

    public class Sales {
        @SerializedName("public")
        public Public mypublic;
        public ArrayList<Presale> presales;
    }

    public class Seatmap {
        public String staticUrl;
    }

    public class Segment {
        public String id;
        public String name;
    }

    public class Self {
        public String href;
    }

    public class Social {
        public Twitter twitter;
    }

    public class Spotify {
        public String url;
    }

    public class Start {
        public String localDate;
        public String localTime;
        public Date dateTime;
        public boolean dateTBD;
        public boolean dateTBA;
        public boolean timeTBA;
        public boolean noSpecificTime;
    }

    public class State {
        public String name;
        public String stateCode;
    }

    public class Status {
        public String code;
    }

    public class SubGenre {
        public String id;
        public String name;
    }

    public class SubType {
        public String id;
        public String name;
    }

    public class Ticketing {
        public SafeTix safeTix;
    }

    public class TicketLimit {
        public String info;
    }

    public class Twitter {
        public String handle;
        public String url;
    }

    public class Type {
        public String id;
        public String name;
    }

    public class UpcomingEvents {
        public int _total;
        public int ticketmaster;
        public int _filtered;
        public int tmr;
        public int ticketweb;
    }

    public class Venue {
        public String href;
        public String name;
        public String type;
        public String id;
        public boolean test;
        public String url;
        public String locale;
        public double distance;
        public String units;
        public String postalCode;
        public String timezone;
        public City city;
        public State state;
        public Country country;
        public Address address;
        public Location location;
        public ArrayList<Market> markets;
        public ArrayList<Dma> dmas;
        public UpcomingEvents upcomingEvents;
        public Links _links;
        public ArrayList<Image> images;
        public Social social;
        public BoxOfficeInfo boxOfficeInfo;
        public String parkingDetail;
        public GeneralInfo generalInfo;
        public String accessibleSeatingDetail;
        public ArrayList<String> aliases;
    }

    public class Wiki {
        public String url;
    }

    public class Youtube {
        public String url;
    }
}

