package at;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import java.io.IOException;

@ContextConfiguration(classes = {CucumberTestConfig.class})
@ActiveProfiles("test")
public class StepDefinitions {

    @Given("^the following daily data is available for bitcoin yesterday:$")
    public void the_following_daily_data_is_available_for_bitcoin_yesterday(DataTable dataTable) throws IOException, InterruptedException {

    }

    @When("^I make a request for daily data \"([^\"]*)\" from \"([^\"]*)\" to \"([^\"]*)\"$")
    public void i_make_a_request_for_daily_data(String baseCurrency, String from, String to) {
    }

    @Then("^the following data should be returned:$")
    public void the_following_data_should_be_returned(DataTable dataTable) throws Throwable {

    }

}