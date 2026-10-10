package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.repository.Datable;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * /food/getMenu
 * /food/addFood        (root key request)
 * /food/removeFood     (root key request)
 */

public class FoodRequest implements HttpHandler {

    private final Datable dataBase;
    private final ObjectMapper mapper;

    public FoodRequest(Datable dataBase, ObjectMapper mapper) {
        this.dataBase = dataBase;
        this.mapper = mapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String response = "";
        int size = -1;
        int result = 0;

        System.out.println(exchange.getRequestURI().getPath());

        if (exchange.getRequestMethod().equals("GET")) {
            result = switch (exchange.getRequestURI().getPath()) {
                case "/food/getMenu" -> {
                    response = mapper.writeValueAsString(dataBase.getFoods());
                    size = response.getBytes().length;
                    yield 200;
                }

                default -> {
                    size = -1;
                    yield 404;
                }
            };

        } else if (exchange.getRequestMethod().equals("POST")) {
            result = switch (exchange.getRequestURI().getPath()) {
                case "/food/addFood" -> {
                    size = -1;
                    yield 204;
                }

                case "/food/removeFood" -> {
                    size = -1;
                    yield 204;
                }
                default -> {
                    size = -1;
                    yield 502;
                }
            };

        }

        exchange.getResponseHeaders().

                add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(result, size);

        if (size > 0) {
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
                os.close();
            }
        }
        exchange.close();

    }
}
