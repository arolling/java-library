import java.util.HashMap;
import java.util.List;
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
      model.put("authors", Author.all());
      model.put("template", "templates/welcome.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/search", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User currentUser = request.session().attribute("currentUser");
      model.put("currentUser", currentUser);
      model.put("template", "templates/search.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/search", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User currentUser = request.session().attribute("currentUser");
      String search = request.queryParams("search");
      List<Book> foundBooks = Book.searchTitle(search);
      List<Author> foundAuthors = Author.searchAuthor(search);
      model.put("foundAuthors", foundAuthors);
      model.put("foundBooks", foundBooks);
      model.put("currentUser", currentUser);
      model.put("template", "templates/search.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/books/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Book book = Book.find(Integer.parseInt(request.params("id")));
      User currentUser = request.session().attribute("currentUser");
      model.put("authors", Author.all());
      model.put("users", book.getPatrons());
      model.put("book", book);
      model.put("currentUser", currentUser);
      model.put("template", "templates/book.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
//This should be a post method, theoretically
    get("/checkout/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Book book = Book.find(Integer.parseInt(request.params("id")));
      User currentUser = request.session().attribute("currentUser");
      currentUser.checkoutBook(book.getId());

      model.put("newBook", book);
      model.put("currentUser", currentUser);
      model.put("template", "templates/welcome.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/browse", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User currentUser = request.session().attribute("currentUser");
      model.put("books", Book.all());
      model.put("currentUser", currentUser);
      model.put("template", "templates/books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/history", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      User currentUser = request.session().attribute("currentUser");

      model.put("currentUser", currentUser);
      model.put("template", "templates/history.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/return/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Book book = Book.find(Integer.parseInt(request.params("id")));
      User currentUser = request.session().attribute("currentUser");
      currentUser.returnBook(book.getId());

      model.put("returnBook", book);
      model.put("currentUser", currentUser);
      model.put("template", "templates/welcome.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/authors/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Author author = Author.find(Integer.parseInt(request.params("id")));
      User currentUser = request.session().attribute("currentUser");
      model.put("author", author);
      model.put("currentUser", currentUser);
      model.put("books", Book.all());
      model.put("template", "templates/author.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/authors/:id/addBook", (request, response) -> {
      Author author = Author.find(Integer.parseInt(request.params("id")));
      Book book = Book.find(Integer.parseInt(request.queryParams("addBook")));
      book.addAuthor(author);
      response.redirect("/authors/" + author.getId());
      return null;
    });

    post("/authors/:id/removeBook", (request, response) -> {
      Author author = Author.find(Integer.parseInt(request.params("id")));
      Integer bookid = Integer.parseInt(request.queryParams("removeBook"));
      author.deleteBook(bookid);
      response.redirect("/authors/" + author.getId());
      return null;
    });

    post("/addUser", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String newUsername = request.queryParams("userName");
      String newPassword = request.queryParams("userPassword");
      String newPermissions = request.queryParams("userPermissions");
      User newUser = new User(newUsername, newPassword, newPermissions);
      newUser.save();
      User currentUser = request.session().attribute("currentUser");
      model.put("currentUser", currentUser);
      model.put("authors", Author.all());
      model.put("newUser", newUser);
      model.put("template", "templates/welcome.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/addAuthor", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String firstName = request.queryParams("addFirst");
      String lastName = request.queryParams("addLast");
      Author newAuthor = new Author(firstName, lastName);
      newAuthor.save();
      User currentUser = request.session().attribute("currentUser");
      model.put("currentUser", currentUser);
      model.put("authors", Author.all());
      model.put("template", "templates/welcome.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/addBook", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String title = request.queryParams("addTitle");
      Integer authorId = Integer.parseInt(request.queryParams("selectAuthor"));
      Integer copies = Integer.parseInt(request.queryParams("addCopies"));
      Book newBook = new Book(title, copies);
      newBook.save();
      newBook.addAuthor(Author.find(authorId));
      User currentUser = request.session().attribute("currentUser");
      model.put("currentUser", currentUser);
      model.put("authors", Author.all());
      model.put("newBook", newBook);
      model.put("template", "templates/welcome.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/books/:id/addAuthor", (request, response) -> {
      int authorId = Integer.parseInt(request.queryParams("selectAuthor"));
      Book book = Book.find(Integer.parseInt(request.params(":id")));
      Author author = Author.find(authorId);
      book.addAuthor(author);
      response.redirect("/books/" + book.getId());
      return null;
    });

    post("/books/:id/removeAuthor", (request, response) -> {
      int authorId = Integer.parseInt(request.queryParams("removeAuthor"));
      Book book = Book.find(Integer.parseInt(request.params(":id")));
      Author author = Author.find(authorId);
      book.removeAuthor(author);
      response.redirect("/books/" + book.getId());
      return null;
    });

    post("/books/:id/changeCopies", (request, response) -> {
      int copies = Integer.parseInt(request.queryParams("addCopies"));
      Book book = Book.find(Integer.parseInt(request.params(":id")));

      book.update(copies);
      response.redirect("/books/" + book.getId());
      return null;
    });
  }
}
