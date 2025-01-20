package cn.techtutorial.dao;

import java.sql.*;
import java.util.*;

import cn.techtutorial.model.Cart;
import cn.techtutorial.model.Product;
import cn.techtutorial.model.Category;

public class ProductDao {
    private Connection con;

    private String query;
    private PreparedStatement pst;
    private ResultSet rs;

    public ProductDao(Connection con) {
        this.con = con;
    }

    // Fetch all products with their category names
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try {
            // Join products and categories tables to get category name
            String query = "SELECT p.*, c.name AS category_name FROM products p JOIN categories c ON p.category_id = c.id";
            PreparedStatement pst = this.con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setImage(rs.getString("image"));
                product.setCategoryId(rs.getInt("category_id"));

                // Set the category object
                Category category = new Category();
                category.setId(rs.getInt("category_id"));
                category.setName(rs.getString("category_name"));
                product.setCategory(category);

                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    // Fetch products by category ID
    public List<Product> getProductsByCategoryId(int categoryId) {
        List<Product> products = new ArrayList<>();
        try {
            // Join products and categories tables to get category name
            String query = "SELECT p.*, c.name AS category_name FROM products p JOIN categories c ON p.category_id = c.id WHERE p.category_id = ?";
            PreparedStatement pst = this.con.prepareStatement(query);
            pst.setInt(1, categoryId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setImage(rs.getString("image"));
                product.setCategoryId(rs.getInt("category_id"));

                // Set the category object
                Category category = new Category();
                category.setId(rs.getInt("category_id"));
                category.setName(rs.getString("category_name"));
                product.setCategory(category);

                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    // Fetch a single product by ID with its category name
    public Product getSingleProduct(int id) {
        Product product = null;
        try {
            // Join products and categories tables to get category name
            query = "SELECT p.*, c.name AS category_name FROM products p JOIN categories c ON p.category_id = c.id WHERE p.id = ?";
            pst = this.con.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setImage(rs.getString("image"));
                product.setCategoryId(rs.getInt("category_id"));

                // Set the category object
                Category category = new Category();
                category.setId(rs.getInt("category_id"));
                category.setName(rs.getString("category_name"));
                product.setCategory(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return product;
    }

    // Calculate the total price of items in the cart
    public double getTotalCartPrice(ArrayList<Cart> cartList) {
        double sum = 0;
        try {
            if (cartList.size() > 0) {
                for (Cart item : cartList) {
                    query = "SELECT price FROM products WHERE id = ?";
                    pst = this.con.prepareStatement(query);
                    pst.setInt(1, item.getId());
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        sum += rs.getDouble("price") * item.getQuantity();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return sum;
    }

    // Fetch cart products with their category names
    public List<Cart> getCartProducts(ArrayList<Cart> cartList) {
        List<Cart> cartProducts = new ArrayList<>();
        try {
            if (cartList.size() > 0) {
                for (Cart item : cartList) {
                    // Join products and categories tables to get category name
                    query = "SELECT p.*, c.name AS category_name FROM products p JOIN categories c ON p.category_id = c.id WHERE p.id = ?";
                    pst = this.con.prepareStatement(query);
                    pst.setInt(1, item.getId());
                    rs = pst.executeQuery();

                    if (rs.next()) {
                        Cart cartItem = new Cart();
                        cartItem.setId(rs.getInt("id"));
                        cartItem.setName(rs.getString("name"));
                        cartItem.setPrice(rs.getDouble("price") * item.getQuantity());
                        cartItem.setQuantity(item.getQuantity());

                        // Set the category object
                        Category category = new Category();
                        category.setId(rs.getInt("category_id"));
                        category.setName(rs.getString("category_name"));
                        cartItem.setCategory(category);

                        cartProducts.add(cartItem);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return cartProducts;
    }
}