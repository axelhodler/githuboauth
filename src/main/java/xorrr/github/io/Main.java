package xorrr.github.io;

import static spark.Spark.get;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;

import com.mashape.unirest.http.Unirest;

public class Main {
    public static void main(String[] args) {
        String client_id = System.getenv("GH_BASIC_CLIENT_ID");
        String client_secret = System.getenv("GH_BASIC_SECRET_ID");

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("client_id", client_id);

            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerTemplateEngine());

        get("/callback",
                (request, response) -> {
                    String sessionCode = request.queryParams("code");

                    String accessToken = null;
                    try {
                        accessToken = Unirest
                                .post("https://github.com/login/oauth/access_token")
                                .header("accept", "application/json")
                                .field("client_id", client_id)
                                .field("client_secret", client_secret)
                                .field("code", sessionCode).asJson().getBody()
                                .getObject().get("access_token").toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String userName = getUserName(accessToken);

                    return accessToken + " belongs to " + userName;
                });
    }

    private static String getUserName(String accessToken) {
        String userName = null;
        try {
            userName = Unirest
                    .get("https://api.github.com/user?access_token="
                            + accessToken).asJson().getBody().getObject()
                    .get("login").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userName;
    }
}
