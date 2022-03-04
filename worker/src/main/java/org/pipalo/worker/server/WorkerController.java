package org.pipalo.worker.server;

import org.pipalo.common.model.TaskInfo;
import org.pipalo.common.model.TaskStatus;
import org.pipalo.worker.service.TaskExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    TaskExecutionService taskExecutionService;

    @PostMapping("/task")
    public void getTask(TaskInfo task) {
        taskExecutionService.execute(task);
    }

    @GetMapping("/task/{id}")
    public TaskStatus getTaskStatus(@PathVariable String id){
        return taskExecutionService.getTaskStatus(id);
    }

}
