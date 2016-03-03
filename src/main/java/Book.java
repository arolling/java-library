import java.util.*;
import org.sql2o.*;

public class Book {
  private int id;
  private String title;
  private int copies;

  public int getId() {
    return id;
  }

  public int getCopies() {
    return copies;
  }

  public String getTitle() {
    return title;
  }

  public Book(String title, int copies) {
    this.title = title;
    this.copies = copies;
  }

  @Override
  public boolean equals(Object otherBook){
    if (!(otherBook instanceof Book)) {
      return false;
    } else {
      Book newBook = (Book) otherBook;
      return this.getTitle().equals(newBook.getTitle()) &&
             this.getId() == newBook.getId() && this.getCopies() == newBook.getCopies();
    }
  }

  public static List<Book> all() {
    String sql = "SELECT * FROM books ORDER BY title";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Book.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO books(title, copies) VALUES (:title, :copies)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("title", title)
        .addParameter("copies", copies)
        .executeUpdate()
        .getKey();
    }
  }

  public static Book find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM books where id=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Book.class);
    }
  }

  public void update(int copies) {
    this.copies = copies;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE books SET copies = :copies WHERE id = :id";
      con.createQuery(sql)
        .addParameter("copies", copies)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void delete() {
    copies = 0;
    try(Connection con = DB.sql2o.open()) {
    String sql = "UPDATE books SET copies = 0 WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void addAuthor(Author author) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO authors_books (author_id, book_id) VALUES (:author_id, :book_id)";
      con.createQuery(sql)
      .addParameter("author_id", author.getId())
      .addParameter("book_id", id)
      .executeUpdate();
    }
  }

  public List<Author> getAuthors() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT authors.* FROM books JOIN authors_books ON (books.id = authors_books.book_id) JOIN authors ON (authors_books.author_id = authors.id) WHERE book_id=:id ORDER BY last_name";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Author.class);
    }
  }

  public String getPrettyAuthors() {
    List<Author> authors = this.getAuthors();
    String pretty = "";
    for(int i = 0 ; i < authors.size() ; i++) {
      pretty += authors.get(i).getFirstName() + " " + authors.get(i).getLastName();
      if (i < authors.size() - 2) {
        pretty += ", ";
      } else if (i == authors.size() - 2) {
        pretty += ", and ";
      }
    }
    return pretty;
  }

  public static List<Book> searchTitle(String search) {
    search = "%" + search + "%";
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM books WHERE LOWER (title) LIKE LOWER (:search) ORDER BY title";
      return con.createQuery(sql)
        .addParameter("search", search)
        .executeAndFetch(Book.class);
    }
  }

  public Integer copiesLeft() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT count(*) FROM checkouts WHERE book_id = :id";
      Integer checkedOut = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Integer.class);
      return copies - checkedOut;
    }
  }

  public boolean isOverdue(User user) {
    java.sql.Date due_date = user.getDueDate(id);
    long currentDateLong = System.currentTimeMillis();
    java.sql.Date currentDate = new java.sql.Date(currentDateLong);
    if(due_date.before(currentDate)) {
      return true;
    }
    return false;
  }


}
