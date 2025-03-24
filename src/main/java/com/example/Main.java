package com.example;

import java.io.IOException;
import java.net.http.*;
import java.net.URI;
import java.time.Duration;
import java.util.HashSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Main {
    public static void main(String[] args)  {
        if (args.length==0 ){
            System.out.println("please pass in github username");
            return;
        }
        if(args.length >1){
            System.out.println("too many arguments");
            return;
        }


        String userName = args[0];

        String userEvents = "";


        try {
            userEvents = getGithubEvents(userName);
        }
        catch (IOException e){
            System.out.println("Could not send HTTP request");
            return;
        }
        catch (InterruptedException e){
            System.out.println("Connection Interrupted");
            return;
        }
        if (userEvents.isEmpty()){
            System.out.println("Could not find User");
            return;
        }

        JSONArray response = new JSONArray();


        try {
            response = parseResponse(userEvents);
        }
        catch (ParseException e){
            System.out.println("Could not parse JSON reponse");
            return;
        }



        System.out.println(getEvents(response, userName));


    }

    public static String getGithubEvents(String Username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/users/"+Username+"/events"))
                .timeout(Duration.of(10, SECONDS))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 404){
            return  "";
        }

        return  response.body();
    }


    public static JSONArray parseResponse(String response) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONArray) parser.parse(response);
    }

    public static  String getEvents(JSONArray response, String userName){
        HashSet<String> finalOutput = new HashSet<>();


        for(Object event : response){
            JSONObject currEvent = (JSONObject) event;


            if( ((String) currEvent.get("type")).equals("PushEvent")){
//                get repo name
                JSONObject repoInfo= (JSONObject) currEvent.get("repo");
                String[] urlPathStr = ((String) repoInfo.get("url")).split("\\/");
                String repoName = urlPathStr[urlPathStr.length-1];

//                get number of commits
                JSONObject payLoad = (JSONObject) currEvent.get("payload");
                int numberCommits = ((JSONArray)payLoad.get("commits")).size();

                finalOutput.add("Pushed "+ numberCommits + " commits to "+ repoName);


            } else if (((String) currEvent.get("type")).equals("CreateEvent")) {
                JSONObject repoInfo= (JSONObject) currEvent.get("repo");
                String[] urlPathStr = ((String) repoInfo.get("url")).split("\\/");
                String repoName = urlPathStr[urlPathStr.length-1];

                finalOutput.add("Created "+ repoName);
            } else if (((String) currEvent.get("type")).equals("IssueEvent")) {
                JSONObject repoInfo= (JSONObject) currEvent.get("repo");
                String[] urlPathStr = ((String) repoInfo.get("url")).split("\\/");
                String repoName = urlPathStr[urlPathStr.length-1];

                finalOutput.add("Opened a issue in "+ repoName);
            }
        }
        String[] output = new String[finalOutput.size()];
        output = finalOutput.toArray(output);
       return String.join("\n", output);
    }


}