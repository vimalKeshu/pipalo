package org.pipalo.common.ops;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;
import org.pipalo.common.constant.ApplicationConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pipalo.common.util.FileUtils;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Properties;

public class FileOperations {
    private static Logger logger = LoggerFactory.getLogger(FileOperations.class);
    private FileOperations(){}

    private static final int BLOCK_SIZE = 4096;

    public static FileSystem getFileSystemInstance(String path, Properties config) throws Exception {
        FileSystem fileSystem = null;
        String hostPath = FileUtils.findHostFileSystem(path);
        String scheme = FileUtils.findScheme(path);
        switch (scheme.toLowerCase()) {
            case "hdfs":
                fileSystem = getHdfsFileSystemInstance(hostPath, config);
                break;
            case "gs":
                fileSystem = getGcsFileSystemInstance(hostPath, config);
                break;
            case "file":
                fileSystem = getLocalFileSystemInstance(hostPath, config);
                break;
            case "local":
                String tPath = hostPath.startsWith("local")
                        ? hostPath.replace("local:", "file:")
                        : hostPath;
                fileSystem = getLocalFileSystemInstance(tPath, config);
                break;
            case "s3a":
                throw new UnsupportedOperationException("Currently scheme,"+scheme+ " not supported. Work in progress..");
            default:
                throw new UnsupportedOperationException("Currently scheme,"+scheme+ " not supported.");
        }
        return fileSystem;
    }

    public static FileSystem getLocalFileSystemInstance(String path, Properties config) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        return FileSystem.get(conf);
    }

    public static FileSystem getGcsFileSystemInstance(String path, Properties config) throws Exception {
        String serviceAccountKeyPath = config
                .getProperty(ApplicationConstant.ObjectStorageConfig.APPLICATION_CREDENTIALS.getName());

        Configuration conf = new Configuration();
        conf.set("fs.gs.impl", com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem.class.getName());
        conf.set("fs.AbstractFileSystem.gs.impl", com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS.class.getName());
        conf.set("google.cloud.auth.service.account.enable", "true");
        conf.set("fs.defaultFS", path);
        conf.set("google.cloud.auth.service.account.json.keyfile", serviceAccountKeyPath);
        return FileSystem.get(conf);
    }

    public static FileSystem getHdfsFileSystemInstance(String path, Properties config)throws Exception {
        String hadoopConfDirPath = config.getProperty(ApplicationConstant.HadoopConfig.HADOOP_HOME.getName());
        String kerberosPrincipal = config.getProperty(ApplicationConstant.HadoopConfig.HADOOP_KERBEROS_PRINCIPAL.getName());
        String kerberosKeyTabFilePath = config.getProperty(ApplicationConstant.HadoopConfig.HADOOP_KERBEROS_KEYTAB_PATH.getName());

        Configuration conf = new Configuration();
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        conf.addResource(new Path("file://"+ hadoopConfDirPath + "/core-site.xml"));
        conf.addResource(new Path("file://"+ kerberosPrincipal + "/hdfs-site.xml"));
        conf.set("fs.defaultFS", path);
        conf.set("hadoop.security.authentication", "kerberos");

        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation.loginUserFromKeytab(kerberosPrincipal,
                kerberosKeyTabFilePath);

        return FileSystem.get(conf);
    }


    public static FileSystem getS3aFileSystemInstance(String path, Properties config) throws Exception {
        throw new OperationNotSupportedException("Not yet supported.");
    }

    public static void getFiles(FileSystem fs, Path path, List<FileStatus> files) throws Exception {
        for (FileStatus fileStatus: fs.listStatus(path)) {
            if (fileStatus.isDirectory())
                getFiles(fs, fileStatus.getPath(), files);
            else files.add(fileStatus);
        }
    }

    public static void copy(String source,
                            Properties sourceConfig,
                            String target,
                            Properties targetConfig) throws Exception {
        FileSystem sourceFileSystem = getFileSystemInstance(source, sourceConfig);
        FileSystem targetFileSystem = getFileSystemInstance(target, targetConfig);

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

    public static FileStatus[] browseFiles(String fsPath, Properties config) throws Exception {
        FileSystem fileSystem = getFileSystemInstance(fsPath, config);
        Path path = new Path(fsPath);
        if (!fileSystem.exists(path)) fileSystem.mkdirs(path);
        for (FileStatus file: fileSystem.listStatus(path))
            logger.info(file.getPath().getName());
        return fileSystem.listStatus(path);
    }

}
