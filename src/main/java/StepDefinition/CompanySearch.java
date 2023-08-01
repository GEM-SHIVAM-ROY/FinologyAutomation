package StepDefinition;


import Locators.Locators;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CompanySearch {
    static XSSFWorkbook workbook = new XSSFWorkbook();
    public static WebDriver driver;
    @Before
    public void setup() {
        driver = DriverClass.setup();
    }
    @Given("the user is on the Finology Ticker website {string}")
    public void openWebpage(String url) {
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }
    @When("the user clicks on the search box")
    public void clickSearchBox() {
        driver.findElement(By.xpath(Locators.searchBox)).click();
    }
    @When("the user enters {string} in the search box")
    public void companyName(String string) {

        Actions actions=new Actions(driver);
        actions.sendKeys(driver.findElement(By.xpath(Locators.searchBox)),string).build().perform();
    }
    @Then("the user should see the search results for {string}")
    public void result(String string) {
        String company = string.toUpperCase();
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(Locators.companiesName(company)));
        WebElement companyLink = driver.findElement(Locators.companiesName(company));
        companyLink.click();
    }
    @And("the user fetches and store the details in the Excel sheet")
    public void dataIntoExcel() {

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = currentDateTime.format(formatter);

        String sheetName = "DataSheet";

        XSSFSheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }

        int lastRow = sheet.getLastRowNum();
        int newRow = lastRow + 1;
        int cellIndx = 0;


        String companyName = driver.findElement(By.xpath(Locators.companyName)).getText();
        List<WebElement> list = driver.findElements(By.xpath(Locators.companyInfoId));

        for (WebElement element : list) {
            String parentText = element.getText();
            List<WebElement> childElements = element.findElements(By.xpath(".//span"));
            for (WebElement childElement : childElements) {
                parentText = parentText.replace(childElement.getText(), "");
            }
            System.out.println(parentText.trim());
        }

        String currentNSE = driver.findElement(By.xpath(Locators.currentNSE)).getText();

        //Create and set values for company header and value cells
        Row companyHeaderRow = sheet.createRow(0);
        createCellAndSetValue(companyHeaderRow, cellIndx, "Company");
        createCellAndSetValue(companyHeaderRow, cellIndx + 1, "Current NSE");
        createCellAndSetValue(companyHeaderRow, cellIndx + 4, "Price Summary");
        createCellAndSetValue(companyHeaderRow, cellIndx + 8, "Finstar");


        Row companyValueRow = sheet.createRow(newRow + 1);
        createCellAndSetValue(companyValueRow, 0, companyName);
        createCellAndSetValue(companyValueRow, 1, currentNSE);

        // Add Price Summary data
        List<WebElement> priceSummaryList = driver.findElements(By.xpath(Locators.priceSummary));
        for (int i = 0; i < priceSummaryList.size(); i++) {
            String data = priceSummaryList.get(i).getText();
            createCellAndSetValue(companyValueRow, 2 + i, data);
        }


        // Add FinStar data
        List<WebElement> finStarList = driver.findElements(By.xpath(Locators.finStar));
        for (int i = 0; i < finStarList.size(); i++) {
            String dataFin = finStarList.get(i).getText();
            createCellAndSetValue(companyValueRow, 6 + i, dataFin);
        }

        // Add P/E and Div.Yield data
        String peValue = driver.findElement(By.xpath(Locators.peValue)).getText();
        String divYieldValue = driver.findElement(By.xpath(Locators.divYieldValue)).getText();
        createCellAndSetValue(companyHeaderRow, cellIndx + 10, "P/E");
        createCellAndSetValue(companyHeaderRow, cellIndx + 11, "Div.Yield");
        createCellAndSetValue(companyValueRow, 10, peValue);
        createCellAndSetValue(companyValueRow, 11, divYieldValue);

        // Add Price chart data
        createCellAndSetValue(companyHeaderRow, cellIndx + 16, "Price chart");
        for (int i = 1; i < 8; i++) {
//            String showPrice = "(//span[@class='float-right showprice']//a)[" + (i + 1) + "]";
            WebElement tenure = driver.findElement(Locators.Showprice(i));
            tenure.click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            createCellAndSetValue(companyHeaderRow, 12 + i - 1, tenure.getText());

            // Get data and add to the corresponding row
            String getData = driver.findElement(By.xpath(Locators.getData)).getText();
            createCellAndSetValue(companyValueRow, 12 + i - 1, getData);
        }
        // Add Sales Growth and Profit Growth data
        createCellAndSetValue(companyHeaderRow, cellIndx + 20, "Sales Growth");
        createCellAndSetValue(companyHeaderRow, cellIndx + 23, "Profit Growth");
        List<WebElement> growthList = driver.findElements(By.xpath(Locators.growthList));
        for (int i = 0; i <= 5; i++) {
            String salesData = growthList.get(i).getText();
            createCellAndSetValue(companyValueRow, 19 + i, salesData);
        }
        // Add timestamp
        createCellAndSetValue(companyHeaderRow, cellIndx + 25, "Timestamp");
        createCellAndSetValue(companyValueRow, cellIndx + 25, timestamp);
    }
    private void createCellAndSetValue(Row row, int cellIndex, String value) {
        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(value);
    }

    public static void excelToJson() throws IOException {
        String filePath = "src/main/resources/data.xlsx";
        try (FileOutputStream fos = new FileOutputStream("src/main/resources/file.json")) {

        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);

        Sheet sheet = workbook.getSheetAt(0);

        JSONArray jsonArray = new JSONArray();

        int lastRowNum = sheet.getLastRowNum();

        JSONArray arr = new JSONArray();
        Row row1 = sheet.getRow(0);
        JSONObject obj = new JSONObject();
        for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex = rowIndex + 2) {
            obj = new JSONObject();
            Row row = sheet.getRow(rowIndex);
            obj.put("Company", row.getCell(0).getStringCellValue());
            obj.put("Current NSE", row.getCell(1).getStringCellValue());
            JSONArray priceSummary = new JSONArray();
            for (int j = 2; j <= 5; j++) {
                priceSummary.put(row.getCell(j).getStringCellValue());
            }
            obj.put("Price Summary", priceSummary);
            JSONArray finstar = new JSONArray();
            for (int j = 6; j <= 9; j++) {
                finstar.put(row.getCell(j).getStringCellValue());

            }
            obj.put("Finstar", finstar);
            obj.put("P/E", row.getCell(10).getStringCellValue());
            obj.put("Div Yield", row.getCell(11).getStringCellValue());
            JSONArray priceChart = new JSONArray();
            for (int j = 12; j <= 18; j++) {
                priceChart.put(row1.getCell(j).getStringCellValue() + "-" + row.getCell(j).getStringCellValue());
            }
            obj.put("Price chart", priceChart);
            JSONArray salesGrowth = new JSONArray();
            for (int j = 19; j <= 21; j++) {
                salesGrowth.put(row.getCell(j).getStringCellValue());
            }
            obj.put("Sales Growth", salesGrowth);
            JSONArray profitGrowth = new JSONArray();
            for (int j = 22; j <= 24; j++) {
                profitGrowth.put(row.getCell(j).getStringCellValue());
            }
            obj.put("Profit Growth", profitGrowth);
            obj.put("Timestamp", row.getCell(25).getStringCellValue());
            arr.put(obj);
        }

        System.out.println(arr.toString());
            fos.write(arr.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @AfterAll
    public static void before_or_after_all() throws IOException {

        try (FileOutputStream fileOut = new FileOutputStream("src/main/resources/data.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver.close();
        excelToJson();
    }

}
