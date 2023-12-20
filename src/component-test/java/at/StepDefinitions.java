package at;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import java.io.StringWriter;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.BeforeClass;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.math.BigInteger.ZERO;

import org.json.JSONObject;
import org.json.JSONArray;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {CucumberTestConfig.class})
@ActiveProfiles("test")
public class StepDefinitions {

    private static Response lastResponse;

    @Given("^the following daily data is available for bitcoin yesterday:$")
    public void the_following_daily_data_is_available_for_bitcoin_yesterday(DataTable dataTable) throws IOException, InterruptedException {
        var x = 0;
        final var y = 0;
        lastResponse = RestAssured
                .given() // TODO note here about postman collection on passing n bucetname to lcalstack ... below may need correcting
                .get("http://127.0.0.1:8080/api/v1/todo/getPresignedPutUrl?fileName=myfile.bla");// TODO use restAssured param
        assertThat(lastResponse.getStatusCode()).isEqualTo(200);
    }

    @When("^I make a request for daily data \"([^\"]*)\" from \"([^\"]*)\" to \"([^\"]*)\"$")
    public void i_make_a_request_for_daily_data(String baseCurrency, String from, String to) {
    }

    @Then("^the following data should be returned:$")
    public void the_following_data_should_be_returned(DataTable dataTable) throws Throwable {

    }

    public static final class Companion { // public is needed for @Before annotation
        @Before
        public static void beforeAll() {
            if (!containerStarted) {
                dockerComposeContainer.withLocalCompose(true); // version in testcontainers library v 1.17.5 buggy
                dockerComposeContainer.start();
                containerStarted = true;
//          Thread.sleep(20000) // workaround for ARM64
            }
            dockerComposeContainer.withRemoveImages(DockerComposeContainer.RemoveImages.ALL);
        }

        private static boolean containerStarted = false;
        private static String client_id = "some_client_id" ; // petshopapi
        private static String client_secret = "some_client_secret"; // 7riSklHZfjhmEGrDFakimD2heGOBImCs
        private static String authPath = "http://localhost:8180/realms/petshoprealm/protocol/openid-connect/token";

        private static String getToken() {
             Response tokenResponse =
                    RestAssured.given()
                            .contentType("application/x-www-form-urlencoded")
                            .formParam("grant_type", "client_credentials")
                            .formParam("client_id", client_id)
                            .formParam("client_secret", client_secret)
                            .post(authPath);
            assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            return new JSONObject(tokenResponse.body().asString()).getString("access_token");
        }

        // TODO what is <?>
        private static final DockerComposeContainer<?> dockerComposeContainer =
                new DockerComposeContainer(new File("docker-compose-component-test.yml"))
                .waitingFor("pet-shop-s3-connector", new HostPortWaitStrategy());

        public static JSONArray convertDataTableToJSONArray(DataTable dataTable, String... columnsToIgnore) throws JSONException {
            List<List<String>> table = dataTable.asLists();
            JSONArray jsonArray = new JSONArray();
            boolean headerRow = true;
            List<String> keys = table.get(ZERO.intValue());
            for (List<String> row : table) {
                jsonArray = (headerRow) ?  jsonArray : jsonArray.put(convertRowToJSONObject(keys, row, columnsToIgnore));
                headerRow=false;
            }
            return jsonArray;
        }

        private static JSONObject convertRowToJSONObject(List<String> keys, List<String> row, String... columnsToIgnore) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            int columnCounter = 0;
            for (String column : row) {
                jsonObject.put(keys.get(columnCounter), column);
                columnCounter++;
            }
            for(String column : columnsToIgnore) {
                jsonObject.remove(column);
            }
            return jsonObject;
        }

        private String createRequestFromDataTable(DataTable dataTable, String templateFile) throws IOException, TemplateException {
            String response;
            Configuration cfg = new Configuration(new Version("2.3.23"));
            cfg.setClassForTemplateLoading(this.getClass(), "/");
            cfg.setEncoding(Locale.getDefault(), "UTF-8");
            var template = cfg.getTemplate(templateFile);
            try (StringWriter out = new StringWriter()) {
                template.process(dataTable.asMaps().get(0), out);
                response = out.getBuffer().toString();
                out.flush();
            }// TODO see linux tools too
            return response;
        }

//        private static String createExpectation(String time) throws IOException, TemplateException {
//            String response;
//            Configuration cfg = new Configuration(new Version("2.3.23"));
//            cfg.setClassForTemplateLoading(CryptoDailyDataFetcherServiceTest.class, "/");
//            cfg.setDefaultEncoding("UTF-8");
//            Template template =
//                    cfg.getTemplate("crypto_data_fetcher_daily_BTC_one_day_sample_response_expectation.ftl");
//            Map<String, Object> templateData = new HashMap<>();
//            if (!time.isEmpty()) {
//                templateData.put("time", time);
//            }
//            try (StringWriter out = new StringWriter()) {
//                template.process(templateData, out);
//                response = out.getBuffer().toString();
//                out.flush();
//            }
//            return response;
//        }

//        public static JSONObject makeStubFromDataTable(String fileName, DataTable dataTable, String... columnsToIgnore) throws IOException, JSONException {
//            JSONObject jsonObject = new JSONObject();
//            JSONArray data = convertDataTableToJSONArray(dataTable, EMPTY_STRING);
//            jsonObject.put(DATA_KEY, data);
//            Files.write(Paths.get(TEMP_PATH_KEY +fileName), jsonObject.toString(4).replaceAll(BACK_SLASH_REGEX, EMPTY_STRING).getBytes());
//            return jsonObject;
//        }
    }

}