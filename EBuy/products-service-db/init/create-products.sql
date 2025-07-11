CREATE TABLE IF NOT EXISTS product_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    description TEXT,
    quantity INT NOT NULL,
    rating FLOAT,
    number_of_ratings INT NOT NULL DEFAULT 0,
    total_sum FLOAT NOT NULL DEFAULT 0.0
);

INSERT INTO product_entity (name, seller_id, price, description, quantity, rating, number_of_ratings, total_sum) VALUES
    ('Laptop', 2, 999.99, 'High performance laptop', 10, 0.0, 0, 0.0),
    ('Smartphone', 2, 499.99, 'Latest smartphone model', 50, 0.0, 0, 0.0),
    ('Headphones', 3, 199.99, 'Noise cancelling headphones', 25, 0.0, 0, 0.0),
    ('Gaming Mouse', 5, 59.99, 'Ergonomic gaming mouse with RGB lighting', 40, 0.0, 0, 0.0),
    ('Mechanical Keyboard', 1, 89.99, 'Backlit mechanical keyboard', 35, 0.0, 0, 0.0),
    ('4K Monitor', 6, 349.99, '27-inch 4K UHD monitor', 15, 0.0, 0, 0.0),
    ('External Hard Drive', 6, 120.00, '2TB portable external hard drive', 60, 0.0, 0, 0.0),
    ('Wireless Charger', 8, 29.99, 'Fast wireless charger for smartphones', 80, 0.0, 0, 0.0),
    ('Smartwatch', 4, 199.99, 'Waterproof smartwatch with fitness tracking', 45, 0.0, 0, 0.0),
    ('Bluetooth Speaker', 4, 79.99, 'Portable Bluetooth speaker with deep bass', 30, 0.0, 0, 0.0);
