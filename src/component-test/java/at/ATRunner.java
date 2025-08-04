package at;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty,html:build/cucumber-reports/html/cucumber.html")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "at")
@ConfigurationParameter(key = Constants.FEATURES_PROPERTY_NAME, value = "src/component-test/resources")
public class ATRunner {
}
