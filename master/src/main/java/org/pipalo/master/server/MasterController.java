package org.pipalo.master.server;

import org.pipalo.common.model.TaskInfo;
import org.pipalo.common.model.WorkerInfo;
import org.pipalo.master.service.ReplicationService;
import org.pipalo.master.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;
import java.util.List;

@RestController
@RequestMapping("/master")
public class MasterController {

    @Autowired
    ReplicationService replicationService;
    @Autowired
    WorkerService workerService;

    @GetMapping
    public List<TaskInfo> getWork() {
        List<TaskInfo> tasks = replicationService.getQueue().pop(2);
        return tasks;
    }

    @PostMapping
    public void registerWorker(WorkerInfo worker) {
        this.workerService.registerWorker(worker);
    }

}
