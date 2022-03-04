package org.pipalo.worker.ops;

import lombok.Getter;
import org.pipalo.common.model.TaskInfo;
import org.pipalo.common.model.TaskStatus;

@Getter
public class TaskExecutor implements Runnable {
    private TaskStatus status;
    private TaskInfo task;
    private int retry=3;

    public TaskExecutor(TaskInfo task){
        this.task = task;
        this.status = TaskStatus.STARTED;
    }

    @Override
    public void run() {
        try{
            System.out.println("The task,"+task+" is performed.");
            this.status = TaskStatus.COMPLETED;
        } catch (Exception ex){
            this.status = TaskStatus.FAILED;
            ex.printStackTrace();
        }
    }
}
