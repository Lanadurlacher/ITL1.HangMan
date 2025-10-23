import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame startFrame = new JFrame("Hangman – Start");

            StartScreen startPanel = new StartScreen(settings -> {
                // Start wurde geklickt → Start-Fenster schließen und Spiel öffnen
                startFrame.dispose();

                hangman game = new hangman();
                game.setTotalLives(settings.lives);

                JFrame gameFrame = new JFrame("Hangman");
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.setContentPane(game.getMainPanel());
                gameFrame.setSize(820, 580);
                gameFrame.setLocationRelativeTo(null);
                gameFrame.setVisible(true);
            }, () -> System.exit(0));

            startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            startFrame.setContentPane(startPanel);
            startFrame.pack();
            Dimension d = startFrame.getSize();
            startFrame.setSize(Math.max(420, d.width + 80), Math.max(220, d.height + 40));
            startFrame.setLocationRelativeTo(null);
            startFrame.setVisible(true);
        });
    }
}
