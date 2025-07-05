package ai;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Keys;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ChatGptClient {
//    TODO: Responsibilities:
//      - Send HTTP requests to OpenAI API with topic prompt.
//      - Handle authentication, rate‑limit headers, and JSON responses.

    private static final String BASE_URL = "https://app.seker.live/fm1/";

    public static String getBalance(){
        return fetchData("check-balance", null);
    }

    public static String postClear(){
        return fetchData("clear-history", null);
    }


    public static String getSurvey(String topic) {
        return fetchData("send-message", topic);
    }

    private static String fetchData(String task, String topic){
        try {
            String urlString = "";
            if (task.equals("send-message")){
                String prompt = URLEncoder.encode(createFormat(topic), StandardCharsets.UTF_8);
                urlString = BASE_URL + task +
                        "?id=" + Keys.STUDENT_ID +
                        "&text=" + prompt;
            } else {
                urlString = BASE_URL + task +
                        "?id=" + Keys.STUDENT_ID;
            }

            System.out.println("url: " + urlString);

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(urlString)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.body() == null) throw new RuntimeException("No response :(");
            String responseBody = response.body().string();


            JSONObject obj = new JSONObject(responseBody);
            if (task.equals("clear-history")){
                return obj.getBoolean("success")? "Fetched successfully" : "ERROR fetching";
            }
            System.out.println(obj.getBoolean("success")? "Fetched successfully" : "ERROR fetching");

            if (!obj.has("extra") || obj.isNull("extra")) {
                throw new RuntimeException("Missing or null 'extra' field");
            }

            response.close();
            return obj.getString("extra");

        } catch (Exception e) {
            System.err.println("Error fetching data: " + e.getMessage());
            return null;
        }
    }

    private static String createFormat(String topic){
        return "Answer ONLY with a valid JSON object in this exact format:\n" +
                "{ \"survey\": { \"title\": \"" + topic + "\", \"questions\": [ ... ] } }\n" +
                "- Include 2 to 3 questions\n" +
                "- Each question must have 2 to 4 randomized answer options\n" +
                "- Do NOT include explanations, markdown, or any text — JSON ONLY" +
                "- Use only names like: survey, title, questions, question, options";
        // topic,question1:[answer1,answer2,...],question2:[answer1,answer2,...],...
    }
}
