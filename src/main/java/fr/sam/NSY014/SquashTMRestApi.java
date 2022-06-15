package fr.sam.NSY014;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SquashTMRestApi {
    private final String host;
    private final String port;
    private final String user;
    private final String password;

    public SquashTMRestApi(@NotNull String host, @NotNull String port, @NotNull String user, @NotNull String password) {
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

    public HashMap<String, String> getTestsByProjectName(@NotNull String ProjectName, @NotNull String CampaignName, @NotNull String IterationName) {
        try {
            auth();
            Response response = RestAssured.get("http://" + host + ":" + port + "/squash/api/rest/latest/projects");
            ArrayList<?> ids;
            ArrayList<?> names;
            ids = response.jsonPath().getJsonObject("_embedded.projects.id");
            names = response.jsonPath().getJsonObject("_embedded.projects.name");
            String ProjectId;
            String CampaignId;
            String IterationId;
            HashMap<String, String> Tests = new HashMap<String, String>();
            String info;


            for (int numProject = 0; numProject < names.size(); numProject++) {
                if (names.get(numProject).toString().equals(ProjectName)) {
                    ProjectId = ids.get(numProject).toString();
                    response = RestAssured.get("http://" + host + ":" + port + "/squash/api/rest/latest/projects/" + ProjectId + "/campaigns");

                    ids = response.jsonPath().getJsonObject("_embedded.campaigns.id");
                    names = response.jsonPath().getJsonObject("_embedded.campaigns.name");
                    info = ("Nom du projet : '" + ProjectName + "' Id du projet : '" + ProjectId + "'\n");

                    for (int numCampaign = 0; numCampaign < names.size(); numCampaign++) {
                        if (names.get(numCampaign).toString().equals(CampaignName)) {
                            CampaignId = ids.get(numCampaign).toString();
                            response = RestAssured.get("http://" + host + ":" + port + "/squash/api/rest/latest/campaigns/" + CampaignId + "/iterations");

                            ids = response.jsonPath().getJsonObject("_embedded.iterations.id");
                            names = response.jsonPath().getJsonObject("_embedded.iterations.name");
                            info = info + ("    Nom de campagne : '" + CampaignName + "' Id de campagne : '" + CampaignId + "'\n");

                            for (int numIteration = 0; numIteration < names.size(); numIteration++) {
                                if (names.get(numIteration).toString().equals(IterationName)) {
                                    IterationId = ids.get(numIteration).toString();
                                    response = RestAssured.get("http://" + host + ":" + port + "/squash/api/rest/latest/iterations/" + IterationId + "/test-suites");

                                    ids = response.jsonPath().getJsonObject("_embedded.test-suites.id");
                                    names = response.jsonPath().getJsonObject("_embedded.test-suites.name");
                                    info = info + ("        Nom d'iteration : '" + IterationName + "' Id d'iteration : '" + IterationId + "'\n");

                                    for (int numSuiteTest = 0; numSuiteTest < names.size(); numSuiteTest++) {
                                        String id = ids.get(numSuiteTest).toString();
                                        String name = names.get(numSuiteTest).toString();
                                        Tests.put(name, id);
                                        info = info + ("            Nom de test : '" + name + "' Id de test : '" + id + "'\n");
                                    }
                                    System.out.println(info);
                                    return Tests;
                                }
                            }
                            System.out.println("Erreur : l'iteration avec comme nom " + IterationName + " n'existe pas");
                            return null;
                        }
                    }
                    System.out.println("Erreur : la campagne avec comme nom " + CampaignName + " n'existe pas");
                    return null;
                }
            }
            System.out.println("Erreur : le projet avec comme nom " + ProjectName + " n'existe pas");
            return null;
        } catch (Exception exception) {
            switch (exception.getClass().getSimpleName()) {
                case "UnknownHostException" -> {
                    System.out.println("Erreur : l'hôte renseigné ne réponds pas");
                }
                case "ConnectException" -> {
                    System.out.println("Erreur : le port renseigné est invalide");
                }
                case "JsonPathException" -> {
                    System.out.println("Erreur : le/les identifiant(s) renseigné(s) est/sont invalide(s)");
                }

            }
        }
        return null;
    }

    public void modifyTestStatus(@NotNull String TestId, @NotNull String Status) {
        List<String> list = Arrays.asList("NB OF TC", "READY", "RUNNING", "SUCCESS", "FAILURE", "BLOCKED", "UNTESTABLE");
        if (list.contains(Status)) {
            try {
                auth();
                String jsonString = "{\r\n" +
                        "    \"status\" : \"" + Status + "\"}";

                RequestSpecification request = RestAssured.given();

                request.contentType(ContentType.JSON);
                request.baseUri("http://localhost:8080/squash/api/rest/latest/test-suites/" + TestId);
                request.body(jsonString);
            } catch (Exception exception) {
                switch (exception.getClass().getSimpleName()) {
                    case "UnknownHostException" -> {
                        System.out.println("Erreur : l'hôte renseigné ne réponds pas");
                    }
                    case "ConnectException" -> {
                        System.out.println("Erreur : le port renseigné est invalide");
                    }
                    case "JsonPathException" -> {
                        System.out.println("Erreur : le/les identifiant(s) renseigné(s) est/sont invalide(s)");
                    }

                }
            }
        } else {
            System.out.println("Erreur : le status donner en paramêtre n'est pas valide");
        }
    }
}