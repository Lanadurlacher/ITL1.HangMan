import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

public class hangman {
    private JPanel mainPanel;
    private JTextField textField1;
    private JLabel hangmanLabel;

    // Deine Buttons A–Z
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton button10;
    private JButton button11;
    private JButton button12;
    private JButton button13;
    private JButton button14;
    private JButton button15;
    private JButton button16;
    private JButton button17;
    private JButton button18;
    private JButton button19;
    private JButton button20;
    private JButton button21;
    private JButton button22;
    private JButton button23;
    private JButton button24;
    private JButton button25;
    private JButton button26;
    private JButton restartButton;

    private String wordToGuess = "PROGRAMM"; // Beispielwort
    private char[] guessedWord;
    private int wrongGuesses = 0;
    private HashSet<JButton> allButtons = new HashSet<>();

    public hangman() {
        // Initiales Wort vorbereiten
        guessedWord = new char[wordToGuess.length()];
        for (int i = 0; i < guessedWord.length; i++) {
            guessedWord[i] = '_';
        }

        // Textfeld (Anzeige des erratenen Wortes)
        textField1.setText(String.valueOf(guessedWord));
        textField1.setFont(new Font("Arial", Font.BOLD, 24));
        textField1.setHorizontalAlignment(JTextField.CENTER);
        textField1.setEditable(false);

        // Label für Fehler / später evtl. Bild
        hangmanLabel.setText("Fehler: 0");
        hangmanLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Alle Buttons in ein Set packen
        allButtons.add(button1); allButtons.add(button2); allButtons.add(button3); allButtons.add(button4);
        allButtons.add(button5); allButtons.add(button6); allButtons.add(button7); allButtons.add(button8);
        allButtons.add(button9); allButtons.add(button10); allButtons.add(button11); allButtons.add(button12);
        allButtons.add(button13); allButtons.add(button14); allButtons.add(button15); allButtons.add(button16);
        allButtons.add(button17); allButtons.add(button18); allButtons.add(button19); allButtons.add(button20);
        allButtons.add(button21); allButtons.add(button22); allButtons.add(button23); allButtons.add(button24);
        allButtons.add(button25); allButtons.add(button26);

        // Buchstaben-Zuordnung (A–Z)
        char letter = 'A';
        for (JButton b : allButtons) {
            b.setText(String.valueOf(letter));
            b.addActionListener(new GuessListener());
            styleButton(b);
            letter++;
        }

        // Restart-Button designen
        restartButton.setText("Restart");
        restartButton.setBackground(new Color(102, 204, 102));
        restartButton.setForeground(Color.WHITE);
        restartButton.setFont(new Font("Arial", Font.BOLD, 14));
        restartButton.setFocusPainted(false);
        restartButton.setBorder(new LineBorder(new Color(80, 150, 80), 2, true));
        restartButton.addActionListener(e -> resetGame());
    }

    // Abgerundeter Button-Stil
    private void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setBorder(new LineBorder(new Color(200, 150, 150), 2, true)); // true = rounded
    }

    // Listener für Buchstaben
    private class GuessListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();
            String letter = clicked.getText();

            // Button verschwinden lassen statt grau
            clicked.setVisible(false);

            if (wordToGuess.contains(letter)) {
                for (int i = 0; i < wordToGuess.length(); i++) {
                    if (wordToGuess.charAt(i) == letter.charAt(0)) {
                        guessedWord[i] = letter.charAt(0);
                    }
                }
                textField1.setText(String.valueOf(guessedWord));

                if (!String.valueOf(guessedWord).contains("_")) {
                    JOptionPane.showMessageDialog(mainPanel, "🎉 Du hast gewonnen!");
                    resetGame();
                }
            } else {
                wrongGuesses++;
                hangmanLabel.setText("Fehler: " + wrongGuesses);
                if (wrongGuesses >= 6) {
                    JOptionPane.showMessageDialog(mainPanel, "💀 Game Over! Das Wort war: " + wordToGuess);
                    resetGame();
                }
            }
        }
    }

    private void resetGame() {
        wordToGuess = "JAVA"; // neues Beispielwort
        guessedWord = new char[wordToGuess.length()];
        for (int i = 0; i < guessedWord.length; i++) {
            guessedWord[i] = '_';
        }
        textField1.setText(String.valueOf(guessedWord));
        wrongGuesses = 0;
        hangmanLabel.setText("Fehler: 0");
        for (JButton b : allButtons) {
            b.setVisible(true);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hangman");
        hangman game = new hangman();
        frame.setContentPane(game.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setVisible(true);
    }
}
