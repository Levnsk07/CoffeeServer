import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import database.repository.StubData;

import controller.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.HttpServer;

public class Main {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(25565), 0);

        server.createContext("/food/", new FoodRequest(new StubData(), new ObjectMapper()));
        server.createContext("/order/", new OrderRequest(new StubData(), new ObjectMapper()));

        server.setExecutor(Executors.newCachedThreadPool());        // work with client int thread
//    server.setExecutor(null);         // work with client one by one (in Queue)
        server.start();

    }
}