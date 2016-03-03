import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class UserTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(User.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfDescriptionsAretheSame() {
    User firstUser = new User("Matt", "password123", "librarian");
    User secondUser = new User("Matt", "password123", "librarian");
    assertTrue(firstUser.equals(secondUser));
  }

  @Test
  public void save_savesObjectIntoDatabase() {
    User myUser = new User("Matt", "password123", "librarian");
    myUser.save();
    User savedUser = User.all().get(0);
    assertTrue(savedUser.equals(myUser));
  }

  @Test
  public void save_assignsIdToObject() {
    User myUser = new User("Matt", "password123", "librarian");
    myUser.save();
    User savedUser = User.all().get(0);
    assertEquals(myUser.getId(), savedUser.getId());
  }

  @Test
  public void find_findsUserInDatabase_true() {
    User myUser = new User("Matt", "password123", "librarian");
    myUser.save();
    User savedUser = User.find(myUser.getId());
    assertTrue(myUser.equals(savedUser));
  }

  @Test
  public void updatePassword_updatesUserPassword() {
    User myUser = new User("Matt", "password123", "librarian");
    myUser.save();
    myUser.updatePassword("102");
    User savedUser = User.find(myUser.getId());
    assertEquals(myUser, savedUser);
  }

  @Test
  public void updatePermissions_updatesUserPermissions() {
    User myUser = new User("Matt", "password123", "librarian");
    myUser.save();
    myUser.updatePermissions("patron");
    User savedUser = User.find(myUser.getId());
    assertEquals(myUser, savedUser);
  }

  @Test
  public void checkoutBook_assignsBookToUser() {
    User myUser = new User("Matt", "password123", "patron");
    myUser.save();

    Book myBook = new Book("The Stand", 1);
    myBook.save();

    myUser.checkoutBook(myBook.getId());

    assertTrue(myUser.getCheckedOutBooks().get(0).equals(myBook));
    assertTrue(myUser.getHistory().contains(myBook));
  }

  @Test
  public void getDueDate_returnsCopyDueDate() {
    User myUser = new User("Matt", "password123", "patron");
    myUser.save();

    Book myBook = new Book("The Stand", 1);
    myBook.save();

    myUser.checkoutBook(myBook.getId());
    //assertEquals(0, myUser.getDueDate(myBook.getId()));
    assertTrue(myUser.getDueDate(myBook.getId()) instanceof java.sql.Date);
  }

  @Test
  public void returnBook_removesEntryFromCheckouts() {
    User myUser = new User("Matt", "password123", "patron");
    myUser.save();

    Book myBook = new Book("The Stand", 1);
    myBook.save();

    myUser.checkoutBook(myBook.getId());
    myUser.returnBook(myBook.getId());
    assertEquals(0, myUser.getCheckedOutBooks().size());
    assertTrue(myUser.getHistory().contains(myBook));
  }

  @Test
  public void findOverdue_returnsListOfUsersWithOverdueBooks() {
    User myUser = new User("Matt", "password123", "patron");
    myUser.save();
    User myUser2 = new User("Abby", "password123", "patron");
    myUser2.save();
    Book myBook = new Book("The Stand", 1);
    myBook.save();
    myUser.checkoutBook(myBook.getId());
    assertEquals(User.findOverdue().get(0).getName(), myUser.getName());
    assertTrue(User.findOverdue().contains(myUser));
  }


  @Test
  public void getHistoryDate_returnsDateCheckedOut() {
    User myUser = new User("Matt", "password123", "patron");
    myUser.save();

    Book myBook = new Book("The Stand", 1);
    myBook.save();
    myUser.checkoutBook(myBook.getId());
    myUser.returnBook(myBook.getId());
    //assertTrue(myUser.getDueDate(myBook.getId()) instanceof java.sql.Date);
    assertTrue(myUser.getHistoryDate(myBook.getId()) instanceof java.sql.Date);
    //assertEquals(0, myUser.getHistoryDate(myBook.getId()));
  }


}
