import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class BookTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Book.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfDescriptionsAretheSame() {
    Book firstBook = new Book("The Stand", 1);
    Book secondBook = new Book("The Stand", 1);
    assertTrue(firstBook.equals(secondBook));
  }

  @Test
  public void save_savesObjectIntoDatabase() {
    Book myBook = new Book("The Stand", 1);
    myBook.save();
    Book savedBook = Book.all().get(0);
    assertTrue(savedBook.equals(myBook));
  }

  @Test
  public void save_assignsIdToObject() {
    Book myBook = new Book("The Stand", 1);
    myBook.save();
    Book savedBook = Book.all().get(0);
    assertEquals(myBook.getId(), savedBook.getId());
  }

  @Test
  public void find_findsBookInDatabase_true() {
    Book myBook = new Book("The Stand", 1);
    myBook.save();
    Book savedBook = Book.find(myBook.getId());
    assertTrue(myBook.equals(savedBook));
  }

  @Test
  public void update_updatesBookNameAndNumber() {
    Book myBook = new Book("The Stand", 1);
    myBook.save();
    myBook.update(102);
    assertEquals(Book.all().get(0).getCopies(), 102);
  }

  // @Test
  // public void addStudent_addsStudentToBook() {
  //   Student myStudent = new Student("John", "12-23-2015", "History");
  //   myStudent.save();
  //
  //   Book myBook = new Book("The Stand", 1);
  //   myBook.save();
  //
  //   myBook.addStudent(myStudent);
  //   Student savedStudent = myBook.getStudents().get(0);
  //   assertTrue(myStudent.equals(savedStudent));
  // }

  // @Test
  // public void getStudents_returnsAllStudents_ArrayList() {
  //   Student myStudent = new Student("John", "12-23-2015", "History");
  //   myStudent.save();
  //
  //   Book myBook = new Book("The Stand", 1);
  //   myBook.save();
  //
  //   myBook.addStudent(myStudent);
  //   List savedStudents = myBook.getStudents();
  //   assertEquals(savedStudents.size(), 1);
  // }


  // @Test
  // public void delete_deletesAllBooksAndListsAssoicationes() {
  //   Student myStudent = new Student("Matt", "2010-08-15", "History");
  //   myStudent.save();
  //
  //   Book myBook = new Book("The Stand", 1);
  //   myBook.save();
  //
  //   myBook.addStudent(myStudent);
  //   myBook.delete();
  //   assertEquals(myStudent.getBooks().size(), 0);
  // }

}
