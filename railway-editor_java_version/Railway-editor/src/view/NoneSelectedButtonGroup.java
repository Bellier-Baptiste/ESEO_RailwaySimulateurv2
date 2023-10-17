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

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

/**
 * ToggleButtonGroup with no default selection.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file NoneSelectedButtonGroup.java
 * @date N/A
 * @since 2.0
 */
public class NoneSelectedButtonGroup extends ButtonGroup {

  /**
   * Sets the selected value for the ButtonModel.
   *
   * @param model    the ButtonModel
   * @param selected true if this button is to be selected, otherwise false
   */
  @Override
  public void setSelected(final ButtonModel model, final boolean selected) {
    if (selected) {
      super.setSelected(model, selected);
    } else {
      clearSelection();
    }
  }
}
