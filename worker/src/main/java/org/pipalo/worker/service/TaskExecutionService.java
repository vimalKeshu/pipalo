package org.pipalo.worker.service;

import org.pipalo.common.model.TaskInfo;
import org.pipalo.common.model.TaskStatus;
import org.pipalo.worker.ops.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TaskExecutionService {
    private ExecutorService executorService = Executors.newWorkStealingPool();
    private Set<TaskExecutor> runningTask = new HashSet<>();

    public TaskExecutionService(){
    }

    public void execute(TaskInfo task) {
        TaskExecutor taskExecutor = new TaskExecutor(task);
        this.runningTask.add(taskExecutor);
        this.executorService.submit(taskExecutor);
    }

    public TaskStatus getTaskStatus(String id) {
        TaskExecutor taskExecutor =  this.runningTask
                .stream()
                .filter(task -> task.getTask().getTaskId().equals(id))
                .findFirst().orElse(null);
        if (taskExecutor == null) return TaskStatus.NOT_FOUND;
        else return taskExecutor.getStatus();
    }

}
