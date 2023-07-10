package pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CartResponse {

        private List<Cart> carts;
        private int total;
        private int skip;
        private int limit;
}
