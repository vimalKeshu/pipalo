package org.pipalo.common.ops;

import org.apache.commons.collections.map.UnmodifiableMap;
import org.apache.hadoop.fs.FileSystem;
import org.pipalo.common.constant.ApplicationConstant;
import org.pipalo.common.util.FileUtils;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ThreadSafe
public class FileSystemConfigOps {
    private FileSystemConfigOps(){}


    public static UnmodifiableMap getFileSystemInstance(String path) throws Exception {
        String scheme = FileUtils.findScheme(path);
        Map<String, String> config = new HashMap<>();

        switch (scheme.toLowerCase()) {
            case "hdfs":
                for (ApplicationConstant.HadoopConfig hConfig: ApplicationConstant.HadoopConfig.values()){
                    config.put(hConfig.getName(), System.getProperty(hConfig.getName()));
                }
                break;
            case "gs":
            case "s3a":
                for (ApplicationConstant.ObjectStorageConfig oConfig: ApplicationConstant.ObjectStorageConfig.values()){
                    config.put(oConfig.getName(), System.getProperty(oConfig.getName()));
                }
                break;
            case "file":
            case "local":
                break;
            default:
        }
        return (UnmodifiableMap) Collections.unmodifiableMap(config);
    }
}
