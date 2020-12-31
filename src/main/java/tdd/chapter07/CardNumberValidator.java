package tdd.chapter07;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CardNumberValidator {
    public CardValidity validate(String cardNumber) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                                    .uri(URI.create("https://xxx.xx.xxx/card"))
                                    .header("Content-Type", "text/plain")
                                    .POST(HttpRequest.BodyPublishers.ofString(cardNumber))
                                    .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            switch (response.body()) {
                case "OK":
                    return CardValidity.VALID;
                case "BAD":
                    return CardValidity.INVALID;
                case "EXPIRED":
                    return CardValidity.EXPIRED;
                case "THEFT":
                    return CardValidity.THEFT;
                default:
                    return CardValidity.UNKNOWN;
            }
        } catch (IOException | InterruptedException e) {
            return CardValidity.ERROR;
        }
    }
}
