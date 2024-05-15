package model;

public class Conversion {
    private String status;
    private String dateOfConversion;
    private String codeBase;
    private String codeTarget;
    private double baseAmount;
    private double conversionRate;
    private double convertedAmount;

    public Conversion(String status, String codeBase, String codeTarget, double baseAmount, double conversionRate, double convertedAmount) {
        this.status = status;
        this.codeBase = codeBase;
        this.codeTarget = codeTarget;
        this.baseAmount = baseAmount;
        this.conversionRate = conversionRate;
        this.convertedAmount = convertedAmount;
    }

    public Conversion(String status, String erroType) {
        this.status = status + ": " + erroType;
    }

    public String getStatus() {
        return status;
    }

    public String getDateOfConversion() {
        return dateOfConversion;
    }

    public void setDateOfConversion(String dateOfConversion) {
        this.dateOfConversion = dateOfConversion;
    }

    @Override
    public String toString() {
        return "\nDate -> " + dateOfConversion +
                "\nBase Currency -> " + codeBase + " " + baseAmount +
                "\nTarget Currency -> " + codeTarget +
                "\nConversion Rate -> " + conversionRate +
                "\nConversion Result -> " + codeTarget + " " + convertedAmount + "\n";
    }

    public String toStringError() {
        return "\nStatus -> " + status +
                "\nDate -> " + dateOfConversion + "\n";
    }
}
