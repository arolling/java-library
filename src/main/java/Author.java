import java.util.*;
import org.sql2o.*;

public class Author {
  private int id;
  private String first_name;
  private String last_name;

  public int getId() {
    return id;
  }

  public String getLastName() {
    return last_name;
  }

  public String getFirstName() {
    return first_name;
  }

  public Author(String first_name, String last_name) {
    this.first_name = first_name;
    this.last_name = last_name;
  }

  @Override
  public boolean equals(Object otherAuthor){
    if (!(otherAuthor instanceof Author)) {
      return false;
    } else {
      Author newAuthor = (Author) otherAuthor;
      return this.getFirstName().equals(newAuthor.getFirstName()) &&
             this.getId() == newAuthor.getId() &&
             this.getLastName().equals(newAuthor.getLastName());
    }
  }

  public static List<Author> all() {
    String sql = "SELECT * FROM authors";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Author.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO authors(first_name, last_name) VALUES (:first_name, :last_name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("first_name", first_name)
        .addParameter("last_name", last_name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Author find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM authors where id=:id";
      Author course = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Author.class);
      return course;
    }
  }

  public void update(String first_name, String last_name) {
    this.first_name = first_name;
    this.last_name = last_name;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE authors SET last_name = :last_name, first_name = :first_name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("last_name", last_name)
        .addParameter("first_name", first_name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public List<Book> getBooks() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT books.* FROM authors JOIN authors_books ON (authors.id = authors_books.author_id) JOIN books ON (authors_books.book_id = books.id) WHERE author_id=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Book.class);
    }
  }

  public void deleteBook(int book_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM authors_books WHERE author_id = :author_id AND book_id = :book_id";
      con.createQuery(sql)
        .addParameter("book_id", book_id)
        .addParameter("author_id", id)
        .executeUpdate();
    }
  }

  public static List<Author> searchAuthor(String search) {
    String[] words = search.split("\\s+", 2);
    ArrayList<Author> foundauthors = new ArrayList<Author>();
    if (words.length > 1) {
      words[0] = "%" + words[0] + "%";
      words[1] = "%" + words[1] + "%";
      try(Connection con = DB.sql2o.open()) {
        String sql = "SELECT * FROM authors WHERE LOWER (first_name) LIKE LOWER (:word1) AND LOWER (last_name) LIKE LOWER (:word2)";
        return con.createQuery(sql)
          .addParameter("word1", words[0])
          .addParameter("word2", words[1])
          .executeAndFetch(Author.class);
      }
    } else {
      words[0] = "%" + words[0] + "%";
      try(Connection con = DB.sql2o.open()) {
        String sql = "SELECT * FROM authors WHERE LOWER (first_name) LIKE LOWER (:word) OR LOWER (last_name) LIKE LOWER (:word)";
        return con.createQuery(sql)
          .addParameter("word", words[0])
          .executeAndFetch(Author.class);
      }
    }
  }

  // public void addStudent(Student student) {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "INSERT INTO students_authors (student_id, course_id, completed) VALUES (:student_id, :course_id, false)";
  //     con.createQuery(sql)
  //     .addParameter("student_id", student.getId())
  //     .addParameter("course_id", id)
  //     .executeUpdate();
  //   }
  // }





  // public void delete() {
  //   try(Connection con = DB.sql2o.open()) {
  //   String sql = "UPDATE authors SET last_name = 0 WHERE id = :id;";
  //     con.createQuery(sql)
  //       .addParameter("id", id)
  //       .executeUpdate();
  //   }
  // }
}
