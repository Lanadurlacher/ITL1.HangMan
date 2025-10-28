import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.util.function.Consumer;

public class StartScreen extends JPanel {

    private JPanel panel1;
    private JSpinner livesSpinner;
    private JComboBox<String> difficultyCombo;
    private JCheckBox autoRestartCheck;
    private JButton startButton;
    private JButton quitButton;

    // Container für Einstellungen
    public static class Settings {
        public final int lives;
        public final String difficulty;
        public final boolean autoRestart;

        public Settings(int lives, String difficulty, boolean autoRestart) {
            this.lives = Math.max(1, lives);
            this.difficulty = (difficulty == null) ? "Mittel" : difficulty;
            this.autoRestart = autoRestart;
        }
    }

    public StartScreen(Consumer<Settings> onStart, Runnable onQuit) {

        setLayout(new BorderLayout());
        add(panel1, BorderLayout.CENTER);

        // ---------- DESIGN (hell, modern) ----------
        Color background = new Color(245, 247, 255);     // Hell-blau/weiß
        Color accent = new Color(45, 120, 255);          // Blau für Buttons
        Color hover = new Color(30, 90, 210);            // Button hover

        setBackground(background);
        panel1.setBackground(background);

        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        for (Component c : panel1.getComponents()) {
            c.setFont(font);
            c.setForeground(Color.DARK_GRAY);
            if (c instanceof JPanel) c.setBackground(background);
        }

        // ---------- ComboBox (Schwierigkeit) ----------
        if (difficultyCombo != null) {
            difficultyCombo.setModel(
                    new DefaultComboBoxModel<>(new String[] {"Leicht", "Mittel", "Schwer"})
            );
            difficultyCombo.setSelectedItem("Mittel");
            difficultyCombo.setBackground(Color.WHITE);
            difficultyCombo.setForeground(Color.BLACK);
        }

        // ---------- Spinner (Leben) ----------
        if (livesSpinner != null) {
            livesSpinner.setModel(new SpinnerNumberModel(8, 1, 12, 1));
            JComponent editor = livesSpinner.getEditor();
            editor.setBackground(Color.WHITE);
            editor.setForeground(Color.BLACK);
        }

        // ---------- Buttons ----------
        styleButton(startButton, accent, hover);
        styleButton(quitButton, new Color(210, 70, 70), new Color(160, 40, 40));

        // ---------- Events ----------
        startButton.addActionListener(e -> {
            Settings s = collectSettings();
            if (onStart != null) onStart.accept(s);
        });

        quitButton.addActionListener(e -> {
            if (onQuit != null) onQuit.run();
            Window w = SwingUtilities.getWindowAncestor(StartScreen.this);
            if (w != null) w.dispose();
        });

        // ENTER als Default-Button
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0 && isDisplayable()) {
                SwingUtilities.getRootPane(StartScreen.this).setDefaultButton(startButton);
            }
        });
    }

    private void styleButton(JButton button, Color base, Color hover) {
        button.setFocusPainted(false);
        button.setBackground(base);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent evt) { button.setBackground(base); }
        });
    }

    private Settings collectSettings() {
        int lives = (livesSpinner != null) ? (Integer) livesSpinner.getValue() : 8;
        String diff = (difficultyCombo != null) ? (String) difficultyCombo.getSelectedItem() : "Mittel";
        boolean auto = (autoRestartCheck != null) && autoRestartCheck.isSelected();
        return new Settings(lives, diff, auto);
    }
}
