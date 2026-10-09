package database.repository;

import database.entity.Food;
import database.entity.Order;

public interface Datable {


    public Order[] getOrders();
    public Food[] getFoods();

    public Boolean addOrder(int[] ID);

}
