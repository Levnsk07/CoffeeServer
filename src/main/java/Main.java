import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import database.entity.Food;
import database.entity.Order;
import database.repository.Datable;
import database.repository.SqlDataBase;
//import database.repository.StubData;

import controller.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class Main {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(25565), 0);

        Datable base = new SqlDataBase();
        ObjectMapper mapper = new ObjectMapper();

        server.createContext("/food/", new FoodRequest(base, mapper));
        server.createContext("/order/", new OrderRequest(base, mapper));

        server.setExecutor(Executors.newCachedThreadPool());        // work with client int thread
//    server.setExecutor(null);         // work with client one by one (in Queue)
        server.start();


    }
}

class Test{
    public static void main(String[] args) {

        SqlDataBase sql = SqlDataBase.connectToBD();
        sql.addOrder(new int[]{4});
        sql.addOrder(new int[]{1,2});
        Order[] some =  sql.getOrders();

        for(Order o : some){
            System.out.println(o);
        }

    }
}