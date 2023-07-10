import app.AbstractTestMartAppFeatures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pojo.Cart;
import pojo.Product;

import java.util.ArrayList;
import java.util.List;

public class AbstractTestMartAppFeaturesTest {

    @Test
    public void testGetCartWithHighestTotal() {

        Cart cart1 = new Cart();
        cart1.setTotal(50.0);
        Cart cart2 = new Cart();
        cart2.setTotal(100.0);
        Cart cart3 = new Cart();
        cart3.setTotal(110.0);

        List<Cart> carts = new ArrayList<>();
        carts.add(cart1);
        carts.add(cart2);
        carts.add(cart3);

        AbstractTestMartAppFeatures testMartAppFeatures = new AbstractTestMartAppFeatures() {
            @Override
            public List<Cart> getAllCarts() {
                return carts;
            }
        };
        Cart highestCart = testMartAppFeatures.getCartWithHighestTotal();

        Assertions.assertEquals(cart3.getTotal(), highestCart.getTotal());
    }

    @Test
    public void testGetCartWithLowestTotal() {

        Cart cart1 = new Cart();
        cart1.setTotal(50.0);
        Cart cart2 = new Cart();
        cart2.setTotal(100.0);
        Cart cart3 = new Cart();
        cart3.setTotal(110.0);

        List<Cart> carts = new ArrayList<>();
        carts.add(cart1);
        carts.add(cart2);
        carts.add(cart3);

        AbstractTestMartAppFeatures testMartAppFeatures = new AbstractTestMartAppFeatures() {
            @Override
            public List<Cart> getAllCarts() {
                return carts;
            }
        };

        Cart lowestCart = testMartAppFeatures.getCartWithLowestTotal();

        Assertions.assertEquals(cart1.getTotal(), lowestCart.getTotal());
    }



    @Test
    public void testAddProductImagesToUserCart() {
        AbstractTestMartAppFeatures testMartAppFeatures = new AbstractTestMartAppFeatures();

        List<Product> enrichedProducts = testMartAppFeatures.addProductImagesToUserCart(1);
        boolean isImagesAdded = false;
        for(Product p : enrichedProducts){
            if (p.getImages() != null){
                isImagesAdded = true;
                break;
            }
        }
        Assertions.assertTrue(isImagesAdded);
    }
}
