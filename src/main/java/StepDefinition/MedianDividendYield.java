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
import java.util.List;

public class MedianDividendYield {
    public static void main(String[] args) {
        String filePath = "src/main/resources/file.json";

        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(jsonString);
            List<Double> dividendYields = new ArrayList<>();

            // Iterate through the JSONArray and extract dividend yield values
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String dividendYieldStr = jsonObject.getString("Div Yield").replaceAll("[^\\d.]", "");
                double dividendYield = Double.parseDouble(dividendYieldStr);
                dividendYields.add(dividendYield);
            }

            // Calculate the median of dividend yield values
            double medianDividendYield = calculateMedian(dividendYields);

            System.out.println("Median Dividend Yield: " + medianDividendYield);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private static double calculateMedian(List<Double> values) {
        Collections.sort(values);
        int size = values.size();
        if (size % 2 == 0) {
            int middleIndex = size / 2;
            double median = (values.get(middleIndex - 1) + values.get(middleIndex)) / 2.0;
            return median;
        } else {
            int middleIndex = size / 2;
            return values.get(middleIndex);
        }
    }
}
