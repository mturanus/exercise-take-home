package pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Cart {

    private int id;
    private List<Product> products;
    private double total;
    private double discountedTotal;
    private int userId;
    private int totalProducts;
    private int totalQuantity;


}
