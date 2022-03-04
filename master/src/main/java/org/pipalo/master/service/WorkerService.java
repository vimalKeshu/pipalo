package org.pipalo.master.service;

import org.pipalo.common.model.TaskInfo;
import org.pipalo.common.model.WorkerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class WorkerService {
    private static Logger logger = LoggerFactory.getLogger(WorkerService.class);
    private ConcurrentMap<WorkerInfo, Set<TaskInfo>> workers = new ConcurrentHashMap<>();

    public WorkerService(){}

    public void registerWorker(WorkerInfo worker) {
        this.workers.putIfAbsent(worker, new HashSet<>());
    }

    public Set<WorkerInfo> getWorkers(){
        return this.workers.keySet();
    }

    public Set<TaskInfo> getWorkerTasks(WorkerInfo worker) {
        return this.workers.getOrDefault(worker, new HashSet<>());
    }

    public void removeWorkerTask(WorkerInfo workerInfo, TaskInfo task){
        this.workers.getOrDefault(workerInfo, new HashSet<>()).remove(task);
    }

    public void addWorkerTask(WorkerInfo worker, TaskInfo task){
        this.workers.putIfAbsent(worker, new HashSet<>()).add(task);
    }
}
