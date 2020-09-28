package com.gregory.learning.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

public class GameBoard extends JPanel implements KeyListener {

  private String direction = "NONE";
  private boolean[][] gameBoard = new boolean[20][20];
  private ArrayList<short[]> snakeBodyCoords = new ArrayList<>();
  private ArrayList<int[]> foodToAdd = new ArrayList<int[]>();
  private Timer timer;
  private boolean gameOver = true;
  private short score = 0;
  private Random random = new Random();

  {
    snakeBodyCoords.add(new short[]{10, 8});
    snakeBodyCoords.add(new short[]{10, 9});
    snakeBodyCoords.add(new short[]{10, 10});
  }

  private short[] snakeHeadCoords = {10, 8};
  private int[] foodCoords = null;

  public GameBoard() {
    addKeyListener(this);
    setSize(500, 500);
    setLayout(new BorderLayout());
    setVisible(true);
    startGame();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Draw board
    int boardLength = 400;
    int verticalCenter = this.getHeight() / 2;
    int xOffset = verticalCenter - boardLength / 2;
    int yOffset = 10;

    g.drawRect(xOffset, yOffset, boardLength,
        boardLength);

    // Draw Food
    if (foodCoords != null) {
      g.setColor(Color.GREEN);
      g.fillRect(foodCoords[0] * 20 + xOffset, foodCoords[1] * 20 + yOffset, 20, 20);
    }

    // Draw snake
    for (short[] snakePart : snakeBodyCoords) {
      g.setColor(Color.RED);
      g.fillRect(snakePart[0] * 20 + xOffset, snakePart[1] * 20 + yOffset, 20, 20);
    }

    // Draw snake head
    g.setColor(Color.BLUE);
    g.fillRect(snakeHeadCoords[0] * 20 + xOffset, snakeHeadCoords[1] * 20 + yOffset, 20, 20);

    // Draw score
    g.setColor(Color.BLACK);
    g.drawString("Score: " + score, 10, 450);
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
    switch (keyCode) {
      case KeyEvent.VK_UP:
        gameOver = false;
        direction = "UP";
        break;
      case KeyEvent.VK_DOWN:
        gameOver = false;
        direction = "DOWN";
        break;
      case KeyEvent.VK_LEFT:
        gameOver = false;
        direction = "LEFT";
        break;
      case KeyEvent.VK_RIGHT:
        gameOver = false;
        direction = "RIGHT";
        break;
    }
  }

  public void gameLoop() {
    if (!gameOver) {
      moveSnake();
      handleFood();
      addToSnake();
      checkBorderCollision();
      checkSnakeHeadCollision();
      repaint();
    }
    repaint();
  }

  public void moveSnake() {
    short[] newSnakeHead;
    short[] removedPiece = null;
    switch (direction) {
      case "NONE":
        break;
      case "UP":
        // Get snake head and update it's position
        removedPiece = snakeBodyCoords.remove(snakeBodyCoords.size() - 1);
        newSnakeHead = new short[]{snakeBodyCoords.get(0)[0],
            (short) (snakeBodyCoords.get(0)[1] - 1)};
        snakeHeadCoords = new short[]{newSnakeHead[0], newSnakeHead[1]};
        snakeBodyCoords.add(0, newSnakeHead);
        break;
      case "DOWN":
        removedPiece = snakeBodyCoords.remove(snakeBodyCoords.size() - 1);
        newSnakeHead = new short[]{snakeBodyCoords.get(0)[0],
            (short) (snakeBodyCoords.get(0)[1] + 1)};
        snakeHeadCoords = new short[]{newSnakeHead[0], newSnakeHead[1]};
        snakeBodyCoords.add(0, newSnakeHead);
        break;
      case "LEFT":
        removedPiece = snakeBodyCoords.remove(snakeBodyCoords.size() - 1);
        newSnakeHead = new short[]{(short) (snakeBodyCoords.get(0)[0] - 1),
            snakeBodyCoords.get(0)[1]};
        snakeHeadCoords = new short[]{newSnakeHead[0], newSnakeHead[1]};
        snakeBodyCoords.add(0, newSnakeHead);
        break;
      case "RIGHT":
        removedPiece = snakeBodyCoords.remove(snakeBodyCoords.size() - 1);
        newSnakeHead = new short[]{(short) (snakeBodyCoords.get(0)[0] + 1),
            snakeBodyCoords.get(0)[1]};
        snakeHeadCoords = new short[]{newSnakeHead[0], newSnakeHead[1]};
        snakeBodyCoords.add(0, newSnakeHead);
        break;
    }
    if (removedPiece != null) {
      gameBoard[removedPiece[0]][removedPiece[1]] = false;
    }
    for (short[] snakeBodyCoord : snakeBodyCoords) {
      if (snakeBodyCoord[0] > 0 && snakeHeadCoords[0] > 0 && snakeBodyCoord[0] < 20
          && snakeHeadCoords[0] < 20 && snakeBodyCoord[1] > 0 && snakeHeadCoords[1] > 0
          && snakeBodyCoord[1] < 20
          && snakeHeadCoords[1] < 20) {
        gameBoard[snakeBodyCoord[0]][snakeBodyCoord[1]] = true;
        gameBoard[snakeHeadCoords[0]][snakeHeadCoords[1]] = true;
      }
    }
  }

  private void startGame() {
    GameBoard thisGameBoard = this;
    timer = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        thisGameBoard.gameLoop();
      }
    };
    timer.schedule(task, 0, 100);
  }

  private void handleFood() {
    // Spawn food if needed
    if (foodCoords == null) {
      foodCoords = new int[2];

      int rand1 = random.nextInt(20);
      int rand2 = random.nextInt(20);


      outerloop: for (int i = 0; i < gameBoard.length; i++) {
        for (int j = 0; j < gameBoard.length; j++) {
          if (!gameBoard[(rand1 + i) % gameBoard.length][(rand2 + j) % gameBoard.length]) {
            foodCoords[0] = rand1 + i % gameBoard.length;
            foodCoords[1] = rand2 + j % gameBoard.length;
            break outerloop;
          }
        }
      }
    }
    // Check for food collision
    if (snakeHeadCoords[0] == foodCoords[0] && snakeHeadCoords[1] == foodCoords[1]) {
      foodToAdd.add(foodCoords);
      score += 1;
      foodCoords = null;
    }

  }

  private void addToSnake() {
    ArrayList<int[]> pieces = new ArrayList<>();
    boolean safeToAdd = true;
    for (int[] foodPiece : foodToAdd) {
      for (short[] snakePiece : snakeBodyCoords) {
        if (snakePiece[0] == foodPiece[0] && snakePiece[1] == foodPiece[1]) {
          safeToAdd = false;
          break;
        }
      }
      if (safeToAdd) {
        pieces.add(foodPiece);
      }
    }
    pieces.forEach(piece -> {
      foodToAdd.remove(piece);
      short[] arr = new short[2];
      arr[0] = (short) piece[0];
      arr[1] = (short) piece[1];
      snakeBodyCoords.add(arr);
    });
  }

  private void checkBorderCollision() {
    if (snakeHeadCoords[0] < 0 || snakeHeadCoords[1] < 0 || snakeHeadCoords[0] > 19
        || snakeHeadCoords[1] > 19) {
      resetGame();
    }
  }

  private void checkSnakeHeadCollision() {
    for (short i = 1; i < snakeBodyCoords.size(); i++) {
      if (snakeBodyCoords.get(i)[0] == snakeHeadCoords[0]
          && snakeBodyCoords.get(i)[1] == snakeHeadCoords[1]) {
        resetGame();
      }
    }
  }

  private void resetGame() {
    gameOver = true;
    snakeBodyCoords.clear();
    snakeBodyCoords.add(new short[]{10, 8});
    snakeBodyCoords.add(new short[]{10, 9});
    snakeBodyCoords.add(new short[]{10, 10});
    snakeHeadCoords[0] = 10;
    snakeHeadCoords[1] = 8;
    foodCoords = null;
    score = 0;
    gameBoard = new boolean[20][20];
  }

  // Don't need these
  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  public void clearTimer() {
    if (timer != null) {
      timer.cancel();
      timer = null;
    }
  }
}
