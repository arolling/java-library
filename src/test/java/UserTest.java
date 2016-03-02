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


  // @Test
  // public void addStudent_addsStudentToUser() {
  //   Student myStudent = new Student("John", "12-23-2015", "History");
  //   myStudent.save();
  //
  //   User myUser = new User("Matt", "password123", "librarian");
  //   myUser.save();
  //
  //   myUser.addStudent(myStudent);
  //   Student savedStudent = myUser.getStudents().get(0);
  //   assertTrue(myStudent.equals(savedStudent));
  // }

  // @Test
  // public void getStudents_returnsAllStudents_ArrayList() {
  //   Student myStudent = new Student("John", "12-23-2015", "History");
  //   myStudent.save();
  //
  //   User myUser = new User("Matt", "password123", "librarian");
  //   myUser.save();
  //
  //   myUser.addStudent(myStudent);
  //   List savedStudents = myUser.getStudents();
  //   assertEquals(savedStudents.size(), 1);
  // }


  // @Test
  // public void delete_deletesAllUsersAndListsAssoicationes() {
  //   Student myStudent = new Student("Matt", "2010-08-15", "History");
  //   myStudent.save();
  //
  //   User myUser = new User("Matt", "password123", "librarian");
  //   myUser.save();
  //
  //   myUser.addStudent(myStudent);
  //   myUser.delete();
  //   assertEquals(myStudent.getUsers().size(), 0);
  // }

}
