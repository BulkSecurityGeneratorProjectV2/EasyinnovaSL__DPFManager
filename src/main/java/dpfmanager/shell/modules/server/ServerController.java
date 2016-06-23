package dpfmanager.shell.modules.server;

import dpfmanager.shell.application.launcher.noui.ConsoleLauncher;
import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.interfaces.console.AppContext;
import dpfmanager.shell.modules.server.core.ServerService;
import dpfmanager.shell.modules.server.messages.PostMessage;
import dpfmanager.shell.modules.server.messages.ServerMessage;
import dpfmanager.shell.modules.threading.core.ThreadingService;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;
import dpfmanager.shell.modules.threading.messages.ThreadsMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Controller(BasicConfig.MODULE_SERVER)
public class ServerController extends DpfSpringController {

  @Autowired
  private ServerService service;

  @Autowired
  private ApplicationContext appContext;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(ServerMessage.class)){
      service.startServer();
    } else if (dpfMessage.isTypeOf(PostMessage.class)){
      PostMessage pm = dpfMessage.getTypedMessage(PostMessage.class);
      if (pm.isPost()){
        service.tractPostRequest(pm);
      }
    }
  }

  @Override
  public Object handleMessageWithResponse(DpfMessage dpfMessage) {
    return null;
  }

  @PostConstruct
  public void init() {
    service.setContext(new ConsoleContext(appContext));
  }

}