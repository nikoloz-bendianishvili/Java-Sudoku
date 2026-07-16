import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.sql.*;
import java.util.Objects;

public class MetropolisTableModel extends AbstractTableModel {

    private final int NUM_COLS = 3;
    private String[] columns = {"Metropolis", "Continent", "Population"};
    private ArrayList<Metropolis> rows;

    private Connection connection;
    private String username = "root";
    private String password = "";
    private String server = "localhost:3306";

    private String database = "MyDataBase";


    /**
     * Constructs MetropolisTableModel and establishes connection to MySQL
     */
    public MetropolisTableModel() {
        rows = new ArrayList<>();

        try {
            String url = "jdbc:mysql://" + server + "/" + database;
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for testing
     * @param testConnection
     */
    public MetropolisTableModel(Connection testConnection) {
        this.rows = new ArrayList<>();
        this.connection = testConnection;
    }


    /**
     * Returns the number of rows in the model. A JTable uses this method
     * to determine how many rows it should display.
     *
     * @return the number of rows in the model
     */
    @Override
    public int getRowCount() {
        return rows.size();
    }


    /**
     * Returns the number of columns in the model. A JTable uses this method
     * to determine how many columns it should create and display on initialization.
     *
     * @return the number of columns in the model
     */
    @Override
    public int getColumnCount() {
        return NUM_COLS;
    }


    /**
     * Returns the value for the cell at columnIndex and rowIndex.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex >= rows.size() || rowIndex < 0 || columnIndex < 0 || columnIndex >= NUM_COLS) {
            return null;
        }
        Metropolis curr = rows.get(rowIndex);
        if(columnIndex == 0) {
            return curr.getMetropolis();
        } else if(columnIndex == 1) {
            return curr.getContinent();
        } else {
            return curr.getPopulation();
        }
    }


    /**
     * Returns the name of the column at columnIndex.
     *
     * @param columnIndex the index of the column
     * @return the name of the column
     */
    @Override
    public String getColumnName(int columnIndex) {
        if(columnIndex >= NUM_COLS) {
            return null;
        }
        return columns[columnIndex];
    }


    /**
     * Searches metropolises matching the given criteria in database and updates table
     * @param metropolis the name of metropolis for searching (can be exact or partial)
     * @param continent the name of continent for searching (can be exact or partial)
     * @param population the number of population for searching (can be more or less)
     * @param populationDropdownIndex 0 for "Population Larger Than", 1 for "Population Smaller Than"
     * @param matchDropdownIndex 0 for "Exact Match", 1 for "Partial Match"
     * @throws SQLException if a database access error occurs
     */
    public void search(String metropolis, String continent, String population,
                       int populationDropdownIndex, int matchDropdownIndex) throws SQLException {
        String command = "SELECT * FROM metropolises";
        ArrayList<String> conditions = new ArrayList<>();
        ArrayList<Object> parameters = new ArrayList<>();
        boolean isExactMatch = (matchDropdownIndex == 0);

        if(metropolis != null  && !metropolis.isEmpty()) {
            if(isExactMatch) {
                conditions.add("metropolis = ?");
                parameters.add(metropolis);
            } else {
                conditions.add("metropolis LIKE ?");
                parameters.add("%" + metropolis + "%");
            }
        }

        if(continent != null  && !continent.isEmpty()) {
            if(isExactMatch) {
                conditions.add("continent = ?");
                parameters.add(continent);
            } else {
                conditions.add("continent LIKE ?");
                parameters.add("%" + continent + "%");
            }
        }

        if(population != null  && !population.isEmpty()) {
            try {
                long populationLong = Long.parseLong(population);
                if(populationDropdownIndex == 0) {
                    conditions.add("population >= ?");
                } else {
                    conditions.add("population < ?");
                }
                parameters.add(populationLong);
            } catch (NumberFormatException e) {
                System.out.println("Incorrect population");
            }
        }

        if(!conditions.isEmpty()) {
            command += " WHERE " + String.join(" AND ", conditions);
        }

        PreparedStatement statement = connection.prepareStatement(command);
        for(int i = 0; i < parameters.size(); i++) {
            statement.setObject(i + 1, parameters.get(i));
        }

        rows.clear();
        ResultSet set = statement.executeQuery();
        while(set.next()) {
            String met = set.getString("metropolis");
            String con = set.getString("continent");
            long pop = set.getLong("population");
            rows.add(new Metropolis(met, con, pop));
        }

        fireTableDataChanged();
    }

    /**
     * Adds a new metropolis to the database and updates the table to display only the newly added row.
     * @param metropolis the name of the metropolis to add
     * @param continent the continent where the metropolis is located
     * @param population the population of the metropolis
     * @throws SQLException if a database access error occurs
     */
    public void add(String metropolis, String continent, String population) throws SQLException {
        if (metropolis == null || metropolis.trim().isEmpty() ||
                continent == null || continent.trim().isEmpty() ||
                population == null || population.trim().isEmpty()) {
            return;
        }

        try {
            long numPopulation = Long.parseLong(population);

            String command = "INSERT INTO metropolises (metropolis, continent, population) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(command);
            statement.setString(1, metropolis);
            statement.setString(2, continent);
            statement.setLong(3, numPopulation);

            statement.executeUpdate();

            rows.clear();
            rows.add(new Metropolis(metropolis, continent, numPopulation));
            fireTableDataChanged();
        } catch (NumberFormatException e) {
            return;
        }
    }
}
