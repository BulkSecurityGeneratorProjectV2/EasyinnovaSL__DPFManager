package dpfmanager.shell.modules.database.core;

import dpfmanager.shell.core.DpFManagerConstants;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.database.messages.CheckTaskMessage;
import dpfmanager.shell.modules.database.messages.CronMessage;
import dpfmanager.shell.modules.database.messages.JobsMessage;
import dpfmanager.shell.modules.database.tables.Crons;
import dpfmanager.shell.modules.database.tables.Jobs;
import dpfmanager.shell.modules.server.messages.StatusMessage;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * Created by Adrià Llorens on 20/04/2016.
 */
@Service(BasicConfig.SERVICE_DATABASE)
@Scope("singleton")
public class DatabaseService extends DpfService {

  private Integer pid;
  private DatabaseConnection connection;
  private DatabaseCache cache;

  @Resource(name = "parameters")
  private Map<String, String> parameters;

  @PostConstruct
  public void init() {
    // No context yet
  }

  @Override
  protected void handleContext(DpfContext context) {
    cache = new DatabaseCache(context);
    connection = new DatabaseConnection(context);
    connection.init();
    pid = connection.getProgramPid();
    cleanDatabase();
  }

  private void cleanDatabase() {
    connection.cleanJobs();
  }

  /**
   * Crons
   */

  public void handleCronMessage(CronMessage cm) {
    if (cm.isSave()) {
      saveCron(cm);
    } else if (cm.isDelete()) {
      deleteCron(cm);
    } else if (cm.isGet()) {
      getCrons();
    }
  }

  public void saveCron(CronMessage dm) {
    if (cache.containsCron(dm.getUuid())) {
      // Editing
      cache.updateCron(dm.getUuid(), dm.getInput(), dm.getConfiguration(), dm.getPeriodicity());
      connection.updateCron(cache.getCron(dm.getUuid()));
    } else {
      // New
      cache.insertNewCron(dm.getUuid(), dm.getInput(), dm.getConfiguration(), dm.getPeriodicity());
      connection.insertCron(cache.getCron(dm.getUuid()));
    }
  }

  public void deleteCron(CronMessage dm) {
    if (cache.containsCron(dm.getUuid())) {
      connection.deleteCron(cache.getCron(dm.getUuid()));
      cache.deleteCron(dm.getUuid());
    }
  }

  public void getCrons() {
    List<Crons> list = connection.getAllCrons();
    if (!list.isEmpty()){
      cache.setCrons(list);
      context.send(GuiConfig.PERSPECTIVE_PERIODICAL + "." + GuiConfig.COMPONENT_PERIODICAL, new CronMessage(CronMessage.Type.RESPONSE, list));
    }
  }

  /**
   * Jobs
   */

  public void handleJobsMessage(JobsMessage dm) {
    if (dm.isNew()) {
      createJob(dm);
    } else if (dm.isInit()) {
      initJob(dm);
    } else if (dm.isUpdate()) {
      updateJob(dm);
    } else if (dm.isFinish()) {
      finishJob(dm);
    } else if (dm.isGet()) {
      getJobs();
    } else if (dm.isResume()) {
      resumeJob(dm);
    } else if (dm.isCancel()) {
      cancelJob(dm);
    } else if (dm.isPause()) {
      pauseJob(dm);
    } else if (dm.isStart()) {
      startJob(dm);
    }
  }

  public void createJob(JobsMessage dm) {
    // Get origin
    String origin = "GUI";
    if (!context.isGui()) {
      origin = parameters.get("mode");
    }
    int state = 1;
    if (dm.getPending()) {
      state = 0;
    }
    cache.insertNewJob(dm.getUuid(), state, 0, dm.getInput(), origin, pid, "");
    connection.insertNewJob(cache.getJob(dm.getUuid()));

    // Now force refresh tasks
    getJobs();
  }

  public void initJob(JobsMessage dm) {
    cache.initJob(dm.getUuid(), dm.getTotal(), dm.getOutput());
    forceSyncDatabase();
  }

  public void startJob(JobsMessage dm) {
    cache.startJob(dm.getUuid());
    forceSyncDatabase();
  }

  public void updateJob(JobsMessage dm) {
    cache.incressProcessed(dm.getUuid());
    syncDatabase();
  }

  public void finishJob(JobsMessage dm) {
    cache.finishJob(dm.getUuid());
    forceSyncDatabase();
    cache.clearJob(dm.getUuid());
  }

  public void resumeJob(JobsMessage dm) {
    cache.resumeJob(dm.getUuid());
    forceSyncDatabase();
  }

  public void cancelJob(JobsMessage dm) {
    if (cache.containsJob(dm.getUuid())) {
      cache.cancelJob(dm.getUuid());
      forceSyncDatabase();
      cache.clearJob(dm.getUuid());
    }
  }

  public void pauseJob(JobsMessage dm) {
    if (cache.containsJob(dm.getUuid())) {
      cache.pauseJob(dm.getUuid());
      forceSyncDatabase();
    }
  }

  public StatusMessage getJobStatus(Long id) {
    Jobs job = connection.getJob(id);
    StatusMessage.Status status = StatusMessage.Status.RUNNING;
    if (job.getState() == 2) {
      status = StatusMessage.Status.FINISHED;
    } else if (job.getState() == -1) {
      status = StatusMessage.Status.NOTFOUND;
    }
    return new StatusMessage(status, job.getOutput(), job.getProcessedFiles(), job.getTotalFiles());
  }

  public StatusMessage getJobStatusByHash(String hash) {
    Jobs job = connection.getJob(hash);
    StatusMessage.Status status = StatusMessage.Status.RUNNING;
    if (job.getState() == 2) {
      status = StatusMessage.Status.FINISHED;
    } else if (job.getState() == -1) {
      status = StatusMessage.Status.NOTFOUND;
    }
    return new StatusMessage(status, job.getOutput(), job.getProcessedFiles(), job.getTotalFiles());
  }

  public void getJobs() {
    List<Jobs> jobs = connection.getJobs();
    context.sendGui(GuiConfig.COMPONENT_PANE, new CheckTaskMessage(jobs, pid));
  }

  /**
   * Sync functions
   */
  private void syncDatabase() {
    if (System.currentTimeMillis() - connection.getLastUpdate() > DpFManagerConstants.UPDATE_INTERVAL) {
      forceSyncDatabase();
    }
  }

  private void forceSyncDatabase() {
    connection.updateJobs(cache.getJobs());
  }

  @PreDestroy
  public void finish() {
    connection.close();
  }

}
