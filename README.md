# **Electronic Shop Application**
### This project contains API's that are used for handling the basic functionalities for any e-shop. These functionalities include doing basic HTTP requests on entities such as products, users, carts and orders.

Project Installation: To install this project, simply clone the code and run the ElectronicShopApplication.kt class file. <br />

Once the project is running, you can visit this URL for the swagger API documentation`http://localhost:8000/swagger-ui.html`. <br />

### Business Logic: <br />

This project contains a collection of API's that allow managing an electronic shop. First of all, this project is compromised of 4 main entities: Product, Cart, User and Order. Users can take action on products such as create, delete, update and fetch products. Then, customers can create accounts from which automatically a cart is created and assigned to them. Each user only has 1 cart and the cart should have only one user associated with it. Next, customers can add products to their carts. Finally, when a user requests an order and this order goes through successfully, the cart is automatically emptied and the inventory for the products in the cart is decreased.

### API endpoints implemented in this project: <br />

1. Product:
    - /product/{id} : 
        - GET: gets a product with the given ID. In case the given ID was not found, an exception is thrown.
        - PUT: updates the product with the given ID in the path. In case the given ID was not found, an exception is thrown.
        - DELETE: deleted the product with the ID in the path. In case the given ID was not found, an exception is thrown.
    - /product :
        - GET: retrieves a list of all the products.
        - POST: creates a new product with the given data in the request body.

2. User:
    - /user :
      - GET: gets all users.
      - POST: creates a new user with the given data in the request body. Also when this API is hit, a new cart is automatically created for the user.
    - /user/{id} :
      - GET: gets the user with the given ID. In case the given ID was not found, an exception is thrown.
      - DELETE: deletes the user with the given ID. In case the given ID was not found, an exception is thrown.
      
3. Cart:
    - /cart/{userId}/{productId} :
      -  POST: adds the product with productID to the cart of the user with userID. An exception is thrown if the userId or the productId are not available.
    - /cart/{userId}:
        - GET: gets the carts for the userID provided. An exception is thrown if the userId is not available.

4. Order:
    - /order/{userId}:
      - POST: creates an order for user with userID. Cart is emptied after order being created successfully.