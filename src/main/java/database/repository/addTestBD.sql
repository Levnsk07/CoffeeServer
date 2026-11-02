#!/usr/bin/sqlite3 dataBaseFile.db < createDataBase.sql


INSERT INTO menu (name, price, discount, status) VALUES
('Margherita Pizza', 450, 10, 1),
('Pepperoni Pizza', 550, 15, 1),
('Classic Burger', 350, 0, 1),
('Cheeseburger', 400, 5, 1),
('French Fries', 180, 0, 1),
('Chicken Nuggets', 250, 10, 1),
('Caesar Salad', 320, 5, 1),
('Carbonara Pasta', 480, 0, 1),
('Tomato Soup', 220, 0, 1),
('Chicken Sandwich', 290, 10, 1),
('Cola', 120, 0, 1),
('Orange Juice', 150, 5, 1),
('Americano Coffee', 130, 0, 1),
('Cheesecake', 260, 15, 0),
('Ice Cream', 170, 0, 0);