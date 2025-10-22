import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class StartScreen extends JPanel {

    // Controls
    private final JSpinner livesSpinner;
    private final JComboBox<String> difficultyCombo;
    private final JCheckBox autoRestartCheck;
    private final JTextField customWordField;
    private final JButton startButton;
    private final JButton quitButton;

    // Settings-Container
    public static class Settings {
        public final int lives;
        public final String difficulty;     // "Leicht", "Mittel", "Schwer"
        public final boolean autoRestart;
        public final String customWord;     // optional, "" wenn leer

        public Settings(int lives, String difficulty, boolean autoRestart, String customWord) {
            this.lives = Math.max(1, lives);
            this.difficulty = difficulty;
            this.autoRestart = autoRestart;
            this.customWord = customWord == null ? "" : customWord.trim().toUpperCase();
        }
    }

    /**
     * @param onStart wird mit den gewählten Einstellungen aufgerufen
     * @param onQuit  wird aufgerufen, wenn Quit gedrückt wird
     */
    public StartScreen(Consumer<Settings> onStart, Runnable onQuit) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 12, 10, 12);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;

        // Titel
        JLabel title = new JLabel("Hangman – Start");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        add(title, c);

        // Leben / Fehler
        c.gridy++;
        c.gridwidth = 1;
        add(new JLabel("Max. Fehler (Leben):"), c);

        livesSpinner = new JSpinner(new SpinnerNumberModel(8, 1, 12, 1));
        c.gridx = 1;
        add(livesSpinner, c);

        // Schwierigkeit (optional)
        c.gridx = 0; c.gridy++;
        add(new JLabel("Schwierigkeit:"), c);
        difficultyCombo = new JComboBox<>(new String[]{"Leicht", "Mittel", "Schwer"});
        difficultyCombo.setSelectedIndex(1);
        c.gridx = 1;
        add(difficultyCombo, c);

        // Auto-Restart (optional)
        c.gridx = 0; c.gridy++;
        autoRestartCheck = new JCheckBox("Nach Sieg/Niederlage automatisch neu starten");
        autoRestartCheck.setBackground(Color.WHITE);
        c.gridwidth = 2;
        add(autoRestartCheck, c);

        // Eigenes Wort (optional)
        c.gridy++;
        c.gridwidth = 1;
        add(new JLabel("Eigenes Wort (optional):"), c);
        customWordField = new JTextField(16);
        c.gridx = 1;
        add(customWordField, c);

        // Buttons unten
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        startButton = new JButton("Spiel starten");
        quitButton = new JButton("Quit");
        buttons.add(quitButton);
        buttons.add(startButton);

        c.gridx = 0; c.gridy++;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.EAST;
        add(buttons, c);

        // Actions
        startButton.addActionListener(e -> {
            Settings s = collectSettings();
            onStart.accept(s);
        });

        quitButton.addActionListener(e -> {
            if (onQuit != null) onQuit.run();
            // Fallback: aktuelles Fenster schließen
            Window w = SwingUtilities.getWindowAncestor(StartScreen.this);
            if (w != null) w.dispose();
        });

        // Enter auf Start
        SwingUtilities.getRootPane(this).setDefaultButton(startButton);
    }

    private Settings collectSettings() {
        int lives = (Integer) livesSpinner.getValue();
        String diff = (String) difficultyCombo.getSelectedItem();
        boolean auto = autoRestartCheck.isSelected();
        String custom = customWordField.getText();
        return new Settings(lives, diff, auto, custom);
    }

    // Optional: öffentliche Setter, falls du per Code Defaults ändern willst
    public void setLives(int lives) { livesSpinner.setValue(Math.max(1, lives)); }
    public void setDifficulty(String difficulty) { difficultyCombo.setSelectedItem(difficulty); }
    public void setAutoRestart(boolean v) { autoRestartCheck.setSelected(v); }
    public void setCustomWord(String w) { customWordField.setText(w); }
}
