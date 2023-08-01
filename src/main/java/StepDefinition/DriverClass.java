package StepDefinition;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverClass {
    public static WebDriver driver;
    public static WebDriver setup(){
        if(driver==null){
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            WebDriverManager.chromedriver().setup();
            driver=new ChromeDriver(options);
        }
        return driver;

    }
}
