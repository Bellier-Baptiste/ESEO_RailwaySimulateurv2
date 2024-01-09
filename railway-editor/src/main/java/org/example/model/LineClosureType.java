package org.example.model;

/**
 * Enum of the {$link org.example.model.EventLineClosed} types.
 *
 * @author Marie Bordet
 * @file LineClosureType.java
 * @date 2024-01-09
 * @since 3.0
 */
public enum LineClosureType {
  /**
   * Planned closure type.
   */
  PLANNED("planned"),
  /**
   * Unexpected closure type.
   */
  UNEXPECTED("unexpected");

  /**
   * String value of the line closure type.
   */
  private final String value;

  /**
   * Constructor of the enum.
   *
   * @param valueToSet String value to set
   */
  LineClosureType(final String valueToSet) {
    this.value = valueToSet;
  }

  /**
   * Get the string value of the line closure type.
   *
   * @return String value
   */
  public String getValue() {
    return this.value;
  }
}
