package com.gregory.learning.panels;

import java.awt.Font;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TitlePanel extends JPanel {

  Timer timer;
  JLabel titleCard = createTitleCard();
  JLabel playSign = createPlaySign();
  JLabel gif = createGif();
  boolean playSignVisible = true;

  public TitlePanel() {
    setSize(500, 500);
    setLayout(null);
    add(titleCard);
    gif.setHorizontalAlignment(SwingConstants.CENTER);
    titleCard.setBounds((int) (500 - titleCard.getPreferredSize().getWidth()) / 2,
        0,
        (int) titleCard.getPreferredSize().getWidth(),
        (int) titleCard.getPreferredSize().getHeight());
    gif.setBounds((500 - (int) (gif.getPreferredSize().getWidth())) / 2,
        (500 - (int) gif.getPreferredSize().getHeight()) / 2,
        (int) gif.getPreferredSize().getWidth(),
        (int) gif.getPreferredSize().getHeight());
    playSign.setBounds((int) (500 - playSign.getPreferredSize().getWidth()) / 2,
        470 - (int) playSign.getPreferredSize().getHeight(),
        (int) playSign.getPreferredSize().getWidth(),
        (int) playSign.getPreferredSize().getHeight());
    add(gif);
    add(playSign);
    animatePlaySign();
  }

  public void flashSign() {
    playSignVisible = !playSignVisible;
    playSign.setVisible(playSignVisible);
  }

  private JLabel createTitleCard() {
    JLabel jLabel = new JLabel("SNAKE", SwingConstants.CENTER);
    jLabel.setFont(new Font("Verdana", Font.BOLD, 60));
    return jLabel;
  }

  private JLabel createGif() {
    JLabel jLabel = new JLabel();
    ImageIcon imageIcon = new ImageIcon(getClass()
        .getResource("/" + "front-page.gif"));
    jLabel.setIcon(imageIcon);
    return jLabel;
  }

  private JLabel createPlaySign() {
    JLabel jLabel = new JLabel("Press any key to play", SwingConstants.CENTER);
    jLabel.setFont(new Font("Verdana", Font.BOLD, 20));
    return jLabel;
  }

  private void animatePlaySign() {
    TitlePanel thisPanel = this;
    timer = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        thisPanel.flashSign();
      }
    };
    timer.schedule(task, 500, 500);
  }

  public void clearTimer() {
    if (timer != null) {
      timer.cancel();
      timer = null;
    }
  }
}
