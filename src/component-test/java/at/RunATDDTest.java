package at;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty","html:build/cucumber-reports/html/cucumber.html"}, features="src/component-test/resources")
public class RunATDDTest {
}
