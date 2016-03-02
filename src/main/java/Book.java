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
    String sql = "SELECT * FROM books";
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
      Book course = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Book.class);
      return course;
    }
  }

  // public void addStudent(Student student) {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "INSERT INTO students_books (student_id, course_id, completed) VALUES (:student_id, :course_id, false)";
  //     con.createQuery(sql)
  //     .addParameter("student_id", student.getId())
  //     .addParameter("course_id", id)
  //     .executeUpdate();
  //   }
  // }

  // public List<Student> getStudents() {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "SELECT students.* FROM books JOIN students_books ON (books.id = students_books.course_id) JOIN students ON (students_books.student_id = students.id) WHERE course_id=:id";
  //     return con.createQuery(sql)
  //       .addParameter("id", id)
  //       .executeAndFetch(Student.class);
  //   }
  // }

  public void update(int copies) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE books SET copies = :copies WHERE id = :id";
      con.createQuery(sql)
        .addParameter("copies", copies)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "UPDATE books SET copies = 0 WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }
}
