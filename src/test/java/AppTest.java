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
  public void loginPatron() {
    User myUser = new User("Matt", "password123", "patron");
    myUser.save();
    User myUser2 = new User("Abby", "password", "patron");
    myUser2.save();
    Book myBook = new Book("The Stand", 1);
    myBook.save();
    myUser.checkoutBook(myBook.getId());
    goTo("http://localhost:4567/");
    click("option", withText("Matt"));
    fill("#userPassword").with("password123");
    submit("#userLogin");
    assertThat(pageSource()).contains("Welcome, Matt");
    assertThat(pageSource()).contains("Search Catalog");
    assertThat(pageSource()).contains("The Stand");
  }

  @Test
  public void search() {
    User myUser = new User("Matt", "password123", "patron");
    myUser.save();
    User myUser2 = new User("Abby", "password", "patron");
    myUser2.save();
    Book myBook = new Book("The Stand", 1);
    myBook.save();
    Book myBook2 = new Book("Nitjdflkhadfihi", 3);
    myBook2.save();

    goTo("http://localhost:4567/");
    click("option", withText("Matt"));
    fill("#userPassword").with("password123");
    submit("#userLogin");
    click("a", withText("Search Catalog"));
    fill("#search").with("and");
    submit(".btn");
    assertThat(pageSource()).contains("The Stand");
  }

  @Test
  public void checkout() {
    User myUser = new User("Matt", "password123", "patron");
    myUser.save();
    User myUser2 = new User("Abby", "password", "patron");
    myUser2.save();
    Book myBook = new Book("The Stand", 1);
    myBook.save();
    Book myBook2 = new Book("Nitjdflkhadfihi", 3);
    myBook2.save();

    goTo("http://localhost:4567/");
    click("option", withText("Matt"));
    fill("#userPassword").with("password123");
    submit("#userLogin");
    goTo("http://localhost:4567/books/" + myBook.getId());
    click("a", withText("Checkout"));
    assertThat(pageSource()).contains("You have successfully checked out The Stand");
  }

  @Test
  public void loginLibrarian() {
    User myUser = new User("Matt", "password123", "patron");
    myUser.save();
    User myUser2 = new User("Abby", "password", "librarian");
    myUser2.save();
    Book myBook = new Book("The Stand", 1);
    myBook.save();
    myUser.checkoutBook(myBook.getId());
    goTo("http://localhost:4567/");
    click("option", withText("Abby"));
    fill("#userPassword").with("password");
    submit("#userLogin");
    fill("#userName").with("John Smith");
    fill("#userPassword").with("password");
    click("option", withText("Librarian"));
    submit("#addUser");
    assertThat(pageSource()).contains("Welcome, Abby");
    assertThat(pageSource()).contains("Find Overdue");
    assertThat(pageSource()).contains("John Smith has been added as a librarian");
  }


}
