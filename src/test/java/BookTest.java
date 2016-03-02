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

  @Test
  public void addAuthor_addsAuthorToBook() {
    Author myAuthor = new Author("Stephen", "King");
    myAuthor.save();

    Book myBook = new Book("The Stand", 1);
    myBook.save();

    myBook.addAuthor(myAuthor);
    Author savedAuthor = myBook.getAuthors().get(0);
    assertTrue(myAuthor.equals(savedAuthor));
  }

  @Test
  public void getAuthors_returnsAllAuthors_ArrayList() {
    Author myAuthor = new Author("Stephen", "King");
    myAuthor.save();

    Book myBook = new Book("The Stand", 1);
    myBook.save();

    myBook.addAuthor(myAuthor);
    assertEquals(myBook.getAuthors().size(), 1);
  }


  @Test
  public void delete_SetsCopiesToZero() {
    Book myBook = new Book("The Stand", 1);
    myBook.save();

    myBook.delete();
    Book savedBook = Book.find(myBook.getId());
    assertEquals(myBook.getCopies(), 0);
    assertTrue(myBook.equals(savedBook));
  }

  @Test
  public void searchTitle_searchesForMatchingTitles() {
    Book myBook = new Book("The Stand", 1);
    myBook.save();
    assertTrue(Book.searchTitle("stand").contains(myBook));
  }

}
