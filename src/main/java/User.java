import java.util.*;
import org.sql2o.*;

public class User {
  private int id;
  private String name;
  private String password;
  private String permissions;

  public int getId() {
    return id;
  }

  public String getPassword() {
    return password;
  }

  public String getName() {
    return name;
  }

  public String getPermissions() {
    return permissions;
  }

  public User(String name, String password, String permissions) {
    this.name = name;
    this.password = password;
    this.permissions = permissions;
  }

  @Override
  public boolean equals(Object otherUser){
    if (!(otherUser instanceof User)) {
      return false;
    } else {
      User newUser = (User) otherUser;
      return this.getName().equals(newUser.getName()) &&
             this.getId() == newUser.getId() &&
             this.getPassword().equals(newUser.getPassword()) &&
             this.getPermissions().equals(newUser.getPermissions());
    }
  }

  public static List<User> all() {
    String sql = "SELECT * FROM users ORDER BY id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(User.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO users(name, password, permissions) VALUES (:name, :password, :permissions)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", name)
        .addParameter("password", password)
        .addParameter("permissions", permissions)
        .executeUpdate()
        .getKey();
    }
  }

  public static User find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM users where id=:id";
      User course = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(User.class);
      return course;
    }
  }

  public void updatePassword(String password) {
    this.password = password;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE users SET password = :password WHERE id = :id";
      con.createQuery(sql)
        .addParameter("password", password)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void updatePermissions(String permissions) {
    this.permissions = permissions;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE users SET permissions = :permissions WHERE id = :id";
      con.createQuery(sql)
        .addParameter("permissions", permissions)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void checkoutBook(int book_id) {
    long currentDate = System.currentTimeMillis();
    currentDate -= (long) 2069900000; // for testing only
    long dueDate = currentDate + (long)259200000;
    java.sql.Date checkoutdate = new java.sql.Date(currentDate);
    java.sql.Date due_date = new java.sql.Date(dueDate);
    try(Connection con = DB.sql2o.open()) {
      String checkoutsql = "INSERT INTO checkouts (user_id, book_id, due_date) VALUES (:user_id, :book_id, :date)";
      con.createQuery(checkoutsql)
        .addParameter("user_id", id)
        .addParameter("book_id", book_id)
        .addParameter("date", due_date)
        .executeUpdate();

      String historysql = "INSERT INTO reading_history (user_id, book_id, checkout_date) VALUES (:user_id, :book_id, :date)";
      con.createQuery(historysql)
        .addParameter("user_id", id)
        .addParameter("book_id", book_id)
        .addParameter("date", checkoutdate)
        .executeUpdate();
    }
  }

  public List<Book> getCheckedOutBooks() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT books.* FROM users JOIN checkouts ON (users.id = checkouts.user_id) JOIN books ON (checkouts.book_id = books.id) WHERE user_id=:id ORDER BY checkouts.due_date";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Book.class);
    }
  }

  public List<Book> getHistory() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT books.* FROM users JOIN reading_history ON (users.id = reading_history.user_id) JOIN books ON (reading_history.book_id = books.id) WHERE user_id=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Book.class);
    }
  }



  public java.sql.Date getDueDate(int book_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT due_date FROM checkouts WHERE book_id = :book_id AND user_id = :user_id";
      return con.createQuery(sql)
        .addParameter("book_id", book_id)
        .addParameter("user_id", id)
        .executeAndFetchFirst(java.sql.Date.class);
      }
  }

  public void returnBook(int book_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM checkouts WHERE book_id = :book_id AND user_id = :user_id";
      con.createQuery(sql)
        .addParameter("book_id", book_id)
        .addParameter("user_id", id)
        .executeUpdate();
    }
  }

  public static List<User> findOverdue() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT users.* FROM checkouts JOIN users ON (users.id = checkouts.user_id) WHERE due_date < CURRENT_DATE";
      return con.createQuery(sql)
        .executeAndFetch(User.class);
    }
  }

  public java.sql.Date getHistoryDate(int book_id) {
    // this should only be run on the list returned from getHistory
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT checkout_date FROM reading_history WHERE book_id = :book_id AND user_id = :user_id";
      return con.createQuery(sql)
        .addParameter("book_id", book_id)
        .addParameter("user_id", id)
        .executeAndFetchFirst(java.sql.Date.class);
    }
  }
}
