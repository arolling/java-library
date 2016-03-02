import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  protected void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/library_test", null, null);
   }

  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String bookssql = "DELETE FROM books *;";
      String authorssql = "DELETE FROM authors *;";
      String userssql = "DELETE FROM users *;";
      String authors_bookssql = "DELETE FROM authors_books *;";
      String checkoutssql = "DELETE FROM checkouts *;";
      String reading_historysql = "DELETE FROM reading_history *;";
      con.createQuery(bookssql).executeUpdate();
      con.createQuery(authorssql).executeUpdate();
      con.createQuery(userssql).executeUpdate();
      con.createQuery(authors_bookssql).executeUpdate();
      con.createQuery(checkoutssql).executeUpdate();
      con.createQuery(reading_historysql).executeUpdate();
    }
  }
}
