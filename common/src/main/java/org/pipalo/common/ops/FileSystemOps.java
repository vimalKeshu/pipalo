package org.pipalo.common.ops;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
import org.pipalo.common.constant.ApplicationConstant;
import org.pipalo.common.util.FileUtils;

import javax.annotation.concurrent.ThreadSafe;
import javax.naming.OperationNotSupportedException;
import java.util.Map;

@ThreadSafe
public class FileSystemOps {
    private FileSystemOps(){}

    public static FileSystem getFileSystemInstance(String path, Map<String,String> config) throws Exception {
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

    public static FileSystem getLocalFileSystemInstance(String path, Map<String,String> config) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        return FileSystem.get(conf);
    }

    public static FileSystem getGcsFileSystemInstance(String path, Map<String,String> config) throws Exception {
        String serviceAccountKeyPath = config
                .get(ApplicationConstant.ObjectStorageConfig.APPLICATION_CREDENTIALS.getName());

        Configuration conf = new Configuration();
        conf.set("fs.gs.impl", com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem.class.getName());
        conf.set("fs.AbstractFileSystem.gs.impl", com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS.class.getName());
        conf.set("google.cloud.auth.service.account.enable", "true");
        conf.set("fs.defaultFS", path);
        conf.set("google.cloud.auth.service.account.json.keyfile", serviceAccountKeyPath);
        return FileSystem.get(conf);
    }

    public static FileSystem getHdfsFileSystemInstance(String path, Map<String,String> config) throws Exception {
        String hadoopConfDirPath = config.get(ApplicationConstant.HadoopConfig.HADOOP_HOME.getName());
        String kerberosPrincipal = config.get(ApplicationConstant.HadoopConfig.HADOOP_KERBEROS_PRINCIPAL.getName());
        String kerberosKeyTabFilePath = config.get(ApplicationConstant.HadoopConfig.HADOOP_KERBEROS_KEYTAB_PATH.getName());

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

    public static FileSystem getS3aFileSystemInstance(String path, Map<String,String> config) throws Exception {
        throw new OperationNotSupportedException("Not yet supported.");
    }

}
