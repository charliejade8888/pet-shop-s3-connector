package at;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;

import static java.math.BigInteger.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;
import static org.testcontainers.shaded.org.apache.commons.io.FileUtils.getFile;
import static reactor.core.publisher.Mono.when;

@ContextConfiguration(classes = {CucumberTestConfig.class})
@ActiveProfiles("test")
public class StepDefinitions {

    // TODO https://docs.gradle.org/current/samples/sample_java_modules_multi_project.html

    // TODO mention aws folder in README + add scenario for using links
    // TODO get this back on main

    private static Response lastResponse;
    private static RequestSpecification request;
    private static String preSignedLinkPath;

    @Given("I need a pre-signed link to {string} a file {string} using bucket {string}")
    public void i_need_a_pre_signed_link_to_a_file_using_bucket(String action, String fileName, String bucketName) {
        preSignedLinkPath = action == "upload"
                ? "http://127.0.0.1:8080/api/v1/todo/getPresignedPutUrl"
                : "http://127.0.0.1:8080/api/v1/todo/getPresignedUrl";
        request = RestAssured
                .given()
                .queryParam("fileName", bucketName + "/" + fileName);
    }

    @Given("I use the link to {string} a file {string} into a bucket {string}") // TODO BS rename method name!
    public void i_use_the_link_to_a_file_into_a_bucket(String action, String fileName, String bucketName) throws IOException {
        // TODO don't need bucketname here!
        final File file = new File("/tmp", fileName);
        FileUtils.writeStringToFile(file, "Hello World", "ISO-8859-1");
        String path = lastResponse.getBody().asString();
        if(action.equals("upload")) {
          RestAssured.urlEncodingEnabled = false;
            lastResponse = RestAssured.
                    given()
                    .multiPart(file) // multipart allows for streaming the file without loading it all into memory at once.
//                    .multiPart("json", jsonData, ContentType.JSON) // Add JSON data as another multi-part form parameter
                    .when()
                    .put(path)
                    .then().extract().response();
        } else {
            lastResponse = RestAssured.
                    given().
                    get(path).
                    andReturn(); // content not fetched until asXXX() called


        }
    }

    @When("I make a request for the link")
    public void i_make_a_request_for_the_link() {
        var x = 0;
        final var y = 0;
        lastResponse = request
                .get(preSignedLinkPath);
    }

    @Then("the pre-signed link should be successfully returned")
    public void the_pre_signed_link_should_be_successfully_returned() throws IOException {
        assertThat(lastResponse.getStatusCode()).isEqualTo(200);
    }

    @Then("the file is present")
    public void the_file_is_present() throws IOException {
//        try(InputStream downloadedFileIS = lastResponse.asInputStream()){ // try with resource closes resources automatically
//            File targetFile = new File("/home/charliejade/out.txt"); // better use /tmp (or OS specific temp dir)
//            Files.copy(downloadedFileIS, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//        } // does not load entire file into memory

        lastResponse.
                then().
                statusCode(200).
                body(containsString("Hello World"));
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