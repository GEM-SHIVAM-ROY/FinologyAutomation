package Locators;

import org.openqa.selenium.By;

public class Locators {
    public static String url="https://ticker.finology.in/";
    public static String searchBox= "//input[@id='txtSearchComp']";
    public  static  String companyName ="//span[@id='mainContent_ltrlCompName']";
    public static String companyInfoId="//p[@id='mainContent_compinfoId']";
    public static String currentNSE="(//span[@class='Number'])[1]";
    public static String priceSummary="//div[@id='mainContent_pricesummary']//*[@class=\"col-6 col-md-3 compess\"]";
    public static String finStar="//div[@id='ratingcollapse']//h6";
    public static String peValue="(//div[@class='col-6 col-md-4 compess'])[4]//p";
    public static String divYieldValue="(//div[@class='col-6 col-md-4 compess'])[7]//p";
    public static String getData="//span[@class='float-left mb-4 d-block d-md-inline-block ml-3']";
    public static String growthList="//div[@class='w-100']//div";
    public static By companiesName(String company)
     {
          return By.xpath("//span[contains(@class, 'badge badge-primary') and contains(text(),'" + company + "')]");
     }
     public static By Showprice(int i){
        return By.xpath("(//span[@class='float-right showprice']//a)[" + (i + 1) + "]");
     }
}
