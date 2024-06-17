package Conversor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Conversao {
    private static final String API_KEY = "b7220f2e84b9ec47797730c2";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo ao Conversor de Moedas!");


        String[] currencies = {"USD", "EUR", "GBP", "JPY", "BRL", "AUD"};


        System.out.println("Selecione a moeda de origem:");
        for (int i = 0; i < currencies.length; i++) {
            System.out.println((i + 1) + ". " + currencies[i]);
        }
        int fromCurrencyIndex = scanner.nextInt() - 1;

        System.out.println("Selecione a moeda de destino:");
        for (int i = 0; i < currencies.length; i++) {
            System.out.println((i + 1) + ". " + currencies[i]);
        }
        int toCurrencyIndex = scanner.nextInt() - 1;

        System.out.print("Digite o valor a ser convertido: ");
        double amount = scanner.nextDouble();

        scanner.close();


        try {
            Map<String, Double> rates = getExchangeRates(currencies[fromCurrencyIndex]);
            double convertedAmount = convertCurrency(amount, rates.get(currencies[fromCurrencyIndex]), rates.get(currencies[toCurrencyIndex]));
            System.out.printf("Valor convertido: %.2f %s\n", convertedAmount, currencies[toCurrencyIndex]);
        } catch (Exception e) {
            System.out.println("Erro ao buscar as taxas de conversão: " + e.getMessage());
        }
    }


    public static Map<String, Double> getExchangeRates(String baseCurrency) throws Exception {
        String url = BASE_URL + API_KEY + "/latest/" + baseCurrency;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> jsonMap = gson.fromJson(response.body(), type);

        Map<String, Double> rates = new HashMap<>();
        if (jsonMap.get("result").equals("success")) {
            Map<String, Double> conversionRates = (Map<String, Double>) jsonMap.get("conversion_rates");
            rates.putAll(conversionRates);
        } else {
            throw new Exception("Falha ao obter taxas de câmbio.");
        }

        return rates;
    }


    public static double convertCurrency(double amount, double fromRate, double toRate) {
        return amount * (toRate / fromRate);
    }
}