import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class hangman {
    private JPanel mainPanel;
    private JTextField textField1;
    private JLabel hangmanLabel;

    private JButton button1, button2, button3, button4, button5, button6,
            button7, button8, button9, button10, button11, button12,
            button13, button14, button15, button16, button17, button18,
            button19, button20, button21, button22, button23, button24,
            button25, button26, restartButton;

    private JPanel JImgPanel;

    private String wordToGuess = "PROGRAMM";
    private char[] guessedWord;
    private int wrongGuesses = 0;
    private final List<JButton> letterButtons = new ArrayList<>(26);

    private static final int MAX_FRAMES = 8;
    private final Image[] frames = new Image[MAX_FRAMES];
    private ImageCanvas imageCanvas;

    private int totalLives = 8;

    private static final Color BORDER_COLOR = new Color(210, 216, 224);
    private static final Color BG_WHITE = Color.WHITE;
    private static final Color BTN_BG = new Color(245, 247, 250);
    private static final Color BTN_FG = new Color(42, 45, 55);
    private static final Color BTN_EDGE = new Color(206, 213, 222);
    private static final Color BTN_USED_BG = new Color(231, 234, 239);
    private static final Color BTN_USED_FG = new Color(150, 154, 165);

    public hangman() {
        // Rahmen + weißer Hintergrund für das Hauptpanel
        Border outer = BorderFactory.createMatteBorder(12, 12, 12, 12, BORDER_COLOR);
        Border innerPad = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        mainPanel.setBorder(new CompoundBorder(outer, innerPad));
        mainPanel.setBackground(BG_WHITE);
        mainPanel.setOpaque(true);

        // Bildfläche vorbereiten: explizit OPAQUE + WEISS
        imageCanvas = new ImageCanvas();
        imageCanvas.setOpaque(true);
        imageCanvas.setBackground(BG_WHITE);

        JImgPanel.setLayout(new BorderLayout());
        JImgPanel.setOpaque(true);
        JImgPanel.setBackground(BG_WHITE);
        JImgPanel.add(imageCanvas, BorderLayout.CENTER);
        imageCanvas.setPreferredSize(new Dimension(360, 360));

        loadHangmanImages();

        guessedWord = new char[wordToGuess.length()];
        Arrays.fill(guessedWord, '_');

        textField1.setText(String.valueOf(guessedWord));
        textField1.setFont(new Font("Arial", Font.BOLD, 24));
        textField1.setHorizontalAlignment(JTextField.CENTER);
        textField1.setEditable(false);
        textField1.setBackground(BG_WHITE);
        textField1.setBorder(BorderFactory.createLineBorder(BTN_EDGE, 1, true));

        hangmanLabel.setText("Fehler: 0 / " + totalLives);
        hangmanLabel.setFont(new Font("Arial", Font.BOLD, 16));
        hangmanLabel.setBackground(BG_WHITE);
        hangmanLabel.setOpaque(true);

        updateHangmanImage();

        letterButtons.addAll(Arrays.asList(
                button1, button2, button3, button4, button5, button6, button7, button8,
                button9, button10, button11, button12, button13, button14, button15, button16,
                button17, button18, button19, button20, button21, button22, button23, button24,
                button25, button26
        ));

        char letter = 'A';
        for (JButton b : letterButtons) {
            b.setText(String.valueOf(letter));
            styleButton(b);
            b.addActionListener(new GuessListener());
            letter++;
        }

        restartButton.setText("RESTART");
        restartButton.addActionListener(e -> resetGame());
    }

    public void setTotalLives(int lives) {
        this.totalLives = Math.max(1, lives);
        wrongGuesses = 0;
        hangmanLabel.setText("Fehler: 0 / " + totalLives);
        updateHangmanImage();
    }

    public JPanel getMainPanel() { return mainPanel; }

    private void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 16));
        b.setBackground(BTN_BG);
        b.setForeground(BTN_FG);
        b.setBorder(BorderFactory.createLineBorder(BTN_EDGE, 2, true));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
    }

    private void markButtonUsed(JButton b) {
        b.setEnabled(false);
        b.setBackground(BTN_USED_BG);
        b.setForeground(BTN_USED_FG);
    }

    private void resetButtonVisual(JButton b) {
        b.setEnabled(true);
        b.setBackground(BTN_BG);
        b.setForeground(BTN_FG);
    }

    private void loadHangmanImages() {
        for (int i = 0; i < MAX_FRAMES; i++) {
            String path = "/images/hangman" + (i + 1) + ".png";
            URL url = getClass().getResource(path);
            if (url == null)
                throw new IllegalStateException("Image not found: " + path);
            frames[i] = new ImageIcon(url).getImage();
        }
    }

    private void updateHangmanImage() {
        int idx;
        if (totalLives <= 1) {
            idx = MAX_FRAMES - 1;
        } else {
            double t = wrongGuesses / (double) (totalLives - 1);
            idx = (int) Math.round(t * (MAX_FRAMES - 1));
            idx = Math.max(0, Math.min(idx, MAX_FRAMES - 1));
        }
        imageCanvas.setImage(frames[idx]);
        JImgPanel.revalidate();
        JImgPanel.repaint();
    }

    private class GuessListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();
            String letter = clicked.getText();
            markButtonUsed(clicked);

            if (wordToGuess.contains(letter)) {
                for (int i = 0; i < wordToGuess.length(); i++) {
                    if (wordToGuess.charAt(i) == letter.charAt(0))
                        guessedWord[i] = letter.charAt(0);
                }
                textField1.setText(String.valueOf(guessedWord));
                if (!String.valueOf(guessedWord).contains("_")) {
                    JOptionPane.showMessageDialog(mainPanel, "🎉 You won!");
                    resetGame();
                }
            } else {
                wrongGuesses++;
                hangmanLabel.setText("Fehler: " + wrongGuesses + " / " + totalLives);
                updateHangmanImage();
                if (wrongGuesses >= totalLives) {
                    JOptionPane.showMessageDialog(mainPanel, "💀 Game Over! Das Wort war: " + wordToGuess);
                    resetGame();
                }
            }
        }
    }

    private void resetGame() {
        String[] pool = {"JAVA", "PROGRAMM", "DATENBANK", "SWING", "KLASSE", "OBJEKT", "VARIABLE"};
        wordToGuess = pool[(int) (Math.random() * pool.length)];
        guessedWord = new char[wordToGuess.length()];
        Arrays.fill(guessedWord, '_');
        textField1.setText(String.valueOf(guessedWord));
        wrongGuesses = 0;
        hangmanLabel.setText("Fehler: 0 / " + totalLives);
        updateHangmanImage();
        for (JButton b : letterButtons) resetButtonVisual(b);
    }

    private static class ImageCanvas extends JPanel {
        private Image image;

        public ImageCanvas() {
            setBackground(Color.WHITE);   // explizit weiß
            setOpaque(true);              // Hintergrund wird wirklich gezeichnet
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // füllt den Hintergrund mit Weiß (weil opaque)
            if (image == null) return;

            int w = getWidth(), h = getHeight();
            int imgW = image.getWidth(this), imgH = image.getHeight(this);
            if (imgW <= 0 || imgH <= 0) return;

            double scale = Math.min((w - 8) / (double) imgW, (h - 8) / (double) imgH);
            int drawW = (int) (imgW * scale), drawH = (int) (imgH * scale);
            int x = (w - drawW) / 2, y = (h - drawH) / 2;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(image, x, y, drawW, drawH, this);
            g2.dispose();
        }

        public void setImage(Image img) {
            this.image = img;
            repaint();
        }
    }
}
