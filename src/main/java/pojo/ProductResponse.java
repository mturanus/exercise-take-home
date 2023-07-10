package pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter @Setter
public class ProductResponse {
    private List<Product> products;
    private int total;
    private int skip;
    private int limit;

}
