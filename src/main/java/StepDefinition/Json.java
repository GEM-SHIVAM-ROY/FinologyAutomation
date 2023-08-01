package StepDefinition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Json {
    public static void main(String[] args) {
            String filePath = "src/main/resources/file.json";

            try {
                String jsonString = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
                JSONArray jsonArray = new JSONArray(jsonString);
                List<CompanyPrice> companyPrices = new ArrayList<>();

                // Iterate through the JSONArray and extract company names and current prices
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String companyName = jsonObject.getString("Company");
                    String currentNSE = jsonObject.getString("Current NSE").replaceAll("[^\\d.]", "");
                    double currentPrice = Double.parseDouble(currentNSE.replace(",", ""));
                    companyPrices.add(new CompanyPrice(companyName, currentPrice));
                }

                // Sort the companyPrices list in descending order based on the current price
                Collections.sort(companyPrices, Comparator.comparingDouble(CompanyPrice::getCurrentPrice).reversed());

                // Print the sorted company names and current prices
                for (CompanyPrice cp : companyPrices) {
                    System.out.println(cp.getCompanyName() + " - " + cp.getCurrentPrice());
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

    static class CompanyPrice {
        private String companyName;
        private double currentPrice;

        public CompanyPrice(String companyName, double currentPrice) {
            this.companyName = companyName;
            this.currentPrice = currentPrice;
        }

        public String getCompanyName() {
            return companyName;
        }

        public double getCurrentPrice() {
            return currentPrice;
        }
    }
    }


