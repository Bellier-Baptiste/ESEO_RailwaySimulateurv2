/*
 * License : MIT License
 *
 * Copyright (c) 2023 Team PFE_2023_16
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package view;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;

/**
 * Popup to inform an imminent merge between two {@link model.Station}.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file Popup.java
 * @date N/A
 * @since 2.0
 */
public class Popup {
  /** Font size. */
  private static final int FONT_SIZE = 14;
  /** JOptionPane jp. */
  private static int jp;
  /** user choice. */
  private int choice;
  /** observer. */
  private Consumer<Object> observer;

  private static void display() {
    JLabel title = new JLabel("Station merge");
    title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(title);
    String messageAide = "you are about to merge station close this "
        + "dialog box \nand select the second station  you want to merge";
    jp = JOptionPane.showConfirmDialog(panel, messageAide,
        "press cancel to abort the operation", JOptionPane.OK_CANCEL_OPTION);
  }

  /**
   * get the JoptionPane.
   *
   * @return JoptionPane jp
   */
  public int getJp() {
    return jp;
  }


  /**
   * get the user's choice.
   *
   * @return int choice
   */
  public int getChoice() {
    return choice;
  }

  /**
   * set the user choice.
   *
   * @param choiceToSet ok or cancel
   */
  public void setChoice(final int choiceToSet) {
    this.choice = choiceToSet;
  }

  /**
   * set the observer.
   *
   * @param observerToSet observer
   */
  public void setObserver(final Consumer<Object> observerToSet) {
    this.observer = observerToSet;
  }

  /**
   * display the popup and get the JoptionPane choice.
   */
  public void pop() {
    EventQueue.invokeLater(() -> {
      display();
      if (jp == JOptionPane.OK_OPTION) {
        choice = 1;
      } else if (jp == JOptionPane.CANCEL_OPTION) {
        choice = 0;
      }
      observer.accept(choice);
    });
  }
}
