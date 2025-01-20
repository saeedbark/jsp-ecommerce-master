package cn.techtutorial.dao;

import cn.techtutorial.model.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    private Connection con;

    public CategoryDao(Connection con) {
        this.con = con;
    }

    // Fetch all categories
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try {
            String query = "SELECT * FROM categories";
            PreparedStatement pst = this.con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                categories.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }
}