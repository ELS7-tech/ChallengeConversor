package Interface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class ConversaoGUI extends JFrame {

    private static final String API_KEY = "b7220f2e84b9ec47797730c2";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    private JComboBox<String> fromCurrencyCombo;
    private JComboBox<String> toCurrencyCombo;
    private JTextField amountField;
    private JLabel resultLabel;

    public ConversaoGUI() {

        setTitle("Conversor de Moedas");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] currencies = {"USD", "EUR", "GBP", "JPY", "BRL", "AUD"};
        fromCurrencyCombo = new JComboBox<>(currencies);
        toCurrencyCombo = new JComboBox<>(currencies);
        amountField = new JTextField(10);
        JButton convertButton = new JButton("Converter");
        resultLabel = new JLabel("Valor convertido: ");


        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });


        JPanel panel = new JPanel();
        panel.add(new JLabel("De:"));
        panel.add(fromCurrencyCombo);
        panel.add(new JLabel("Para:"));
        panel.add(toCurrencyCombo);
        panel.add(new JLabel("Valor:"));
        panel.add(amountField);
        panel.add(convertButton);
        panel.add(resultLabel);


        add(panel);
    }

    private void convertCurrency() {
        String fromCurrency = fromCurrencyCombo.getSelectedItem().toString();
        String toCurrency = toCurrencyCombo.getSelectedItem().toString();
        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor válido.");
            return;
        }

        try {
            Map<String, Double> rates = getExchangeRates(fromCurrency);
            double convertedAmount = amount * (rates.get(toCurrency) / rates.get(fromCurrency));
            resultLabel.setText(String.format("Valor convertido: %.2f %s", convertedAmount, toCurrency));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar as taxas de conversão: " + e.getMessage());
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
        java.lang.reflect.Type type = new TypeToken<Map<String, Object>>(){}.getType();
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

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ConversaoGUI().setVisible(true);
            }
        });
    }
}

