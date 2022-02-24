package org.pipalo.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Objects;

public class FileUtils {
    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private FileUtils(){}

    public static String findHostFileSystem(String path) throws Exception {
        Objects.requireNonNull(path , "Provided path is null.");
        URI uri = URI.create(path);
        String scheme = uri.getScheme();
        logger.debug("scheme: "+scheme);
        if (scheme == null) throw new Exception("Not abel to find the scheme from path: "+path);
        String rootPath = uri.getRawSchemeSpecificPart().replace("//","/");
        logger.debug("rootPath: "+rootPath);
        String hostPath = findHost(rootPath);
        logger.debug("hostPath: "+hostPath);
        return scheme + "://" + hostPath;
    }

    public static String findScheme(String path) {
        Objects.requireNonNull(path , "Provided path is null.");
        URI uri = URI.create(path);
        return uri.getScheme();
    }

    private static String findHost(String path) {
        char pathSeparator = path.charAt(0);
        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        while (pathSeparator != path.charAt(i)){
            stringBuilder.append(path.charAt(i));
            i = i + 1;
        }
        return stringBuilder.toString();
    }

}
