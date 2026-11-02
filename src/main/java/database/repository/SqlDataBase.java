package database.repository;

import database.entity.Food;
import database.entity.Order;

import java.sql.*;
import java.util.Arrays;

public class SqlDataBase implements Datable {
    private String url = "jdbc:sqlite:src/main/java/database/repository/dataBaseFile.db";
    private static SqlDataBase DB = null;
    private Connection connection = null;

    public SqlDataBase() {
        try {
            connection = DriverManager.getConnection(url);
            connection.createStatement().executeUpdate("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /// pattern "Once"
    public static SqlDataBase connectToBD() {
        if (DB == null) DB = new SqlDataBase();
        return DB;
    }

    private enum Tables {
        menu,
        orders,
        foodsOrder
    }

    @Override
    public Food[] getFoods() {
        final String sql = "select ID,name,price,discount from menu where status != 0";
        ResultSet rs;
        Food[] foods;

        try {
            rs = connection.createStatement().executeQuery(sql);
            foods = new Food[getSize(Tables.menu)];
            int i = 0;

            while (rs.next()) {
                Food tempFood = exsternToFood(rs);
                foods[i++] = tempFood;
            }

        } catch (SQLException ex) {
            System.err.println("Error in sql execute");
            throw new RuntimeException(ex);
        }
        return foods;

    }

    @Override
    public Order[] getOrders() {
        int count = getSize(Tables.orders);

        if (count == 0) return null; // TODO exception?

        // TODO fuck --> new class {foodOrder} | new layer?
        int[] ordersID = new int[count];
        int[] orderStatus = new int[count];

        String sql = "select * from orders";
        try (ResultSet rs = connection.createStatement().executeQuery(sql)) {
            System.out.println("Count: " + count);

            for (int i = 0; i < count; i++) {
                rs.next();

                ordersID[i] = rs.getInt("ID");
                orderStatus[i] = rs.getInt("status");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // -----------------------------

        sql = "Select * from foodsOrder where orderID = ?";
        Order[] orders = new Order[count];

        count = 0;
        try {

            for (int id : ordersID) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                int tempCount = getSize(Tables.foodsOrder, id);
                Food[] tempFood = new Food[tempCount];
                for (int i = 0; i < tempCount; i++) {
                    tempFood[i] = new Food(rs.getInt("menuID"), rs.getString("menuName"), rs.getInt("menuPrice"), rs.getInt("menuDiscount"));
                    rs.next();
                }
                orders[count] = new Order(ordersID[count], tempFood.clone(), orderStatus[count]);
                count++;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return orders;
    }

    @Override
    public Boolean addOrder(int[] id) {

        //  Get from ID[] @link(Food) object
        Food[] foodsOrder = new Food[id.length];
        String sql = "Select * from menu where id=?";
        ResultSet rs;
        int counter = 0;
        for (int i : id) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, i);
                rs = preparedStatement.executeQuery();
//                System.out.println(rs.getInt("price"));
//                rs.next();
//                System.out.println(rs.getInt("price"));

                if (exsternToFood(rs) == null) {
                    return false;
                }
                foodsOrder[counter++] = exsternToFood(rs);
            } catch (SQLClientInfoException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //     Create Center Order and  get ID
        sql = "INSERT INTO orders (status) VALUES (?)";
        long newOrderID;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, 0);
            preparedStatement.executeUpdate();
            ResultSet temp = preparedStatement.getGeneratedKeys();
            temp.next();
            newOrderID = temp.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //      Final adding food to Orders
        sql = "INSERT INTO foodsOrder (orderID,menuID,menuName,menuPrice,menuDiscount) VALUES (?,?,?,?,?)";
        for (Food f : foodsOrder) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, newOrderID);
                preparedStatement.setLong(2, f.getID());
                preparedStatement.setString(3, f.getName());
                preparedStatement.setInt(4, f.getPrice());
                preparedStatement.setInt(5, f.getDiscount());

                preparedStatement.executeLargeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }

        return null;
    }

    @Override
    public boolean removeOrder(int ID) {
        // TODO make check for valid data
        String sql = "DELETE FROM orders where ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, ID);

            preparedStatement.executeLargeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean changeOrderStatus(int ID) {
        String sql = "select status from orders where id = " + ID;
        int status;
        try (ResultSet rs = connection.createStatement().executeQuery(sql)) {
            rs.next();
            status = rs.getInt("status") + 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (status > 4) {
            removeOrder(ID);
            return true;
        }

        sql = "UPDATE orders set status=status+1 WHERE id = " + ID;

        return false;
    }


    // ==============================================================================


    private Food exsternToFood(ResultSet rs) throws SQLException {
        if ((rs.getInt("ID") == 0) && (rs.getInt("name") == 0) && (rs.getInt("price") == 0) && (rs.getInt("discount") == 0)) {
            return null;
        }
        Food temp = new Food(rs.getInt("ID"), rs.getString("name"), rs.getInt("price"), rs.getInt("discount"));
        return temp;
    }

    private int getSize(Tables t) {
        ResultSet rs;
        StringBuilder sql = new StringBuilder("SELECT Count(*) from ");
        String temp = switch (t) { //      TODO: add for all type of tables
            case menu -> {
                yield "menu WHERE status != 0";
            }
            case orders -> {

                yield "orders";
            }
            default -> {
                yield "";
            }
        };
        sql.append(temp);

        System.out.println("Command: " + sql);

        try {
            rs = connection.createStatement().executeQuery(sql.toString());
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getSize(Tables t, int more) {
        ResultSet rs;
        StringBuilder sql = new StringBuilder("SELECT Count(*) from ");
        String temp = switch (t) { //      TODO: add for all type of tables
            case menu -> {
                yield "menu WHERE status != 0";
            }
            case orders -> {
                yield "orders where status = " + more;
            }
            case foodsOrder -> {
                yield "foodsOrder where ID = " + more;
            }
            default -> {
                yield "";
            }
        };
        sql.append(temp);

        System.out.println("Command: " + sql);

        try {
            rs = connection.createStatement().executeQuery(sql.toString());
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

