package com.example.dinder.adapters.model;

import java.util.List;

public class Restaurant {
    private String id;
    private String alias;
    private String name;
    private String imageUrl;
    private boolean isClosed;
    private String url;
    private int reviewCount;
    private List<Category> categories;
    private double rating;
    private Coordinates coordinates;
    private List<String> transactions;
    private String price;
    private Location location;
    private String phone;
    private String displayPhone;
    private double distance;

    public Restaurant(int id, String name) {
        this.id = String.valueOf(id);
        this.name = name;
    }

    // Define inner classes for nested JSON objects:

    public static class Category {
        private String alias;
        private String title;
    }

    public static class Coordinates {
        private double latitude;
        private double longitude;
    }

    public static class Location {
        private String address1;
        private String address2;
        private String address3;
        private String city;
        private String zipCode;
        private String country;
        private String state;
        private List<String> displayAddress;
    }

    // Add your getters and setters for each field

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDisplayPhone() {
        return displayPhone;
    }

    public void setDisplayPhone(String displayPhone) {
        this.displayPhone = displayPhone;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
