package dpfmanager.shell.interfaces.gui.component.global.comparators;

import dpfmanager.shell.modules.report.util.ReportIndividualGui;
import dpfmanager.shell.modules.statistics.model.StatisticsIso;

import java.util.Comparator;

/**
 * Created by Adrià Llorens on 26/05/2017.
 */
public class IndividualComparator implements  Comparator<ReportIndividualGui> {

  public enum Mode {
    ERRORS, WARNINGS, NAME, RESULT, PATH
  }

  public enum Order {
    ASC, DESC
  }

  private Mode mode;

  private Order order;

  public IndividualComparator(Mode m, Order o){
    mode = m;
    order = o;
  }

  /**
   * Negative = o1 < o2
   * Positive = o1 > o2
   */
  @Override
  public int compare(ReportIndividualGui o1, ReportIndividualGui o2) {
    if (!mode.equals(Mode.NAME)) {
      o1.load();
      o2.load();
    }
    Integer compare = 0;
    switch (mode){
      case ERRORS:
        if (order.equals(Order.ASC)){
          compare = o1.getErrors().compareTo(o2.getErrors());
        } else {
          compare = o2.getErrors().compareTo(o1.getErrors());
        }
        break;
      case WARNINGS:
        if (order.equals(Order.ASC)){
          if (onlyWarnigns(o1) && !onlyWarnigns(o2)) {
            compare = -1;
          } else if (!onlyWarnigns(o1) && onlyWarnigns(o2)) {
            compare = 1;
          } else {
            compare = o1.getWarnings().compareTo(o2.getWarnings());
          }
        } else {
          if (onlyWarnigns(o1) && !onlyWarnigns(o2)) {
            compare = -1;
          } else if (!onlyWarnigns(o1) && onlyWarnigns(o2)) {
            compare = 1;
          } else {
            compare = o2.getWarnings().compareTo(o1.getWarnings());
          }
        }
        break;
      case RESULT:
        if (order.equals(Order.ASC)){
          compare = o1.getErrors().compareTo(o2.getErrors());
        } else {
          compare = o2.getErrors().compareTo(o1.getErrors());
        }
        break;
      case NAME:
        if (order.equals(Order.ASC)){
          compare = o1.getLowerName().compareTo(o2.getLowerName());
        } else {
          compare = o2.getLowerName().compareTo(o1.getLowerName());
        }
        break;
      case PATH:
        if (order.equals(Order.ASC)){
          compare = o1.getFilePath().toLowerCase().compareTo(o2.getFilePath().toLowerCase());
        } else {
          compare = o2.getFilePath().toLowerCase().compareTo(o1.getFilePath().toLowerCase());
        }
        break;
    }

    // If equals, default NAME ASC
    if (compare == 0){
      compare = o1.getLowerName().compareTo(o2.getLowerName());
    }
    return compare;
  }

  private boolean onlyWarnigns(ReportIndividualGui o) {
    return o.getErrors() == 0 && o.getWarnings() > 0;
  }

}
