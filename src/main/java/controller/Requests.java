package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.repository.Datable;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Deprecated(since = "2.0", forRemoval = true)
public class Requests implements HttpHandler {

    private final Datable dataBase;

    public Requests(Datable dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();

        System.out.println(httpExchange.getRequestMethod() + "\t\t" + path);

        if (httpExchange.getRequestMethod().equals("GET")) {
            String returnee = "";
            switch (path) {
                case "/getFood":
                    returnee = buildJson(dataBase.getFoods());
                    break;

                case "/getOrders":
                    returnee = buildJson(dataBase.getOrders());
                    break;

                default:
                    System.err.println("Error 404, Nothing to send");
                    httpExchange.sendResponseHeaders(404, -1);
                    httpExchange.close();
                    return;
            }

            byte[] response = returnee.getBytes(StandardCharsets.UTF_8);
            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            httpExchange.sendResponseHeaders(200, response.length);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response);
                os.close();
            }

        } else if (httpExchange.getRequestMethod().equals("POST")) {
            switch (path) {
                case "/addOrder":

                    String json = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(json);

                    int[] id = mapper.convertValue(root.get("id"), int[].class);

                    if (dataBase.addOrder(id)) {
                        httpExchange.sendResponseHeaders(200, -1);
                    } else {
                        httpExchange.sendResponseHeaders(400, -1);
                    }
                    break;

                default:
                    System.err.println("Error 502, bad request\n");
                    httpExchange.sendResponseHeaders(502, -1);
                    httpExchange.close();
                    return;
            }
        }


        httpExchange.close();
    }

    private String buildJson(Object[] arr) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(arr);
        return json;
    }
}
