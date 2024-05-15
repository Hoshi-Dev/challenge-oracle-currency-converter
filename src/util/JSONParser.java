package util;

import com.google.gson.*;

import model.Conversion;

import java.util.HashMap;
import java.util.Map;


public class JSONParser {
    private final Map<String, String> jsonMap;

    public JSONParser() {
        this.jsonMap = new HashMap<>();
    }

    /**
     * Crea una lista verificando, según el searchField, qué tipo de consulta se realizó a la API,
     * y llama a los métodos correspondientes, ya que las respuestas contienen formatos distintos.
     *
     * @return Una lista con los datos pedidos desde la API.
     */
    public Map<String, String> jsonToList(String json, String searchField) {

        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        String status = verifyResult(jsonObject);

        if (status.equalsIgnoreCase("success")) {
            if (searchField.equalsIgnoreCase("supported_codes")) {
                supportedCodesArray(jsonObject.getAsJsonArray(searchField));
            }
            if (searchField.equalsIgnoreCase("conversion_rates")) {
                conversionRatesArray(jsonObject.get(searchField));
            }

        } else {
            String errorType = jsonObject.get("error-type").getAsString();
            System.out.println("Error -> " + errorType);
        }
        return jsonMap;
    }

    public void supportedCodesArray(JsonArray jsonArray) {
        for (JsonElement jsonElement : jsonArray) {
            JsonArray codeAndCountry = jsonElement.getAsJsonArray();
            String code = codeAndCountry.get(0).getAsString();
            String country = codeAndCountry.get(1).getAsString();
            jsonMap.put(code, country);
        }
    }

    public void conversionRatesArray(JsonElement jsonElement) {
        if (jsonElement != null && jsonElement.isJsonObject()) {
            JsonObject conversionObject = jsonElement.getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : conversionObject.entrySet()) {
                jsonMap.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
    }

    public String verifyResult(JsonObject jsonObject) {
        return jsonObject.get("result").getAsString();
    }

    /**
     * Maneja los datos que llegan de la consulta de conversión completa por pares.
     *
     * @return Un nuevo objeto Conversion con parámetros para asignar atributos.
     */
    public Conversion parsePairConversionJson(String json, String codeBase, String codeTarget, double baseAmount) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        String status = verifyResult(jsonObject);

        if (status.equalsIgnoreCase("success")) {
            double conversionRate = jsonObject.get("conversion_rate").getAsDouble();
            double convertedAmount = jsonObject.get("conversion_result").getAsDouble();
            return new Conversion(status, codeBase, codeTarget, baseAmount, conversionRate, convertedAmount);

        } else {
            String errorType = jsonObject.get("error-type").getAsString();
            return new Conversion(status, errorType);
        }
    }
}
