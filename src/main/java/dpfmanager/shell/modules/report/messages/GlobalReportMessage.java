package dpfmanager.shell.modules.report.messages;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.IndividualReport;

import java.util.List;

/**
 * Created by Adrià Llorens on 24/03/2016.
 */
public class GlobalReportMessage extends DpfMessage {

  private List<IndividualReport> individuals;
  private Configuration config;

  public GlobalReportMessage(List<IndividualReport> i, Configuration c){
    individuals = i;
    config = c;
  }

  public List<IndividualReport> getIndividuals() {
    return individuals;
  }

  public Configuration getConfig() {
    return config;
  }

}
