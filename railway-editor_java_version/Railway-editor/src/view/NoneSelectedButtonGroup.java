package view;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

/** ToggleButtonGroup with no default selection. */
public class NoneSelectedButtonGroup extends ButtonGroup {
  @Override
  public void setSelected(ButtonModel model, boolean selected) {
    if (selected) {
      super.setSelected(model, selected);
    } else {
      clearSelection();
    }
  }
}