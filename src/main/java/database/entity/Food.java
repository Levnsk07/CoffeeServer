package database.entity;

public class Food {

    private final int ID;
    private String name;
    private int price;

    private int sale;

    public int getSale() {
        return sale;
    }

    public int getFinalPrice(){
        return (int)(price - (price/100.0)*sale);
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public Food(int ID, String name, int price) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        sale = 0;
    }

    public Food(int ID, String name, int price, int sale) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.sale = sale;
    }

    @Override
    public String toString() {
        return "Food{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
//        return (int)(price - (price/100.0)*sale);
    }
}
