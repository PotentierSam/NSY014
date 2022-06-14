package fr.sam.NSY014;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

public class SquashTMRestApi {
    private final String host;
    private final String port;
    private final String user;
    private final String password;

    public SquashTMRestApi(@NotNull String host ,@NotNull  String port, @NotNull String user, @NotNull String password) {
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

    public String getCampaignById(@NotNull int campaignId) {
        auth();
        String response;
        String test;

        try
        {
            response = RestAssured.get("http://" + host + ":" + port + "/squash/api/rest/latest/campaigns/"+campaignId).jsonPath().getJsonObject("name");
            System.out.println(response.toString());
            test = RestAssured.get("http://" + host + ":" + port + "/squash/api/rest/latest/iterations/85").jsonPath().getJsonObject("name");
            System.out.println(test.toString());
        }
        catch (Exception exception)
        {
            System.out.println(exception.getClass().getSimpleName());
            switch (exception.getClass().getSimpleName())
            {
                case "UnknownHostException":
                {
                    System.out.println("Erreur : l'hôte renseigné ne réponds pas");
                }

                case "ConnectException":
                {
                    System.out.println("Erreur : le port renseigné est invalide");
                }

                case "JsonPathException":
                {
                    System.out.println("Erreur : le/les identifiant(s) renseigné(s) est/sont invalide(s)");
                }
            }

            return "";
        }

        return "";
    }

    public List<String> getTestCasesByProjectId()
    {
        return null;
    }
}