package org.pipalo.master.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TaskQueue<T> {
    private Queue<T> tasks = new LinkedList<>();
    public TaskQueue(){}

    public synchronized List<T> pop(int noOfTasks) {
        List<T> taskList = new ArrayList<>();
        if (tasks.isEmpty()) return taskList;
        int cnt = 0;
        while (!tasks.isEmpty()) {
            taskList.add(tasks.poll());
            cnt++;
            if (cnt >= noOfTasks) break;
        }
        return taskList;
    }

    public synchronized void push(List<T> taskList) {
        this.tasks.addAll(taskList);
    }

    public synchronized void push(T task) {
        this.tasks.add(task);
    }

    public int size() {return this.tasks.size();}
}
