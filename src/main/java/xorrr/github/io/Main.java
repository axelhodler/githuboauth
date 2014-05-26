package xorrr.github.io;

import static spark.Spark.get;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class Main {
    public static void main(String[] args) {
        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("client_id", System.getenv("GH_BASIC_CLIENT_ID"));

            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerTemplateEngine());

        get("/callback",
                (request, response) -> {
                    String sessionCode = request.queryParams("code");
                    HttpResponse<JsonNode> jsonResponse = null;
                    try {
                        jsonResponse = Unirest
                                .post("https://github.com/login/oauth/access_token")
                                .header("accept", "application/json")
                                .field("client_id",
                                        System.getenv("GH_BASIC_CLIENT_ID"))
                                .field("client_secret",
                                        System.getenv("GH_BASIC_SECRET_ID"))
                                .field("code", sessionCode).asJson();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String accessToken = jsonResponse.getBody().getObject()
                            .get("access_token").toString();

                    HttpResponse<JsonNode> resp = null;
                    try {
                        resp = Unirest.get(
                                "https://api.github.com/user?access_token="
                                        + accessToken).asJson();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return resp.getBody().getObject().get("login").toString();
                });
    }
}
