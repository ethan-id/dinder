package com.example.dinder.adapters.model;

import java.util.List;

/**
 * Represents a restaurant with various details such as name, categories, location, etc.
 */
public class Restaurant {
    /**
     * Unique Identifier for the Restaurant
     */
    private String id;
    /**
     * Restaurant's alias
     */
    private String alias;
    /**
     * Restaurant's name
     */
    private String name;
    /**
     * URL of an image used by the Restaurant
     */
    private String imageUrl;
    /**
     * Status describing whether the Restaurant is currently closed
     */
    private boolean isClosed;
    /**
     * URL to the Restaurant's Yelp page
     */
    private String url;
    /**
     * The count of reveiews the Restaurant has received
     */
    private int reviewCount;
    /**
     * The Restaurant's applicable categories
     */
    private List<Category> categories;
    /**
     * The average rating the Restaurant has received
     */
    private double rating;
    /**
     * The latitudinal and longitudinal coordinates of the Restaurant's location
     */
    private Coordinates coordinates;
    /**
     * A List of Strings containing transactions
     */
    private List<String> transactions;
    /**
     * $, $$, $$$. Describes the approximate average cost of the Restaurant's menu items
     */
    private String price;
    /**
     * An Object containing specific location details about the Restaurant
     */
    private Location location;
    /**
     * The Restaurant's phone number
     */
    private String phone;
    /**
     * A formatted version of the Restaurant's phone number to be used on the UI
     */
    private String displayPhone;
    /**
     * The distance the Restaurant is away from the User
     */
    private double distance;

    /**
     * Default constructor for creating an instance of Restaurant with no initial values.
     */
    public Restaurant() {}

    /**
     * Constructs a Restaurant with a specified ID and name.
     *
     * @param id   The unique identifier for the restaurant.
     * @param name The name of the restaurant.
     */
    public Restaurant(int id, String name) {
        this.id = String.valueOf(id);
        this.name = name;
    }

    /**
     * Represents a category of a restaurant.
     */
    public static class Category {
        /**
         * The Category's alian
         */
        private String alias;
        /**
         * The Category
         */
        private String title;

        /**
         * Constructs a Category with an alias and a title.
         *
         * @param alias The alias of the category.
         * @param title The title or name of the category.
         */
        public Category(String alias, String title) {
            this.alias = alias;
            this.title = title;
        }
    }

    /**
     * Represents geographical coordinates with latitude and longitude.
     */
    public static class Coordinates {
        /**
         * The Restaurant's latitude
         */
        private double latitude;
        /**
         * The Restaurant's longitude
         */
        private double longitude;

        /**
         * Constructs Coordinates with specified latitude and longitude.
         *
         * @param latitude  The latitude of the location.
         * @param longitude The longitude of the location.
         */
        public Coordinates(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    /**
     * Represents a location with address details and geographical information.
     */
    public static class Location {
        /**
         * The first address line for the Restaurant's location
         */
        private String address1;
        /**
         * The second address line for the Restaurant's location
         */
        private String address2;
        /**
         * The third address line for the Restaurant's location
         */
        private String address3;
        /**
         * The city the Restaurant is located in
         */
        private String city;
        /**
         * Restaurant's zip code
         */
        private String zipCode;
        /**
         * The country where the Restaurant is located within
         */
        private String country;
        /**
         * The state where the Restaurant is located within
         */
        private String state;
        /**
         * A List of Strings that are formatted to more easily display the Restaurant's address
         */
        private List<String> displayAddress;

        /**
         * Constructs a Location with detailed address information.
         *
         * @param address1        The primary street address.
         * @param address2        Additional address information, such as suite or apartment number.
         * @param address3        Further address details.
         * @param city            The city in which the restaurant is located.
         * @param zipCode         The postal code for the location.
         * @param country         The country of the restaurant.
         * @param state           The state or province of the restaurant.
         * @param displayAddress  A list of strings that form the complete displayable address.
         */
        public Location(String address1, String address2, String address3, String city, String zipCode, String country, String state, List<String> displayAddress) {
            this.address1 = address1;
            this.address2 = address2;
            this.address3 = address3;
            this.city = city;
            this.zipCode = zipCode;
            this.country = country;
            this.state = state;
            this.displayAddress = displayAddress;
        }
    }

    /**
     * Gets the unique identifier of the restaurant.
     *
     * @return The unique identifier of the restaurant.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the restaurant.
     *
     * @param id The unique identifier to set for the restaurant.
     */
    public void setId(String id) {
        this.id = id;
    }

    // ...

    /**
     * Gets the alias of the restaurant.
     *
     * @return The alias of the restaurant.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the alias of the restaurant.
     *
     * @param alias The alias to set for the restaurant.
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Gets the name of the restaurant.
     *
     * @return The name of the restaurant.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the restaurant.
     *
     * @param name The name to set for the restaurant.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the URL of the image representing the restaurant.
     *
     * @return The image URL of the restaurant.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the URL of the image representing the restaurant.
     *
     * @param imageUrl The image URL to set for the restaurant.
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Checks if the restaurant is closed.
     *
     * @return true if the restaurant is closed, otherwise false.
     */
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * Sets the restaurant's closed status.
     *
     * @param closed The closed status to set for the restaurant.
     */
    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    /**
     * Gets the URL of the restaurant's website.
     *
     * @return The URL of the restaurant.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL of the restaurant's website.
     *
     * @param url The website URL to set for the restaurant.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the number of reviews for the restaurant.
     *
     * @return The number of reviews.
     */
    public int getReviewCount() {
        return reviewCount;
    }

    /**
     * Sets the number of reviews for the restaurant.
     *
     * @param reviewCount The number of reviews to set.
     */
    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    /**
     * Gets the list of categories associated with the restaurant.
     *
     * @return The list of restaurant categories.
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Sets the list of categories associated with the restaurant.
     *
     * @param categories The list of categories to set for the restaurant.
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * Gets the rating of the restaurant.
     *
     * @return The rating of the restaurant.
     */
    public double getRating() {
        return rating;
    }

    /**
     * Sets the rating of the restaurant.
     *
     * @param rating The rating to set for the restaurant.
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Gets the geographical coordinates of the restaurant.
     *
     * @return The coordinates of the restaurant.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Sets the geographical coordinates of the restaurant.
     *
     * @param coordinates The coordinates to set for the restaurant.
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Gets the list of transaction types the restaurant supports.
     *
     * @return The list of transaction types.
     */
    public List<String> getTransactions() {
        return transactions;
    }

    /**
     * Sets the list of transaction types the restaurant supports.
     *
     * @param transactions The list of transaction types to set.
     */
    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    /**
     * Gets the price level of the restaurant.
     *
     * @return The price level of the restaurant.
     */
    public String getPrice() {
        return price;
    }

    /**
     * Sets the price level of the restaurant.
     *
     * @param price The price level to set for the restaurant.
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Gets the location of the restaurant including address and geographical information.
     *
     * @return The location of the restaurant.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location of the restaurant.
     *
     * @param location The location to set for the restaurant.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets the phone number of the restaurant.
     *
     * @return The phone number of the restaurant.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the restaurant.
     *
     * @param phone The phone number to set for the restaurant.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the display phone number of the restaurant.
     *
     * @return The display phone number of the restaurant.
     */
    public String getDisplayPhone() {
        return displayPhone;
    }

    /**
     * Sets the display phone number of the restaurant.
     *
     * @param displayPhone The display phone number to set for the restaurant.
     */
    public void setDisplayPhone(String displayPhone) {
        this.displayPhone = displayPhone;
    }

    /**
     * Gets the distance of the restaurant from a reference point.
     *
     * @return The distance of the restaurant.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Sets the distance of the restaurant from a reference point.
     *
     * @param distance The distance to set for the restaurant.
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }
}

