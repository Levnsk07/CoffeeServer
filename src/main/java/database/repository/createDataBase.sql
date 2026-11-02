#!/usr/bin/sqlite3 dataBaseFile.db < createDataBase.sql


PRAGMA foreign_keys = ON;

DROP TABLE if EXISTS menu;
create table menu(
    ID INTEGER PRIMARY KEY NOT NULL,
    name TEXT not null,
    price INTEGER check (price > 0),
    discount INTEGER DEFAULT 0,
    status INTEGER DEFAULT 0
);


DROP TABLE if EXISTS orders;
create table orders(
    ID INTEGER PRIMARY KEY NOT NULL,
    status INTEGER DEFAULT 0 check(status >=0 AND status <=4)
);


DROP TABLE if EXISTS foodsOrder;
create table foodsOrder(
    ID INTEGER PRIMARY KEY NOT NULL,

    orderID INTEGER not null,


    menuID INTEGER not null,
    menuName TEXT not null,
    menuPrice INTEGER not null check (menuPrice > 0) ,
    menuDiscount INTEGER DEFAULT 0,
    FOREIGN KEY (orderID) REFERENCES orders(ID) ON DELETE CASCADE
);
