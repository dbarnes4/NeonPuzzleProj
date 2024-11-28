import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PuzzleGUI extends JFrame {
	public JPanel puzzlePanel;
	public JPanel menuPanel;
	public JButton[][] buttons;
	public JLabel titleLabel;
	public int moves;
	public int size;

	// Default colors
	public Color numberColor = Color.CYAN;
	public Color borderColor = Color.CYAN;
	public Color titleColor = Color.CYAN;
	public Color menuButtonColor = Color.CYAN;

	public PuzzleGUI() {
		// Set up for main menu
		setTitle("Sliding Puzzle Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		getContentPane().setBackground(Color.BLACK);

		// Panel for title and image, using vertical layout
		JPanel titleImagePanel = new JPanel();
		titleImagePanel.setLayout(new BoxLayout(titleImagePanel, BoxLayout.Y_AXIS));
		titleImagePanel.setBackground(Color.BLACK);

		// Title Label
		titleLabel = new JLabel("Neon Puzzle Slide", SwingConstants.CENTER);
		titleLabel.setForeground(titleColor);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Set up the image label with the image
		ImageIcon image = new ImageIcon("puzzlePiece.jpg");
		JLabel imageLabel = new JLabel(image);
		imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Add title and image to the panel
		titleImagePanel.add(titleLabel);
		titleImagePanel.add(Box.createRigidArea(new Dimension(0, 10)));
		titleImagePanel.add(imageLabel);

		add(titleImagePanel, BorderLayout.NORTH);

		// Menu panel with buttons
		menuPanel = new JPanel();
		menuPanel.setBackground(Color.BLACK);
		JButton selectPuzzleButton = new JButton("Select Puzzle");
		JButton unsolvedPuzzlesButton = new JButton("Show Unsolved Puzzles");
		JButton exitButton = new JButton("Exit");
		JButton changeColorButton = new JButton("Change Colors"); // New color change button

		// Set button colors
		setButtonStyle(selectPuzzleButton);
		setButtonStyle(unsolvedPuzzlesButton);
		setButtonStyle(exitButton);
		setButtonStyle(changeColorButton);

		menuPanel.add(selectPuzzleButton);
		menuPanel.add(unsolvedPuzzlesButton);
		menuPanel.add(changeColorButton);
		menuPanel.add(exitButton);
		add(menuPanel, BorderLayout.SOUTH);

		// Puzzle panel
		puzzlePanel = new JPanel();
		puzzlePanel.setBackground(Color.BLACK);
		add(puzzlePanel, BorderLayout.CENTER);

		// Action listeners
		selectPuzzleButton.addActionListener(e -> showPuzzleSizeDialog());
		unsolvedPuzzlesButton.addActionListener(e -> showUnsolvedPuzzles());
		changeColorButton.addActionListener(e -> changeColors());
		exitButton.addActionListener(e -> System.exit(0));

		// Initialize the frame
		setSize(400, 400);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void setButtonStyle(JButton button) {
		button.setBackground(Color.BLACK);
		button.setForeground(menuButtonColor);
		button.setOpaque(true);
		button.setBorder(new LineBorder(borderColor, 2));
	}

	public void showPuzzleSizeDialog() {
		String[] options = { "3x3", "4x4", "5x5" };
		String choice = (String) JOptionPane.showInputDialog(this, "Select puzzle size:", "Puzzle Size",
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (choice != null) {
			switch (choice) {
			case "3x3":
				size = 3;
				break;
			case "4x4":
				size = 4;
				break;
			case "5x5":
				size = 5;
				break;
			}
			Puzzle.puzzleSelector(size);
			createPuzzleGrid();
		}
	}

	public void createPuzzleGrid() {
		puzzlePanel.removeAll();
		puzzlePanel.setLayout(new GridLayout(size, size));
		buttons = new JButton[size][size];

		// Populate the grid with buttons
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				buttons[i][j] = new JButton(Puzzle.puzzle[i][j]);
				buttons[i][j].setForeground(numberColor);
				if (!Puzzle.puzzle[i][j].equals(" ")) {
					buttons[i][j].addActionListener(new PuzzleButtonListener(i, j));
					buttons[i][j].setBackground(Color.BLACK);
				} else {
					buttons[i][j].setBackground(Color.DARK_GRAY);
				}
				buttons[i][j].setOpaque(true);
				buttons[i][j].setBorder(new LineBorder(borderColor, 2));
				puzzlePanel.add(buttons[i][j]);
			}
		}
		puzzlePanel.revalidate();
		puzzlePanel.repaint();
	}

	public void showUnsolvedPuzzles() {
		ArrayList<String[][]> unsolvedPuzzlesList = (ArrayList<String[][]>) Puzzle.unsolvedPuzzles;

		String[] puzzleOptions = new String[unsolvedPuzzlesList.size()];

		// Prepare string representations of each unsolved puzzle for the dialog box
		for (int i = 0; i < unsolvedPuzzlesList.size(); i++) {
			StringBuilder puzzleString = new StringBuilder();
			String[][] puzzle = unsolvedPuzzlesList.get(i);
			for (String[] row : puzzle) {
				for (String cell : row) {
					puzzleString.append(cell).append(" ");
				}
				puzzleString.append("\n");
			}
			puzzleOptions[i] = "Puzzle " + (i + 1) + ":\n" + puzzleString.toString();
		}

		if (puzzleOptions.length > 0) {
			String selectedPuzzle = (String) JOptionPane.showInputDialog(this, "Select an unsolved puzzle to load:",
					"Unsolved Puzzles", JOptionPane.QUESTION_MESSAGE, null, puzzleOptions, puzzleOptions[0]);

			// Load unfinished puzzles
			if (selectedPuzzle != null) {
				int puzzleIndex = -1;
				for (int i = 0; i < puzzleOptions.length; i++) {
					if (selectedPuzzle.contains("Puzzle " + (i + 1))) {
						puzzleIndex = i;
						break;
					}
				}

				if (puzzleIndex != -1) {
					Puzzle.puzzle = unsolvedPuzzlesList.get(puzzleIndex);
					size = Puzzle.puzzle.length;
					createPuzzleGrid();
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "No unsolved puzzles available.");
		}
	}

	public void changeColors() {
		if (size > 0) { // Check if a puzzle has been selected
			Color newNumberColor = JColorChooser.showDialog(this, "Select Number Color", numberColor);
			Color newBorderColor = JColorChooser.showDialog(this, "Select Border Color", borderColor);
			Color newTitleColor = JColorChooser.showDialog(this, "Select Title Color", titleColor);
			Color newMenuButtonColor = JColorChooser.showDialog(this, "Select Menu Button Color", menuButtonColor);

			if (newNumberColor != null)
				numberColor = newNumberColor;
			if (newBorderColor != null)
				borderColor = newBorderColor;
			if (newTitleColor != null)
				titleColor = newTitleColor;
			if (newMenuButtonColor != null)
				menuButtonColor = newMenuButtonColor;

			titleLabel.setForeground(titleColor); // Update title color

			// Update menu button colors and borders
			for (Component comp : menuPanel.getComponents()) {
				if (comp instanceof JButton) {
					JButton button = (JButton) comp;
					button.setForeground(menuButtonColor); // Update text color
					button.setBorder(new LineBorder(borderColor, 2)); // Set border color
				}
			}

			// Recreate the puzzle grid to apply the new colors
			createPuzzleGrid();
		} else {
			JOptionPane.showMessageDialog(this, "Please select a puzzle size first.");
		}
	}

	public class PuzzleButtonListener implements ActionListener {
		public final int row;
		public final int col;

		public PuzzleButtonListener(int row, int col) {
			this.row = row;
			this.col = col;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (Puzzle.checkMove(Puzzle.puzzle[row][col])) {
				if (Puzzle.makeMove(Puzzle.puzzle[row][col])) {
					moves++;
					createPuzzleGrid(); // Update the grid after a move
					if (Puzzle.isSolved()) {
						JOptionPane.showMessageDialog(PuzzleGUI.this, "Puzzle Solved!  Moves: " + moves);
					}
				} else {
					JOptionPane.showMessageDialog(PuzzleGUI.this, "Select a different piece.");
				}
			}
		}
	}

	public static void main(String[] args) {
		new PuzzleGUI();
	}
}
