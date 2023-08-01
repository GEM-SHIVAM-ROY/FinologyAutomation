package RunnerOptions;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(glue = "StepDefinition",features = {"src/main/resources/Features/finology_search.feature"},
        plugin = {"pretty","json:target/Jsonreports/reports.json","html:target/Htmlreports/reports.html","junit:target/JunitReports/reports/reports.xml"},
        monochrome = true)
public class RunnerFile {

}
