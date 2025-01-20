<%@page import="cn.techtutorial.connection.DbCon"%>
<%@page import="cn.techtutorial.dao.ProductDao"%>
<%@page import="cn.techtutorial.dao.CategoryDao"%>
<%@page import="cn.techtutorial.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.lang.ClassNotFoundException"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%
User auth = (User) request.getSession().getAttribute("auth");
if (auth != null) {
    request.setAttribute("person", auth);
}

ProductDao productDao = null;
CategoryDao categoryDao = null;
List<Product> products = new ArrayList<>();
List<Category> categories = new ArrayList<>();
int selectedCategoryId = 0;

try {
    // Get database connection and initialize DAOs
    productDao = new ProductDao(DbCon.getConnection());
    categoryDao = new CategoryDao(DbCon.getConnection());

    // Get the selected category ID from the query parameter
    String categoryParam = request.getParameter("category");
    if (categoryParam != null && !categoryParam.isEmpty()) {
        selectedCategoryId = Integer.parseInt(categoryParam);
    }

    // Fetch products based on the selected category
    if (selectedCategoryId > 0) {
        products = productDao.getProductsByCategoryId(selectedCategoryId);
    } else {
        products = productDao.getAllProducts(); // Show all products if no category is selected
    }

    // Fetch all categories for the dropdown
    categories = categoryDao.getAllCategories();
} catch (ClassNotFoundException e) {
    e.printStackTrace();
    out.println("Error: MySQL JDBC driver not found. Make sure the driver is in the classpath.");
} catch (SQLException e) {
    e.printStackTrace();
    out.println("Error: Unable to connect to the database. Check your database settings.");
} catch (Exception e) {
    e.printStackTrace();
    out.println("Error: An unexpected error occurred. Details: " + e.getMessage());
}
%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/includes/head.jsp"%>
<title>E-Commerce Cart</title>
</head>
<body>
    <%@include file="/includes/navbar.jsp"%>

    <div class="container">
        <!-- Category Filter Form -->
        <div class="card-header my-3 d-flex justify-content-between align-items-center">
            <form action="index.jsp" method="get" class="form-inline">
                <div class="form-group">
                    <label for="categoryFilter" class="mr-2">Filter by Category:</label>
                    <select class="form-control" id="categoryFilter" name="category" onchange="this.form.submit()">
                        <option value="0">All Categories</option>
                        <%
                        for (Category category : categories) {
                        %>
                        <option value="<%=category.getId()%>" <%=selectedCategoryId == category.getId() ? "selected" : ""%>>
                            <%=category.getName()%>
                        </option>
                        <%
                        }
                        %>
                    </select>
                </div>
            </form>
        </div>

        <!-- Product List -->
        <div class="row">
            <%
            if (!products.isEmpty()) {
                for (Product p : products) {
            %>
            <div class="col-md-3 my-3">
                <div class="card w-100 shadow-sm">
                    <img class="card-img-top" src="product-image/<%=p.getImage() %>" alt="<%=p.getName() %>">
                    <div class="card-body">
                        <h5 class="card-title font-weight-bold"><%=p.getName() %></h5>
                        <h6 class="price text-success">$<%=p.getPrice() %></h6>
                        <h6 class="category text-muted">Category: <%=p.getCategory().getName() %></h6>
                        <div class="mt-3 d-flex justify-content-between align-items-center">
                            <a class="btn btn-outline-dark btn-sm" href="add-to-cart?id=<%=p.getId()%>">Add to Cart</a>
                            <a class="btn btn-primary btn-sm" href="order-now?quantity=1&id=<%=p.getId()%>">Buy Now</a>
                             <a href="toggle-favorite?id=<%=p.getId()%>">
                    <%
                    List<Integer> favorites = (List<Integer>) session.getAttribute("favorites");
                    boolean isFavorite = favorites != null && favorites.contains(p.getId());
                    %>
                    <i class="fas fa-heart" style="color: <%=isFavorite ? "red" : "grey"%>;"></i>
                </a>
                        </div>
                    </div>
                </div>
            </div>
            <%
                }
            } else {
                out.println("No products found.");
            }
            %>
        </div>
    </div>

    <%@include file="/includes/footer.jsp"%>
</body>
</html>