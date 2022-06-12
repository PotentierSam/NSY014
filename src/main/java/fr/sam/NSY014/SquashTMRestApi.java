package fr.sam.NSY014;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.ArrayList;

public class SquashTMRestApi {
    private final String host;
    private final String port;
    private final String user;
    private final String password;

    public SquashTMRestApi(String host, String port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    private void auth() {
        RestAssured.baseURI = "http://" + host;
        RestAssured.port = Integer.parseInt(port);
        RestAssured.authentication = RestAssured.basic(user, password);
        RestAssured.useRelaxedHTTPSValidation();
    }

    public String getProjectIdByName(String projectName) {
        auth();
        final Response response = RestAssured.get("http://" + host + ":" + port + "/squash/api/rest/latest/projects");
        final JsonPath path = response.jsonPath();
        final ArrayList<?> ids = path.getJsonObject("_embedded.projects. id");
        final ArrayList<?> names = path.getJsonObject("_embedded.projects.name");

        for (int numProject = 0; numProject < names.size(); numProject++) {
            if (names.get(numProject).toString().equals(projectName)) {
                return ids.get(numProject).toString();
            }
        }
        return null;
    }
}