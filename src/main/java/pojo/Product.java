package pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Product {
    private int id;
    private String title;
    private String description;
    private double price;
    private double discountPercentage;
    private double rating;
    private int stock;
    private String brand;
    private String category;
    private String thumbnail;
    private List<String> images;
    private int quantity;
    private double total;
    private double discountedPrice;
}
