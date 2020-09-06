package com.gregory.learning.panels;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class HomeScreen extends JFrame implements KeyListener {

  private Component visiblePanel;

  public HomeScreen() {
    super("Snake");
    visiblePanel = new TitlePanel();
    addKeyListener(this);
    setSize(500, 500);
    add(visiblePanel);
    setResizable(false);
    setVisible(true);
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
  }

  @Override
  public void keyPressed(KeyEvent e) {
    // Start the game
    TitlePanel titlePanel = (TitlePanel) this.visiblePanel;
    titlePanel.clearTimer();
    this.remove(visiblePanel);
    this.visiblePanel = new GameBoard();
    this.add(visiblePanel);
    visiblePanel.setFocusable(true);
    this.repaint();

    // Remove this key listener
    this.removeKeyListener(this);

    // Pass the focus down
    visiblePanel.requestFocusInWindow();
  }

  // Don't need these
  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }
}
