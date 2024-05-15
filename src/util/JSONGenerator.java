package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Conversion;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JSONGenerator {
    public static void saveConversionHistory(List<Conversion> conversionList, String filePath){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(conversionList);

        try(FileWriter writer = new FileWriter(filePath)){
            writer.write(json);
            System.out.println("""
                    -------------------------------------------------------
                    Historial guardado exitosamente
                    -------------------------------------------------------
                    """);
        }catch (IOException e){
            System.out.println("Error al guardar el historial: " + e.getMessage());
        }
    }
}
