package app;

import io.restassured.response.Response;
import model.contract.CartService;
import model.contract.ProductService;
import model.contract.UserService;
import pojo.Cart;
import pojo.CartResponse;
import pojo.Product;
import pojo.ProductResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

// Note: Convert this class to concrete class and add implementation (missing body) to all methods. You will remove the word
// `abstract` everywhere. This class is only kept `abstract` for the sake of interview exercise.
public class AbstractTestMartAppFeatures implements CartService<Cart>, ProductService<Product, String>, UserService<Object> {

	private static final String BASE_URL = "https://dummyjson.com";
	private static Logger log = Logger.getLogger(AbstractTestMartAppFeatures.class.getName());

	static {
		baseURI = BASE_URL;
	}

	private Response makeRequest(String endpoint) {
		log.info("Creating request to: " + BASE_URL + endpoint);
		Response response = given().get(endpoint);
		if (response.getStatusCode() != 200) {
			log.severe("HTTP Error Code: " + response.getStatusCode());
			throw new RuntimeException("Error code: " + response.getStatusCode());
		}
		return response;
	}

	@Override
	public List<Cart> getAllCarts() {
		Response response = makeRequest("/carts");
		CartResponse cartResponse = response.as(CartResponse.class);
		return cartResponse.getCarts();
	}

	//1
	@Override
	public Cart getCart(Integer cartId) {
		Response response = makeRequest("/carts/" + cartId);
		return response.as(Cart.class);
	}

	@Override
	public List<Cart> getUserCarts(Integer userId) {
		Response response = makeRequest("/carts/user/" + userId);
		CartResponse cartResponse = response.as(CartResponse.class);
		return cartResponse.getCarts();
	}

	@Override
	public List<Product> getAllProducts() {
		Response response = makeRequest("/products");
		ProductResponse productResponse = response.as(ProductResponse.class);
		return productResponse.getProducts();
	}

	//2
	@Override
	public List<Product> getAllProducts(int limit, int skip, String... fields) {

		StringBuilder endpointBuilder = new StringBuilder("/products");
		endpointBuilder.append("?limit=").append(limit);
		endpointBuilder.append("&skip=").append(skip);
		endpointBuilder.append("&select=").append(String.join(",", fields));
		Response response = makeRequest(endpointBuilder.toString());
		ProductResponse productResponse = response.as(ProductResponse.class);

		return productResponse.getProducts();
	}

	//3
	@Override
	public Product getProduct(Integer productId) {
		Response response = makeRequest("/products/" + productId);
		return response.as(Product.class);
	}

	//4
	@Override
	public List<Product> searchProducts(String query) {
		Response response = makeRequest("/products/search?q=" + query);
		ProductResponse productResponse = response.as(ProductResponse.class);
		return productResponse.getProducts();
	}

	//5
	@Override
	public List<String> getCategories() {
		Response response = makeRequest("/products/categories");
		return response.jsonPath().getList(".", String.class);
	}

	//6
	@Override
	public List<Product> getProductsByCategory(String categoryName) {
		Response response = makeRequest("/products/category/" + categoryName);
		ProductResponse productResponse = response.as(ProductResponse.class);
		return productResponse.getProducts();
	}

	//7
	@Override
	public List<Object> getAllUsers() {
		Response response = makeRequest("/users");
		return response.jsonPath().getList(".", Object.class);
	}

	//8
	@Override
	public Object getUser(Integer userId) {
		Response response = makeRequest("/users/" + userId);
		return response.as(Object.class);
	}

	//9
	@Override
	public List<Object> searchUsers(String query) {
		Response response = makeRequest("/users/search?q=" +query);
		return response.jsonPath().getList(".", Object.class);
	}

	/**
	 * Prints the titles of all products that have a rating less than or equal to the provided criteria.
	 *
	 * @param rating The rating threshold.
	 */
	public void getProductTitlesByWorseRating(double rating) {
		log.info("Get the product with equals to or less the rating: " + rating);
		List<Product> products = getAllProducts();
		for (Product product : products) {
			double productRating = product.getRating();
			if (productRating <= rating) {
				System.out.println(product.getTitle());
			}
		}
	}

	/**
	 * Returns the cart with the highest total value.
	 *
	 * @returns The cart with the highest total value.
	 */
	public Cart getCartWithHighestTotal() {
		log.info("Cart with Highest total value");
		List<Cart> carts = getAllCarts();

		Cart highestCart = null;
		double highestTotal = Double.MIN_VALUE;
		for (Cart cart : carts) {
			if (cart.getTotal() > highestTotal) {
				highestCart = cart;
				highestTotal = cart.getTotal();
			}
		}

		if (highestCart == null) {
			log.severe("There is no cart.");
			throw new RuntimeException("There is no cart.");
		}

		return highestCart;
	}

	/**
	 * Returns the cart with the lowest total value.
	 *
	 * @returns The cart with the lowest total value.
	 */
	public Cart getCartWithLowestTotal() {
		log.info("Cart with the Lowest Total value");
		List<Cart> carts = getAllCarts();

		Cart lowestCart = null;
		double lowestTotal = Double.MAX_VALUE;
		for (Cart cart : carts) {
			if (cart.getTotal() < lowestTotal) {
				lowestCart = cart;
				lowestTotal = cart.getTotal();
			}
		}

		if (lowestCart == null) {
			log.severe("There is no cart.");
			throw new RuntimeException("There is no cart.");
		}

		return lowestCart;
	}

	/**
	 * Enriches the product information in a user's cart by adding product images.
	 * The current product information in a cart has limited fields.
	 * This method adds the `images` field for each product in a given user's cart.
	 * Note: This method only applies to the first element from the `carts[]` JSON response.
	 *
	 * @param userId The ID of the user whose cart's product information will be enriched.
	 * @returns A list of products with enriched information in the user's cart.
	 */
	public List<Product> addProductImagesToUserCart(Integer userId) {
		log.info("Add product image: " + userId);
		List<Product> products = getAllProducts();

		List<Cart> userCarts = getUserCarts(userId);
		if (userCarts.isEmpty()) {
			log.severe("There is no cart for the user");
			throw new RuntimeException("There is no cart for the user");
		}

		Cart userCart = userCarts.get(0);
		List<Product> userProducts = userCart.getProducts();
		List<Product> enrichedProductInfo = new ArrayList<>();

		for (Product userProduct : userProducts) {
			int productId = userProduct.getId();
			for (Product moreProductInfo : products) {
				if (moreProductInfo.getId() == productId) {
					log.info("Adding images: " + productId);
					userProduct.setImages(moreProductInfo.getImages());
				}
			}
			enrichedProductInfo.add(userProduct);
		}
		return enrichedProductInfo;
	}


}
