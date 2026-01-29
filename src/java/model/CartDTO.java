package model;

public class CartDTO {
    private int cartID;
    private String userID;
    private ProductDTO product; // Chứa thông tin chi tiết sản phẩm để hiển thị
    private int quantity;

    public CartDTO() {}
    public CartDTO(int cartID, String userID, ProductDTO product, int quantity) {
        this.cartID = cartID;
        this.userID = userID;
        this.product = product;
        this.quantity = quantity;
    }
    // Getters and Setters...
    public ProductDTO getProduct() { return product; }
    public void setProduct(ProductDTO product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}