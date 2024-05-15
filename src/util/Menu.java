package util;

import service.ConversionService;
import service.CurrencyService;


import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Menu {
    private final Scanner input;
    private double baseAmount;
    private final ConversionService conversionService;
    private final CurrencyService currencyService;
    //Uso de constantes para los mensajes repetidos y los menús
    private static final String MAIN_MENU = """
            -------------------------------------------------------
            Ingrese la opción que desea realizar:
            -------------------------------------------------------
            [1] Realizar una conversión.
            [2] Consultar historial de operaciones.
            [3] <-- Salir -->
            """;
    private static final String CONVERSION_MENU = """
            -------------------------------------------------------
            Las operaciones más realizadas:
            -------------------------------------------------------
            [1] ARS -> USD (Peso Argentino -> Dólar Estadounidense)
            [2] BRL -> USD (Real Brasileño -> Dólar Estadounidense)
            [3] USD -> MXN (Dólar Estadounidense -> Peso Mexicano)
            [4] USD -> EUR (Dólar Estadounidense -> Euro)
            [5] USD -> JPY (Dólar Estadounidense -> Yen Japonés)
            -------------------------------------------------------
            [6] Elegir otras monedas del listado completo
            [7] Conversión Personalizada
            -------------------------------------------------------
            """;
    private static final String THANK_YOU_MESSAGE = """
            -------------------------------------------------------
             $ ---> Gracias por utilizar nuestro servicio. <--- $
            -------------------------------------------------------
            """;
    private static final String INVALID_MESSAGE = """
            -------------------------------------------------------
            Ha ingresado una opción no valida.
            -------------------------------------------------------
            """;

    public Menu() {
        this.input = new Scanner(System.in);
        this.conversionService = new ConversionService();
        this.currencyService = new CurrencyService();
    }

    public void mainMenu() {
        int mainOption = 0;

        while (mainOption != 3) {
            System.out.println(MAIN_MENU);
            try {
                mainOption = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error: debe ingresar un número válido.");
                input.next();
                continue;
            }

            switch (mainOption) {
                case 1:
                    conversionMenu();
                    break;
                case 2:
                    conversionService.showConversionList();
                    saveConversionHistoryMenu();
                    break;
                case 3:
                    System.out.println(THANK_YOU_MESSAGE);
                    break;
                default:
                    System.out.println(INVALID_MESSAGE);
                    break;
            }
        }
    }

    public void conversionMenu() {
        int option;

        System.out.println(CONVERSION_MENU);

        try {
            option = input.nextInt();

            if (option != 6 && option != 7) {
                System.out.println("Ingrese el monto que desea convertir:");
                baseAmount = input.nextDouble();
            }

        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un número válido.");
            input.next(); // Limpiar la entrada inválida
            return;
        }

        switch (option) {
            case 1:
                convert("ARS","USD");
                break;
            case 2:
                convert("BRL", "USD");
                break;
            case 3:
                convert("USD", "MXN");
                break;
            case 4:
                convert("USD", "EUR");
                break;
            case 5:
                convert("USD", "JPY");
                break;
            case 6:
                currencyService.supportedCodes();

                System.out.println("\n¿Desea saber la tasa de conversión de alguna moneda de este listado? (y/n)");
                String response = input.next();

                if (response.equalsIgnoreCase("y")) {
                    System.out.println("""
                            -------------------------------------------------------
                            Indique la moneda que desea consultar:
                            """);

                    String codeBaseRates = input.next().toUpperCase();

                    if (currencyService.verifyCodeConversionRates(codeBaseRates)) {
                        currencyService.currencyRates(codeBaseRates);
                    } else {
                        System.out.println("No se ha encontrado la moneda ingresada.");
                    }
                }
                break;

            case 7:
                personalizedConversionMenu();
                break;

            default:
                System.out.println(INVALID_MESSAGE);
                break;
        }
    }

    private void convert(String codeBase, String codeTarget){
        pairConversionMenu(codeBase,codeTarget,baseAmount);
    }

    public void personalizedConversionMenu() {
        System.out.println("""
                -------------------------------------------------------
                Ingrese la moneda de origen:
                """);
        try {
            String codeBase = input.next();

            System.out.println("Ingrese la moneda destino:");
            String codeTarget = input.next();

            System.out.println("Ingrese el monto a convertir: ");
            baseAmount = input.nextDouble();
            pairConversionMenu(codeBase, codeTarget, baseAmount);

        } catch (InputMismatchException e) {
            System.out.println("Error: El monto ingresado no es un número válido.");
            input.next();
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("Error de lectura: " + e.getMessage());
        }
    }

    public void pairConversionMenu(String codeBase, String codeTarget, double baseAmount) {
        conversionService.pairConversion(codeBase, codeTarget, baseAmount);
    }

    public void saveConversionHistoryMenu(){
        System.out.println("¿Desea descargar el historial en un archivo? (y/n)");
        try{
            String response = input.next();

            if (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n")) {
                throw new IllegalArgumentException("La respuesta debe ser 'y' o 'n'");
            }

            if (response.equalsIgnoreCase("y")){
                conversionService.saveConversionHistoryToFile();
            }

        }catch (IllegalArgumentException e){
            System.out.println("Error: " + e.getMessage());
        }

    }
}





