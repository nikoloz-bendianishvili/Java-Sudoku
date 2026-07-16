import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	
	public SudokuFrame() {
		super("Sudoku Solver");
		
		BorderLayout layout = new BorderLayout(4, 4);
		setLayout(layout);

		JTextArea source = new JTextArea(15, 20);
		JTextArea result = new JTextArea(15, 20);
		source.setBorder(new TitledBorder("Puzzle"));
		result.setBorder(new TitledBorder("Solution"));
		add(source, BorderLayout.CENTER);
		add(result, BorderLayout.EAST);

		Box controls = Box.createHorizontalBox();
		JButton check = new JButton("Check");
		JCheckBox autoCheck = new JCheckBox("Auto Check", true);
		controls.add(check);
		controls.add(autoCheck);
		add(controls, BorderLayout.SOUTH);
		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Sudoku sudoku = new Sudoku(source.getText());
					int count = sudoku.solve();
					if(count > 0) {
						result.setText(sudoku.getSolutionText() + "\n" + "solutions: " + count + "\n" +
						"elapsed: " + sudoku.getElapsed() + "ms");
					} else {
						result.setText("There is no solution");
					}
				} catch (Exception exception) {
					result.setText("Parsing problem");
				}
			}
		});

		source.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				checkSolve();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				checkSolve();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				checkSolve();
			}

			private void checkSolve() {
				if(autoCheck.isSelected()) {
					check.doClick();
				}
			}
		});

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
