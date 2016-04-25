package dpfmanager.shell.modules.database;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.database.core.DatabaseService;
import dpfmanager.shell.modules.database.messages.DatabaseMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 20/04/2016.
 */
@Controller(BasicConfig.MODULE_DATABASE)
public class DatabaseController extends DpfSpringController {

  @Autowired
  private DatabaseService service;

  @Autowired
  private ApplicationContext appContext;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(DatabaseMessage.class)) {
      DatabaseMessage dm = dpfMessage.getTypedMessage(DatabaseMessage.class);
      if (dm.isNew()) {
        service.createJob(dm);
      } else if (dm.isUpdate()) {
        service.updateJob(dm);
      } else if (dm.isFinish()) {
        service.finishJob(dm);
      } else if (dm.isGet()) {
        service.getJobs(dm);
      }
    }
  }

  @PostConstruct
  public void init() {
    service.setContext(new ConsoleContext(appContext));
  }
}
