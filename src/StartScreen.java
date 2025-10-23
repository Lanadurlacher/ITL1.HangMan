import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.util.function.Consumer;

public class StartScreen extends JPanel {

    // Diese Felder müssen exakt den Komponenten-Namen in deiner .form entsprechen!
    private JPanel panel1;
    private JSpinner livesSpinner;
    private JComboBox<String> difficultyCombo;
    private JCheckBox autoRestartCheck;
    private JTextField customWordField;
    private JButton startButton;
    private JButton quitButton;

    // ---- Settings-Container ----
    public static class Settings {
        public final int lives;
        public final String difficulty;
        public final boolean autoRestart;
        public final String customWord;

        public Settings(int lives, String difficulty, boolean autoRestart, String customWord) {
            this.lives = Math.max(1, lives);
            this.difficulty = (difficulty == null) ? "Mittel" : difficulty;
            this.autoRestart = autoRestart;
            this.customWord = (customWord == null) ? "" : customWord.trim().toUpperCase();
        }
    }

    // ---- Konstruktor (Callbacks für Start und Quit) ----
    public StartScreen(Consumer<Settings> onStart, Runnable onQuit) {
        // IntelliJ füllt panel1 und alle Felder automatisch aus der .form
        setLayout(new BorderLayout());
        add(panel1, BorderLayout.CENTER);

        // --- ComboBox initialisieren ---
        if (difficultyCombo != null) {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("Leicht");
            model.addElement("Mittel");
            model.addElement("Schwer");
            difficultyCombo.setModel(model);
            difficultyCombo.setSelectedItem("Mittel");
        }

        // --- Spinner Standardwert ---
        if (livesSpinner != null) {
            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(8, 1, 12, 1);
            livesSpinner.setModel(spinnerModel);
        }

        // Aktionen verdrahten
        if (startButton != null) {
            startButton.addActionListener(e -> {
                Settings s = collectSettings();
                if (onStart != null) onStart.accept(s);
            });
        }

        if (quitButton != null) {
            quitButton.addActionListener(e -> {
                if (onQuit != null) onQuit.run();
                Window w = SwingUtilities.getWindowAncestor(StartScreen.this);
                if (w != null) w.dispose();
            });
        }

        // Default-Button (ENTER = Start) erst setzen, wenn Fenster sichtbar ist
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0 && isDisplayable()) {
                JRootPane root = SwingUtilities.getRootPane(StartScreen.this);
                if (root != null && startButton != null)
                    root.setDefaultButton(startButton);
            }
        });
    }

    // ---- Daten aus Formular lesen ----
    private Settings collectSettings() {
        int lives = (livesSpinner != null) ? (Integer) livesSpinner.getValue() : 8;
        String diff = (difficultyCombo != null) ? (String) difficultyCombo.getSelectedItem() : "Mittel";
        boolean auto = (autoRestartCheck != null) && autoRestartCheck.isSelected();
        String custom = (customWordField != null) ? customWordField.getText() : "";
        return new Settings(lives, diff, auto, custom);
    }
}
