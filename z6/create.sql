CREATE TABLE Products(
  name VARCHAR(50) PRIMARY KEY,
  description VARCHAR(300),
  amount INT
);
CREATE TABLE Categories(
  name VARCHAR(50),
  product VARCHAR(50),
  PRIMARY KEY(name,product),
  FOREIGN KEY (product) REFERENCES Products(name)
);
CREATE TABLE Users(
  name VARCHAR(50),
  product VARCHAR(50),
  amount INT,
  PRIMARY KEY(name,product),
  FOREIGN KEY (product) REFERENCES Products(name)
);
--Products lists products available in the marketplace/in checkout. amount is the number in marketplace
--Users contain usernames and products in that user's checkout
