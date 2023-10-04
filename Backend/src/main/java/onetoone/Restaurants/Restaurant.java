package onetoone.Restaurants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 *
 * @author Vivek Bengre
 *
 */

@Entity
public class Restaurant {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int count;

    private int id;
    private String name;
    private String code;
    private String rating;
    private String review_count;
    private String image_url;
    private String url;
    private String is_closed;
    private String latitude;
    private String longitude;
    private String price;
    private String address;
    private String distance;
    private String phone_number;

    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * cascade is responsible propagating all changes, even to children of the class Eg: changes made to laptop within a user object will be reflected
     * in the database (more info : https://www.baeldung.com/jpa-cascade-types)
     * @JoinColumn defines the ownership of the foreign key i.e. the user table will have a field called laptop_id
     */

    public Restaurant(int count, String name, String code, String is_closed, String rating, String review_count, String image_url,
                      String url, String price, String longitude, String latitude, String address, String phone_number,
                      String distance) {
        this.count = count;
        this.name = name;
        this.code = code;
        this.is_closed = is_closed;
        this.rating = rating;
        this.review_count = review_count;
        this.image_url = image_url;
        this.url = url;
        this.price = price;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.phone_number = phone_number;
        this.distance = distance;

    }

    public Restaurant() {
    }

    // =============================== Getters and Setters for each field ================================== //

    public String get_name() {
        return this.name;
    }

    public String get_code() {
        return this.code;
    }

    public String get_is_closed() {return this.is_closed; }

    public String get_rating() { return this.rating; }

    public String get_review_count() {
        return this.review_count;
    }

    public String get_image_url() {
        return this.image_url;
    }

    public String get_url() {
        return this.url;
    }

    public String get_price() {
        return this.price;
    }

    public String get_longitude() {
        return this.longitude;
    }

    public String get_latitude() {
        return this.latitude;
    }
    public String get_address() {
        return this.address;
    }
    public String get_phone_number() {
        return this.phone_number;
    }
    public String get_distance() {
        return this.distance;
    }

    /**
     * Setters for restaurant class
     */

    public void set_count(int count) {
        this.count = count;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public void set_code(String code) {
        this.code = code;
    }

    public void set_is_closed(String is_closed) {this.is_closed = is_closed; }

    public void set_rating(String rating) {
        this.rating = rating;
    }

    public void set_review_count(String review_count) {
        this.review_count = review_count;
    }

    public void set_image_url(String image_url) {
        this.image_url = image_url;
    }

    public void set_url(String url) {
        this.url = url;
    }

    public void set_price(String price) {
        this.price = price;
    }

    public void set_longitude(String longitude) {
        this.longitude = longitude;
    }

    public void set_latitude(String latitude) {
        this.latitude = latitude;
    }
    public void set_address(String address) {
        this.address = address;
    }
    public void set_phone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    public void set_distance(String distance) {
        this.distance = distance;
    }

    public String printValues() {
        return (count + "" + name + ", " + code + ", " + is_closed +", " + rating +", " + review_count +", " + url +", " + image_url +", " + price +", " + longitude +
                ", " + latitude +", " + address +", " + phone_number +", " + distance);
    }

}
