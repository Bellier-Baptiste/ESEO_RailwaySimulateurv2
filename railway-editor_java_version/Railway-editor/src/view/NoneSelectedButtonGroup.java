/** Class part of the view package of the application. */

package view;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

/**
 * ToggleButtonGroup with no default selection.
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
