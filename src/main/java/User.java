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
    String sql = "SELECT * FROM users";
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

  // public void addStudent(Student student) {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "INSERT INTO students_users (student_id, course_id, completed) VALUES (:student_id, :course_id, false)";
  //     con.createQuery(sql)
  //     .addParameter("student_id", student.getId())
  //     .addParameter("course_id", id)
  //     .executeUpdate();
  //   }
  // }

  // public List<Student> getStudents() {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "SELECT students.* FROM users JOIN students_users ON (users.id = students_users.course_id) JOIN students ON (students_users.student_id = students.id) WHERE course_id=:id";
  //     return con.createQuery(sql)
  //       .addParameter("id", id)
  //       .executeAndFetch(Student.class);
  //   }
  // }



  // public void delete() {
  //   try(Connection con = DB.sql2o.open()) {
  //   String sql = "UPDATE users SET password = 0 WHERE id = :id;";
  //     con.createQuery(sql)
  //       .addParameter("id", id)
  //       .executeUpdate();
  //   }
  // }
}
