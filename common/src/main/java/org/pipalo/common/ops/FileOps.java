package org.pipalo.common.ops;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.util.List;
import java.util.Map;

@ThreadSafe
public class FileOps {
    private static Logger logger = LoggerFactory.getLogger(FileOps.class);
    private FileOps(){}

    private static final int BLOCK_SIZE = 4096;

    public static void getFiles(FileSystem fs, Path path, List<FileStatus> files) throws Exception {
        for (FileStatus fileStatus: fs.listStatus(path)) {
            if (fileStatus.isDirectory())
                getFiles(fs, fileStatus.getPath(), files);
            else files.add(fileStatus);
        }
    }

    public static void copy(String source,
                            Map<String,String> sourceConfig,
                            String target,
                            Map<String,String> targetConfig) throws Exception {
        FileSystem sourceFileSystem = FileSystemOps.getFileSystemInstance(source, sourceConfig);
        FileSystem targetFileSystem = FileSystemOps.getFileSystemInstance(target, targetConfig);

        Path sourcePath = new Path(source);
        Path targetPath = new Path(target);

        if (!sourceFileSystem.exists(sourcePath))
            throw new Exception("Not able to find the file: "+sourcePath.getName());

        if (targetFileSystem.exists(targetPath))
            targetFileSystem.delete(targetPath, true);

        try(FSDataInputStream fsDataInputStream = sourceFileSystem.open(sourcePath);
            FSDataOutputStream fsDataOutputStream = targetFileSystem.create(targetPath)){
            IOUtils.copyBytes(fsDataInputStream, fsDataOutputStream, BLOCK_SIZE, true);
        }
    }

    public static FileStatus[] browseFiles(String fsPath, Map<String,String> config) throws Exception {
        FileSystem fileSystem = FileSystemOps.getFileSystemInstance(fsPath, config);
        Path path = new Path(fsPath);
        if (!fileSystem.exists(path)) fileSystem.mkdirs(path);
        for (FileStatus file: fileSystem.listStatus(path))
            logger.info(file.getPath().getName());
        return fileSystem.listStatus(path);
    }

}
