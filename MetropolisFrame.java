import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MetropolisFrame extends JFrame {

    private JTable table;

    private JTextField writeMetropolis;
    private JTextField writeContinent;
    private JTextField writePopulation;

    private JButton add;
    private JButton search;

    JComboBox<String> populationDropdown;
    JComboBox<String> matchDropdown;

    public MetropolisFrame() {
        table = new JTable();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MetropolisTableModel model = new MetropolisTableModel();
        table.setModel(model);
        setSize(800, 600);

        writeMetropolis  = new JTextField(15);
        writeContinent  = new JTextField(15);
        writePopulation = new JTextField(15);

        add = new JButton("Add");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    model.add(writeMetropolis.getText(),
                            writeContinent.getText(),
                            writePopulation.getText());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        search = new JButton("Search");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    model.search(writeMetropolis.getText(),
                            writeContinent.getText(),
                            writePopulation.getText(),
                            populationDropdown.getSelectedIndex(),
                            matchDropdown.getSelectedIndex());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        String[] populationOptions = {"Population Larger Than", "Population Smaller Than"};
        populationDropdown = new JComboBox<>(populationOptions);

        String[] matchOptions = {"Exact Match", "Partial Match"};
        matchDropdown  = new JComboBox<>(matchOptions);

        JLabel metropolisLabel = new JLabel("Metropolis");
        JLabel continentLabel = new JLabel("Continent");
        JLabel populationLabel = new JLabel("Population");

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel top = new JPanel();
        top.add(metropolisLabel);
        top.add(writeMetropolis);
        top.add(continentLabel);
        top.add(writeContinent);
        top.add(populationLabel);
        top.add(writePopulation);

        Box right = Box.createVerticalBox();
        right.add(add);
        right.add(Box.createVerticalStrut(10));
        right.add(search);
        right.add(Box.createVerticalStrut(20));

        JPanel searchOptionsPanel = new JPanel();
        searchOptionsPanel.setLayout(new BoxLayout(searchOptionsPanel, BoxLayout.Y_AXIS));
        searchOptionsPanel.setBorder(BorderFactory.createTitledBorder("Search Options"));
        searchOptionsPanel.add(populationDropdown);
        searchOptionsPanel.add(Box.createVerticalStrut(10));
        searchOptionsPanel.add(matchDropdown);
        right.add(searchOptionsPanel);

        this.add(top, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(right, BorderLayout.EAST);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MetropolisFrame();
    }
}
