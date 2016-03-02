import java.util.HashMap;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("loginFail", request.session().attribute("loginFail"));
      model.put("allUsers", User.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/login", (request, response) -> {
      int userId = Integer.parseInt(request.queryParams("userName"));
      User user = User.find(userId);
      String password = request.queryParams("userPassword");
      if (user.getPassword().equals(password)) {
        request.session().attribute("loginFail", false);
        request.session().attribute("currentUser", user);
        response.redirect("/welcome");
        return null;
      }
      request.session().attribute("loginFail", true);
      response.redirect("/");
      return null;
    });

    get("/welcome", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User currentUser = request.session().attribute("currentUser");
      model.put("currentUser", currentUser);
      model.put("template", "templates/welcome.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
