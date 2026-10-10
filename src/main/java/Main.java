import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import database.repository.Datable;
import database.repository.StubData;

import controller.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class Main {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(25565), 0);

        Datable base = new StubData();
        ObjectMapper mapper = new ObjectMapper();

        server.createContext("/food/", new FoodRequest(base, mapper));
        server.createContext("/order/", new OrderRequest(base, mapper));

        server.setExecutor(Executors.newCachedThreadPool());        // work with client int thread
//    server.setExecutor(null);         // work with client one by one (in Queue)
        server.start();

    }
}