import com.sun.net.httpserver.HttpServer;
import controller.Requests;
import database.repository.StubData;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.HttpServer;

void main(String[] args) throws IOException {

    HttpServer server = HttpServer.create(new InetSocketAddress(25565), 0);

    server.createContext("/", new Requests(new StubData()));    // Подключение контроллера и базы данных

    server.setExecutor(Executors.newCachedThreadPool());        // work with client int thread
//    server.setExecutor(null);         // work with client one by one (in Queue)
    server.start();

}
