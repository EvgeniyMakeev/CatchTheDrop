package CatchTheDrop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class GameWindow extends JFrame {
    public static GameWindow gameWindow;
    private static long lastFrameTime;
    private static Image background;
    private static Image drop;
    private static Image game_over;
    private static float dropLeft = 200;
    private static float dropTop = -100;
    private static float dropV = 200;
    private static int score = 0;

    public static void main(String[] args) throws IOException {
        background = ImageIO.read(Objects.requireNonNull(GameWindow.class.getResourceAsStream("background.png")));
        drop = ImageIO.read(Objects.requireNonNull(GameWindow.class.getResourceAsStream("drop.png")));
        game_over = ImageIO.read(Objects.requireNonNull(GameWindow.class.getResourceAsStream("game_over.png")));
        gameWindow = new GameWindow();
        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameWindow.setLocation(400, 200);
        gameWindow.setSize(906, 478);
        gameWindow.setResizable(false);
        lastFrameTime = System.nanoTime();
        GameField gameField = new GameField();
        gameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float dropRigt = dropLeft + drop.getWidth(null);
                float dropBottom = dropTop + drop.getWidth(null);
                boolean isDrop = x >= dropLeft && x <= dropRigt && y >= dropTop && y <= dropBottom;
                if(isDrop) {
                    dropTop = -100;
                    dropLeft = (int) (Math.random() * (gameField.getWidth() - drop.getWidth(null)));
                    dropV = dropV + 20;
                    score++;
                    gameWindow.setTitle("Score: " + score);
                }
            }
        });
        gameWindow.add(gameField);
        gameWindow.setVisible(true);
    }

    public static void onRepaint(Graphics g) {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime) * 0.000000001f;
        lastFrameTime = currentTime;
        dropTop = dropTop + dropV * deltaTime;
        g.drawImage(background, 0,0, null);
        g.drawImage(drop, (int)dropLeft, (int) dropTop, null);
        if(dropTop > gameWindow.getHeight()) g.drawImage(game_over, 280, 120, null);
    }

    private static class GameField extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}