<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Create Product</title>
    </head>
    <body>
        <h1>Add New Product</h1>
        <h3 style="color: red;">${requestScope.ERROR}</h3>

        <form action="MainController" method="POST">
            Name: <input type="text" name="name" value="${param.name}" required/><br/>
            Category: <input type="text" name="category" value="${param.category}" required/><br/>
            Price: <input type="number" step="any" name="price" value="${param.price}" required/><br/>
            Stock: <input type="number" name="stockQuantity" value="${param.stockQuantity}" required/><br/>

            <input type="submit" name="action" value="Create"/>
        </form>
    </body>
</html>