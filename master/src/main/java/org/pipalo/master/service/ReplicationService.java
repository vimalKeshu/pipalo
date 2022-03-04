package org.pipalo.master.service;

import lombok.Getter;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.pipalo.common.model.SourceInfo;
import org.pipalo.common.model.TargetInfo;
import org.pipalo.common.model.TaskInfo;
import org.pipalo.common.ops.FileOps;
import org.pipalo.common.ops.FileSystemOps;
import org.pipalo.master.queue.TaskQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReplicationService {
    private static Logger logger = LoggerFactory.getLogger(ReplicationService.class);
    @Getter
    private final SourceInfo sourceInfo;
    @Getter
    private final TargetInfo targetInfo;
    @Getter
    private final TaskQueue<TaskInfo> queue;

    public ReplicationService() throws Exception {
        this.sourceInfo = SourceInfo.SourceInfoBuilder.build();
        this.targetInfo = TargetInfo.TargetInfoBuilder.build();
        this.queue = new TaskQueue<>();
        fillQueue();
    }

    private void fillQueue() throws Exception {
        FileSystem sourceFileSystem = FileSystemOps
                .getFileSystemInstance(sourceInfo.getSource(), sourceInfo.getSourceConfig());
        FileSystem targetFileSystem = FileSystemOps
                .getFileSystemInstance(targetInfo.getTarget(), targetInfo.getTargetConfig());
        Path sourcePath = new Path(sourceInfo.getSource());
        Path targetPath = new Path(targetInfo.getTarget());

        if (!sourceFileSystem.exists(sourcePath))
            throw new Exception("Not able to find the source path: "+sourceInfo.getSource());
        if (!targetFileSystem.exists(targetPath))
            targetFileSystem.mkdirs(targetPath);

        List<FileStatus> sourceFiles = new ArrayList<>();
        FileOps.getFiles(sourceFileSystem, sourcePath, sourceFiles);
        logger.info("Total files: "+sourceFiles.size());
        if (!sourceFiles.isEmpty()) {
            for (FileStatus fileStatus: sourceFiles) {
                String pathPrefix = fileStatus.getPath()
                        .toString()
                        .replaceFirst(sourceInfo.getSource(), "");
                String source = fileStatus.getPath().toString();
                String target = targetPath + pathPrefix;
                queue.push(new TaskInfo(UUID.randomUUID().toString(),
                        source,
                        sourceInfo.getSourceConfig(),
                        target,
                        targetInfo.getTargetConfig()
                        ));
            }
        }
        logger.info("Total files submitted: "+queue.size());
    }

}
