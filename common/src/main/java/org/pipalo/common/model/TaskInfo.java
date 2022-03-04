package org.pipalo.common.model;

import lombok.Getter;

import java.util.Map;
import java.util.Objects;

@Getter
public class TaskInfo {
    private final String taskId;
    private final String sourcePath;
    private final Map<String,String> sourceConfig;
    private final String targetPath;
    private final Map<String, String> targetConfig;

    public TaskInfo(String taskId,
                    String sourcePath,
                    Map<String,String> sourceConfig,
                    String targetPath, Map<String,
                    String> targetConfig) {
        this.taskId = taskId;
        this.sourcePath = sourcePath;
        this.sourceConfig = sourceConfig;
        this.targetPath = targetPath;
        this.targetConfig = targetConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskInfo taskInfo = (TaskInfo) o;
        return sourcePath.equals(taskInfo.sourcePath) && targetPath.equals(taskInfo.targetPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourcePath, targetPath);
    }
}
