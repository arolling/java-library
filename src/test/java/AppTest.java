import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();
  public WebDriver getDefaultDriver() {
      return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Epicodus Library");
  }

  @Test
  public void login() {
    User myUser = new User("Matt", "password123", "patron");
    myUser.save();
    User myUser2 = new User("Abby", "password", "patron");
    myUser2.save();
    goTo("http://localhost:4567/");
    click("option", withText("Matt"));
    fill("#userPassword").with("password123");
    submit("#userLogin");
    assertThat(pageSource()).contains("Welcome, Matt");
  }
}
