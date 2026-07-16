import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;

public class SQLTests {

    private static Connection testConnection;
    private MetropolisTableModel model;

    @BeforeAll
    public static void setUpTable() throws SQLException {
        testConnection = DriverManager.getConnection("jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1", "sa", "");

        try (Statement statement = testConnection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS metropolises (" +
                    "metropolis VARCHAR(64), " +
                    "continent VARCHAR(64), " +
                    "population BIGINT)");
        }
    }

    @BeforeEach
    public void insertData() throws SQLException {
        try (Statement statement = testConnection.createStatement()) {
            statement.execute("DELETE FROM metropolises;");

            statement.execute("INSERT INTO metropolises VALUES" +
                    "('Mumbai','Asia',20400000)," +
                    "('New York','North America',21295000)," +
                    "('San Francisco','North America',5780000)," +
                    "('London','Europe',8580000)," +
                    "('Rome','Europe',2715000)," +
                    "('Melbourne','Australia',3900000)," +
                    "('San Jose','North America',7354555)," +
                    "('Rostov-on-Don','Europe',1052000);");
        }

        model = new MetropolisTableModel(testConnection);
    }

    @Test
    public void testGetters() throws SQLException {
        model.search("", "", "", 0, 0);

        assertEquals(3, model.getColumnCount());
        assertEquals(8, model.getRowCount());
        assertEquals("Metropolis", model.getColumnName(0));
        assertNull(model.getColumnName(4));
        assertEquals("New York", model.getValueAt(1, 0));
        assertNull(model.getValueAt(-1, 0));
        assertNull(model.getValueAt(0, -1));
    }


    @Test
    public void testValidAdd() throws SQLException {
        model.add("Barcelona", "Europe", "5000000");

        assertEquals(1, model.getRowCount());
        assertEquals("Barcelona", model.getValueAt(0, 0));
        assertEquals("Europe", model.getValueAt(0, 1));
        assertEquals(5000000L, model.getValueAt(0, 2));
    }

    @Test
    public void testManyValidAdds() throws SQLException {
        model.add("Barcelona", "Europe", "5000000");

        assertEquals(1, model.getRowCount());
        assertEquals("Barcelona", model.getValueAt(0, 0));
        assertEquals("Europe", model.getValueAt(0, 1));
        assertEquals(5000000L, model.getValueAt(0, 2));

        model.add("Madrid", "Europe", "8000000");

        assertEquals(1, model.getRowCount());
        assertEquals("Madrid", model.getValueAt(0, 0));
        assertEquals("Europe", model.getValueAt(0, 1));
        assertEquals(8000000L, model.getValueAt(0, 2));

        model.add("Munich", "Europe", "6000000");

        assertEquals(1, model.getRowCount());
        assertEquals("Munich", model.getValueAt(0, 0));
        assertEquals("Europe", model.getValueAt(0, 1));
        assertEquals(6000000L, model.getValueAt(0, 2));
    }

    @Test
    public void testInvalidAdd() throws SQLException {
        model.add("Barcelona", "", "5000000");

        assertEquals(0, model.getRowCount());
    }

    @Test
    public void testMixedValidAndInvalidAdds() throws SQLException {
        model.add("Barcelona", "Europe", "");
        assertEquals(0, model.getRowCount());

        model.add("Madrid", "Europe", "8000000");
        assertEquals(1, model.getRowCount());
        assertEquals("Madrid", model.getValueAt(0, 0));
        assertEquals("Europe", model.getValueAt(0, 1));
        assertEquals(8000000L, model.getValueAt(0, 2));

        model.add("Munich", "", "6000000");
        assertEquals(1, model.getRowCount());
        assertEquals("Madrid", model.getValueAt(0, 0));
        assertEquals("Europe", model.getValueAt(0, 1));
        assertEquals(8000000L, model.getValueAt(0, 2));

        model.add(null, "Europe", "550000");
        assertEquals(1, model.getRowCount());
        assertEquals("Madrid", model.getValueAt(0, 0));
        assertEquals("Europe", model.getValueAt(0, 1));
        assertEquals(8000000L, model.getValueAt(0, 2));

        model.add("Manchester", "Europe", "550000");
        assertEquals(1, model.getRowCount());
        assertEquals("Manchester", model.getValueAt(0, 0));
        assertEquals("Europe", model.getValueAt(0, 1));
        assertEquals(550000L, model.getValueAt(0, 2));

        model.add("Barcelona", "  ", "NotANumber");
        assertEquals(1, model.getRowCount());
        assertEquals("Manchester", model.getValueAt(0, 0));
        assertEquals("Europe", model.getValueAt(0, 1));
        assertEquals(550000L, model.getValueAt(0, 2));

        model.add("Barcelona", "Europe", "NotANumber");
        assertEquals(1, model.getRowCount());
        assertEquals("Manchester", model.getValueAt(0, 0));
        assertEquals("Europe", model.getValueAt(0, 1));
        assertEquals(550000L, model.getValueAt(0, 2));
    }

    @Test
    public void testEmptySearch() throws SQLException {
        model.search("", "", "", 0, 0);

        assertEquals(8, model.getRowCount());

        assertEquals("Mumbai", model.getValueAt(0, 0));
        assertEquals("Asia", model.getValueAt(0, 1));
        assertEquals(20400000L, model.getValueAt(0, 2));

        assertEquals("Rostov-on-Don", model.getValueAt(7, 0));
        assertEquals("Europe", model.getValueAt(7, 1));
        assertEquals(1052000L, model.getValueAt(7, 2));
    }

    @Test
    public void testNullSearch() throws SQLException {
        model.search(null, null, null, 1, 1);

        assertEquals(8, model.getRowCount());

        assertEquals("New York", model.getValueAt(1, 0));
        assertEquals("North America", model.getValueAt(1, 1));
        assertEquals(21295000L, model.getValueAt(1, 2));


        assertEquals("London", model.getValueAt(3, 0));
        assertEquals("Europe", model.getValueAt(3, 1));
        assertEquals(8580000L, model.getValueAt(3, 2));
    }

    @Test
    public void testMatchAndPopulationSearches() throws SQLException {
        model.search("", "Europe", "2000000", 0, 0);
        assertEquals(2, model.getRowCount());
        assertEquals("London", model.getValueAt(0, 0));
        assertEquals("Rome", model.getValueAt(1, 0));

        model.search("San", "North", "5000000", 0, 1);
        assertEquals(2, model.getRowCount());
        assertEquals("San Francisco", model.getValueAt(0, 0));
        assertEquals("San Jose", model.getValueAt(1, 0));

        model.search("", "Europe", "50000000", 1, 1);
        assertEquals(3, model.getRowCount());
        assertEquals("London", model.getValueAt(0, 0));
        assertEquals("Rome", model.getValueAt(1, 0));
        assertEquals(1052000L, model.getValueAt(2, 2));

        model.search("", "Amer", "7000000", 0, 1);
        assertEquals(2, model.getRowCount());
        assertEquals("New York", model.getValueAt(0, 0));
        assertEquals("San Jose", model.getValueAt(1, 0));

        model.search("London", "Asia", "", 0, 0);
        assertEquals(0, model.getRowCount());
    }

    @Test
    public void testSearchWithAdds() throws SQLException {
        model.search("", "Euro", "", 0, 1);
        assertEquals(3, model.getRowCount());
        assertEquals("London", model.getValueAt(0, 0));

        model.add("Berlin", "Europe", "6000000");


        model.search("", "Euro", "", 0, 1);
        assertEquals(4, model.getRowCount());
        assertEquals("London", model.getValueAt(0, 0));
        assertEquals("Berlin", model.getValueAt(3, 0));
    }

}
