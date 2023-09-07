package tech.aluvesoftware.api.http;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;

import static io.restassured.RestAssured.given;

public class Request {

    private final String baseURL;
    private final String[] acceptableMethods = new String[]{"GET", "POST", "DELETE", "PUT", "PATCH"};
    private RequestSpecification requestSpec;
    @Setter
    private String endPoint = "";
    @Setter
    private Map<String, String> headers;
    @Setter
    private String body;
    private String method;
    private ArrayList<Object> media;
    @Setter
    @Getter
    private Cookie cookie;
    @Setter
    private Map<String, String> requestParam;
    @Setter
    private Map<String, String> formParams;

    public Request(String baseURL) {
        this.baseURL = baseURL;
    }


    /**
     * This function sets the HTTP method to be used in a request and throws an exception if the method is
     * not valid.
     *
     * @param method A string representing an HTTP method (e.g. GET, POST, PUT, DELETE).
     */
    public void setHTTPMethod(String method) {
        if (Arrays.stream(acceptableMethods).anyMatch(Predicate.isEqual(method.toUpperCase()))) {
            this.method = method.toUpperCase();
        } else {
            throw new IllegalArgumentException("Invalid method: " + method
                    + "\nValid methods: " + Arrays.toString(acceptableMethods));
        }
    }

    /**
     * The function builds a request specification with the given base URL.
     */
    public void build() {
        requestSpec = new RequestSpecBuilder().setBaseUri(baseURL).build();
    }

    /**
     * This function returns a modified request specification based on various input parameters.
     *
     * @return The method `request()` returns a `RequestSpecification` object.
     */
    private RequestSpecification request() {
        RequestSpecification requestModifier = given().spec(requestSpec);
        if (headers != null) {
            requestModifier.headers(headers);
        }

        if (formParams != null) {
            requestModifier.formParams(formParams);
        }

        if (requestParam != null) {
            requestModifier.params(requestParam);
        }

        if (body != null) {
            requestModifier.body(body);
        }

        if (cookie != null) {
            requestModifier.cookie(cookie);
        }

        if (media != null) {
            requestModifier.multiPart((String) media.get(0), (File) media.get(1));
        }
        return requestModifier;
    }

    /**
     * This function returns the value of the "Cookie" header.
     *
     * @return The method `getCookie()` is returning the value of the "Cookie" header from the `headers`
     * object.
     */
    public String getCookie() {
        return headers.get("Cookie");
    }

    /**
     * The function adds a key and a file location to an ArrayList.
     *
     * @param key                  The key is a unique identifier for the file being added. It is used to retrieve the
     *                             file later on when needed.
     * @param absoluteFileLocation The absolute file location is the complete path of a file on a
     *                             computer's file system, including the drive letter (if applicable), all directories, and the file
     *                             name with its extension. For example, "C:\Users\JohnDoe\Documents\example.txt" is an absolute file
     *                             location on
     */
    public void addFile(String key, String absoluteFileLocation) {
        this.media = new ArrayList<>();
        this.media.add(key);
        this.media.add(new File(absoluteFileLocation));
    }

    /**
     * The function sends an HTTP request.
     *
     * @return The method `send()` returns a `Response` object. The specific `Response` object returned
     * depends on the HTTP method specified in the `method` variable. If `method` is "POST", a
     * `Response` object is returned after making a POST request to the `endPoint`. If `method` is
     * "PUT", a `Response` object is returned after making a PUT request to the
     */
    public Response send() {

        switch (method) {
            case "POST":
                return request().when().redirects().follow(true).post(endPoint);
            case "PUT":
                return request().when().redirects().follow(true).put(endPoint);
            case "DELETE":
                return request().when().redirects().follow(true).delete(endPoint);
            default:
                return request().when().redirects().follow(true).get(endPoint);
        }
    }
}
