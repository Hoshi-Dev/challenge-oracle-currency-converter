package model;
import java.util.Map;

public class Currency {
    String code;
    Map<String, String> conversionRates;
    String name;

    public Currency() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, String> getConversionRates() {
        return conversionRates;
    }

    public void setConversionRates(Map<String, String> conversionRates) {
        this.conversionRates = conversionRates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Code: " + code + " -> " + name + "\n";
    }

}
