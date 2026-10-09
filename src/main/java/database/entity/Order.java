package database.entity;

public class Order {

    private final int ID;
    private Food[] foods;
    private int status; // 0 - inQueue | 1 - inProgress | 2 - Done
    private int finalPrice;


    public Order(int ID, Food[] foods, int status) {
        this.ID = ID;
        this.foods = foods;
        this.status = status%4;

        for (Food f : foods) {
            finalPrice += f.getFinalPrice();
        }
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public int getID() {
        return ID;
    }

    public Food[] getFoods() {
        return foods;
    }

    public int getStatus() {
        return status;
    }
}
