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

    // Swing UI Designer fields (keep these as they are in your .form)
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

    // Panel from Designer where the image should be drawn
    private JPanel JImgPanel;

    private String wordToGuess = "PROGRAMM"; // example word
    private char[] guessedWord;
    private int wrongGuesses = 0;

    // Keep a deterministic order (A..Z). Do NOT use a Set (HashSet scrambles order).
    private final List<JButton> letterButtons = new ArrayList<>(26);

    // Image handling (store raw Images; scale during painting)
    private static final int MAX_FRAMES = 8; // hangman1..hangman8
    private final Image[] frames = new Image[MAX_FRAMES];

    // Our custom image canvas that paints centered & scaled into JImgPanel
    private ImageCanvas imageCanvas;

    // Neutral UI colors
    private static final Color BORDER_COLOR = new Color(210, 216, 224);
    private static final Color BG_WHITE = Color.WHITE;
    private static final Color BTN_BG = new Color(245, 247, 250);
    private static final Color BTN_FG = new Color(42, 45, 55);
    private static final Color BTN_EDGE = new Color(206, 213, 222);
    private static final Color BTN_USED_BG = new Color(231, 234, 239);
    private static final Color BTN_USED_FG = new Color(150, 154, 165);

    public hangman() {
        // ---- A) Border around the main panel + WHITE background (kills beige) ----
        Border outer = BorderFactory.createMatteBorder(12, 12, 12, 12, BORDER_COLOR);
        Border innerPad = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        mainPanel.setBorder(new CompoundBorder(outer, innerPad));
        mainPanel.setBackground(BG_WHITE); // ensure pure white

        // ---- B) Prepare the dedicated image panel (also WHITE) ----
        imageCanvas = new ImageCanvas();
        imageCanvas.setOpaque(true);
        imageCanvas.setBackground(BG_WHITE); // no beige here
        JImgPanel.setOpaque(true);
        JImgPanel.setBackground(BG_WHITE);   // match parent

        JImgPanel.setLayout(new BorderLayout());
        JImgPanel.add(imageCanvas, BorderLayout.CENTER);
        imageCanvas.setPreferredSize(new Dimension(360, 360));

        // ---- C) Load images (classpath: /images/hangman1.png ... /hangman8.png) ----
        loadHangmanImages();          // fills frames[]

        // ---- D) Prepare word ----
        guessedWord = new char[wordToGuess.length()];
        Arrays.fill(guessedWord, '_');

        // ---- E) Word display ----
        textField1.setText(String.valueOf(guessedWord));
        textField1.setFont(new Font("Arial", Font.BOLD, 24));
        textField1.setHorizontalAlignment(JTextField.CENTER);
        textField1.setEditable(false);
        textField1.setBackground(BG_WHITE); // match UI
        textField1.setBorder(BorderFactory.createLineBorder(BTN_EDGE, 1, true));

        // ---- F) Status label (just text; image is drawn in JImgPanel) ----
        hangmanLabel.setText("FEHLER: 0");
        hangmanLabel.setFont(new Font("Arial", Font.BOLD, 16));
        hangmanLabel.setBackground(BG_WHITE);
        hangmanLabel.setOpaque(true);

        // Show first image in the canvas
        updateHangmanImage();

        // ---- G) Put buttons into a fixed, alphabetical order (A..Z). ----
        letterButtons.addAll(Arrays.asList(
                button1,  button2,  button3,  button4,  button5,  button6,  button7,  button8,  button9,  button10,
                button11, button12, button13, button14, button15, button16, button17, button18, button19, button20,
                button21, button22, button23, button24, button25, button26
        ));

        // ---- H) Label A–Z + listeners + simple design ----
        char letter = 'A';
        for (JButton b : letterButtons) {
            b.setText(String.valueOf(letter));
            styleButton(b);
            b.addActionListener(new GuessListener());
            letter++;
        }

        // ---- I) Restart button ----
        restartButton.setText("RESTART");
        restartButton.addActionListener(e -> resetGame());
    }

    // Button style (rounded, neutral)
    private void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 16));
        b.setMargin(new Insets(6, 10, 6, 10));
        b.setPreferredSize(new Dimension(44, 40));
        b.setBackground(BTN_BG);
        b.setForeground(BTN_FG);
        b.setBorder(BorderFactory.createLineBorder(BTN_EDGE, 2, true));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
    }

    // Visually "used" look (keep visible but light)
    private void markButtonUsed(JButton b) {
        b.setEnabled(false); // disables click
        b.setBackground(BTN_USED_BG);
        b.setForeground(BTN_USED_FG);
        b.setBorder(BorderFactory.createLineBorder(new Color(218, 222, 228), 2, true));
    }

    private void resetButtonVisual(JButton b) {
        b.setEnabled(true);
        b.setBackground(BTN_BG);
        b.setForeground(BTN_FG);
        b.setBorder(BorderFactory.createLineBorder(BTN_EDGE, 2, true));
        b.setVisible(true); // just in case
    }

    // === Load images from classpath (resources/images/hangman1.png ... hangman8.png) ===
    private void loadHangmanImages() {
        for (int i = 0; i < MAX_FRAMES; i++) {
            String path = "/images/hangman" + (i + 1) + ".png";
            URL url = getClass().getResource(path);
            if (url == null) {
                throw new IllegalStateException(
                        "Image not found: " + path + "\n" +
                                "Place PNGs under resources/images/ (or src/main/resources/images/) named hangman1.png..hangman8.png."
                );
            }
            ImageIcon raw = new ImageIcon(url);
            frames[i] = raw.getImage(); // store raw image; scale later in paint
        }
    }

    // === Draw the correct frame into JImgPanel ===
    private void updateHangmanImage() {
        int idx = Math.min(wrongGuesses, MAX_FRAMES - 1);
        if (imageCanvas != null) {
            imageCanvas.setImage(frames[idx]);
        }
        JImgPanel.revalidate();
        JImgPanel.repaint();
    }

    // Letter click logic
    private class GuessListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();
            String letter = clicked.getText();

            // Keep the button visible but make it light/disabled
            markButtonUsed(clicked);

            if (wordToGuess.contains(letter)) {
                for (int i = 0; i < wordToGuess.length(); i++) {
                    if (wordToGuess.charAt(i) == letter.charAt(0)) {
                        guessedWord[i] = letter.charAt(0);
                    }
                }
                textField1.setText(String.valueOf(guessedWord));

                if (!String.valueOf(guessedWord).contains("_")) {
                    JOptionPane.showMessageDialog(mainPanel, "🎉 You won!");
                    resetGame();
                }
            } else {
                wrongGuesses++;
                hangmanLabel.setText("Fehler: " + wrongGuesses);
                updateHangmanImage(); // advance picture

                if (wrongGuesses >= MAX_FRAMES - 1) {
                    JOptionPane.showMessageDialog(mainPanel, "💀 Game Over! The word was: " + wordToGuess);
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

        hangmanLabel.setText("Fehler: 0");
        updateHangmanImage(); // show first image again

        for (JButton b : letterButtons) resetButtonVisual(b);
    }

    // ======= Custom image canvas that centers & scales the image inside JImgPanel =======
    private static class ImageCanvas extends JPanel {
        private Image image;
        private static final int PADDING = 8; // inner padding so image doesn't touch edges

        public ImageCanvas() {
            // ensure no surprise background from LAF
            setBackground(Color.WHITE);
            setOpaque(true);
        }

        public void setImage(Image img) {
            this.image = img;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // fills with our background (white)
            if (image == null) return;

            int panelW = getWidth();
            int panelH = getHeight();

            // Keep aspect ratio: fit image into (panel - padding)
            int availW = Math.max(1, panelW - 2 * PADDING);
            int availH = Math.max(1, panelH - 2 * PADDING);
            int imgW = image.getWidth(this);
            int imgH = image.getHeight(this);
            if (imgW <= 0 || imgH <= 0) return;

            double scale = Math.min(availW / (double) imgW, availH / (double) imgH);
            int drawW = (int) Math.round(imgW * scale);
            int drawH = (int) Math.round(imgH * scale);

            int x = (panelW - drawW) / 2;
            int y = (panelH - drawH) / 2;

            // High quality scaling
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(image, x, y, drawW, drawH, this);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        // System L&F for native look
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        JFrame frame = new JFrame("Hangman");
        hangman game = new hangman();
        frame.setContentPane(game.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(820, 580);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
