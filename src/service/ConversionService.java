package service;

import api.ExchangeRateAPI;
import model.Conversion;
import util.JSONGenerator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConversionService {

    private final ExchangeRateAPI search;
    private final List<Conversion> conversionList;

    public ConversionService() {
        this.search = new ExchangeRateAPI();
        this.conversionList = new ArrayList<>();
    }

    public void pairConversion(String codeBase, String codeTarget, double baseAmount) {
        try {
            Conversion conversion = search.convertPairCurrencies(codeBase, codeTarget, baseAmount);

            if (conversion.getDateOfConversion() == null) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                conversion.setDateOfConversion(now.format(formatter));
            }
            showResultConversion(conversion);
        } catch (Exception e) {
            System.out.println("Error durante la operación: " + e.getMessage());
        }

    }

    public void showResultConversion(Conversion conversion) {
        if ("success".equalsIgnoreCase(conversion.getStatus())) {
            System.out.println("""
                    -------------------------------------------------------
                    La operación ha sido exitosa
                    -------------------------------------------------------
                    """ + conversion);
        } else {
            System.out.println("""
                    -------------------------------------------------------
                    Ha surgido un error en la operación
                    -------------------------------------------------------
                    """ + conversion.toStringError());
        }
        conversionList.add(conversion);
    }

    public void showConversionList() {
        StringBuilder message = new StringBuilder();
        int position = 1;

        if (conversionList.isEmpty()) {
            System.out.println("[Historial vacío] Aún no ha realizado ninguna operación.");
            return;
        }

        for (Conversion conversion : conversionList) {
            message.append("[Conversión n°").append(position).append("] ");

            if ("success".equalsIgnoreCase(conversion.getStatus())) {
                message.append(conversion);
            } else {
                message.append(conversion.toStringError());
            }
            message.append("\n");
            position++;
        }
        System.out.println(message.toString());
    }

    public void saveConversionHistoryToFile() {
        String currentDir = System.getProperty("user.dir"); //captura el directorio actual
        String folderPath = currentDir + "\\src\\util\\"; //capetas dentro de la estructura del proyecto
        String fileName = "conversion_history.json";
        String filePath = folderPath + fileName;
        JSONGenerator.saveConversionHistory(conversionList, filePath);
    }
}
