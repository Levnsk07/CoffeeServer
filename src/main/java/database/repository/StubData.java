package database.repository;

import database.entity.Food;
import database.entity.Order;

import java.util.ArrayList;
import java.util.List;


//TIP это класс "затычка" Необходимо заменить на более подходящую базу данных (например sql)
public class StubData implements Datable {

    private ArrayList<Order> orders = new ArrayList<Order>();
    private ArrayList<Food> foods = new ArrayList<Food>();

    public StubData() { // Временно заполняем каким нибудь меню
        foods.add(new Food(foods.size(), "rice", 100));
        foods.add(new Food(foods.size(), "tee", 50));
        foods.add(new Food(foods.size(), "coffee", 75,50));
        foods.add(new Food(foods.size(), "lattes", 120));
    }

    @Override
    public Order[] getOrders() {
        return List.copyOf(orders).toArray(new Order[0]);
    }

    @Override
    public Food[] getFoods() {
        return List.copyOf(foods).toArray(new Food[0]);
    }

    private int freeId = 0;
    @Override
    public Boolean addOrder(int[] id) {
        if (id.length == 0) return false;
        Food[] temp = new Food[id.length];
        int counter = 0;
        for (int i : id) {
            if (i >= foods.size() || i < 0) {
                return false;
            }
            temp[counter++] = foods.get(i).copy(); // now not linked
        }

        orders.add(new Order(freeId++, temp, 0));


        return true;
    }
}
