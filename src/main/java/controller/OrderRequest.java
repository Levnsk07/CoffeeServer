package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.repository.Datable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


/**
 * /order/getOrder
 * /order/addOrder
 * /order/changeStatus (auth key request)
 * /order/removeStatus (root key request)
 *
 */

public class OrderRequest implements HttpHandler {

    private final Datable dataBase;
    private final ObjectMapper mapper;

    public OrderRequest(Datable dataBase, ObjectMapper mapper) {
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
                case "/order/getOrder" -> {
                    response = mapper.writeValueAsString(dataBase.getOrders());
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
                case "/order/addOrder" -> {
                    String json = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                    int[] id = mapper.convertValue(mapper.readTree(json).get("id"), int[].class);
                    size = -1;
                    if (dataBase.addOrder(id)) {
                        yield 200;
                    } else {
                        yield 200;
                    }
                }
                case "/order/changeStatus" -> {
                    size = -1;
                    yield 204;
                }
                case "/order/removeStatus" -> {
                    size = -1;
                    yield 204;
                }
                default -> {
                    size = -1;
                    yield 502;
                }
            };

        }

        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
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
