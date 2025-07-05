package ai;

import model.Question;
import model.Survey;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AiSurveyGenerator {
//    TODO: Responsibilities:
//      - Transform raw API text into structured Question and option lists.

    public static Survey generateSurvey(String topic, User creator, int delayInMinutes){
        String data = ChatGptClient.getSurvey(topic);
        if (data == null) {
            System.out.println("null data");
            return null;
        }

        if (data.startsWith("```json")){
            System.out.println("data substringed");
            data = data.substring(7,data.length() - 3);
        }

//        System.out.println("\ndata: \n" + data);
        JSONObject obj = new JSONObject(data);
        JSONObject surveyObj = obj.getJSONObject("survey");

        String title = surveyObj.getString("title");
        Survey survey = new Survey(creator, title, delayInMinutes);

        JSONArray questionsArray = surveyObj.getJSONArray("questions");

        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject questionObj = questionsArray.getJSONObject(i);
            // {
            //   "question": "What is your favorite type of dragon?",
            //   "options": [ "Fire Dragon", "Water Dragon", "Ice Dragon", "Shadow Dragon" ]
            //  }

            String questionTopic = "X_X";
            if (questionObj.has("question")) questionTopic = questionObj.getString("question");

            if (questionObj.has("text")) questionTopic = questionObj.getString("text");

            JSONArray optionsArray = questionObj.getJSONArray("options");
            List<String> answers = new ArrayList<>();

            for (int j = 0; j < optionsArray.length(); j++) {
                answers.add(optionsArray.getString(j));
            }

            Question question = new Question(questionTopic,answers);
            survey.addQuestion(question);
        }

        return survey;
    }
}
