<%@page import="cn.techtutorial.connection.DbCon"%>
<%@page import="cn.techtutorial.dao.ProductDao"%>
<%@page import="cn.techtutorial.model.*"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%
User auth = (User) request.getSession().getAttribute("auth");
if (auth != null) {
    request.setAttribute("person", auth);
}

ProductDao productDao = new ProductDao(DbCon.getConnection());
List<Product> allProducts = productDao.getAllProducts();
List<Integer> favorites = (List<Integer>) session.getAttribute("favorites");

List<Product> favoriteProducts = new ArrayList<>();
if (favorites != null) {
    for (Product p : allProducts) {
        if (favorites.contains(p.getId())) {
            favoriteProducts.add(p);
        }
    }
}
%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/includes/head.jsp"%>
<title>Favorites</title>
</head>
<body>
    <%@include file="/includes/navbar.jsp"%>

    <div class="container">
        <div class="card-header my-3">Favorite Products</div>
        <div class="row">
            <%
            if (!favoriteProducts.isEmpty()) {
                for (Product p : favoriteProducts) {
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
                                <i class="fas fa-heart" style="color: red;"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            <%
                }
            } else {
                out.println("No favorite products found.");
            }
            %>
        </div>
    </div>

    <%@include file="/includes/footer.jsp"%>
</body>
</html>