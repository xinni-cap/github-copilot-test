package com.calculator.api.tests.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * Main Cucumber test runner that discovers and runs all feature files.
 * <p>
 * Tests are configured via {@code src/test/resources/cucumber.properties}.
 * Use Maven profiles or tags to run specific subsets of tests.
 * </p>
 *
 * <p>Run all tests:</p>
 * <pre>mvn test</pre>
 *
 * <p>Run only smoke tests:</p>
 * <pre>mvn test -Dcucumber.filter.tags="@smoke"</pre>
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.calculator.api.tests.steps")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "pretty,html:target/cucumber-reports/report.html,json:target/cucumber-reports/report.json")
@ConfigurationParameter(key = OBJECT_FACTORY_PROPERTY_NAME, value = "io.cucumber.picocontainer.PicoFactory")
public class CucumberTestRunner {
    // Intentionally empty — configuration is in annotations above and cucumber.properties
}
