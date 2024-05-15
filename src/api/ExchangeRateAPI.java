package api;

import model.Conversion;
import util.JSONParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;


public class ExchangeRateAPI {
    private static final String API_KEY= "d595dc9abda9286846d79b24";


    /**
     * Consulta para realizar la conversion de dos monedas y devolver el resultado ya hecho.
     * Recibe tres parámetros para realizar la conversión.
     * @return Un objeto Conversion.
     */
    public Conversion convertPairCurrencies(String codeBase, String codeTarget, double amount) {
        URI urlString = URI.create("https://v6.exchangerate-api.com/v6/"
                + API_KEY
                + "/pair/"
                + codeBase + "/"
                + codeTarget + "/"
                + amount);

        String json = connectToAPI(urlString);
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parsePairConversionJson(json, codeBase, codeTarget, amount);
    }

    /**
     * Consulta para tasa de conversión por moneda ingresada por usuario.
     * @return Una lista de objetos Json.
     */
    public Map<String, String> conversionRate(String codeBase)  {
        URI urlString = URI.create("https://v6.exchangerate-api.com/v6/"
                + API_KEY
                + "/latest/"
                + codeBase);

        String json = connectToAPI(urlString);
        JSONParser jsonParser = new JSONParser();
        String searchField = "conversion_rates";
        return jsonParser.jsonToList(json, searchField);
    }

    /**
     * Consulta para monedas disponibles y su nombre por nacionalidad.
     * @return Una lista de listas en formato Json.
     */
    public Map<String, String> supportedCodes() {

        URI urlString = URI.create("https://v6.exchangerate-api.com/v6/"
                + API_KEY
                + "/codes");

        String json = connectToAPI(urlString);
        JSONParser jsonParser = new JSONParser();
        String searchField = "supported_codes";
        return jsonParser.jsonToList(json, searchField);
    }

    /**
     * Conexión a la api que recibe diferentes tipos de consulta.
     * @return El String de la respuesta.
     */
    public String connectToAPI(URI urlString) {

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(urlString)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al conectarse a la API: "+ e.getMessage());
        }
    }

}
