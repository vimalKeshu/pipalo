package org.pipalo.common.model;

import lombok.Getter;
import org.apache.commons.collections.map.UnmodifiableMap;
import org.pipalo.common.constant.ApplicationConstant;
import org.pipalo.common.ops.FileSystemConfigOps;

public class SourceInfo {
    @Getter
    private String source;
    @Getter
    private UnmodifiableMap sourceConfig;

    private SourceInfo(SourceInfoBuilder builder) {
        this.source = builder.source;
        this.sourceConfig = builder.sourceConfig;
    }

    public static class SourceInfoBuilder {
        private static SourceInfoBuilder builder = new SourceInfoBuilder();
        private String source;
        private UnmodifiableMap sourceConfig;
        private SourceInfoBuilder(){}

        public static SourceInfo build() throws Exception {

            builder.source = System.getProperty(ApplicationConstant.ArgsConfig.SOURCE.getName());
            builder.sourceConfig = FileSystemConfigOps.getFileSystemInstance(builder.source);
            return new SourceInfo(builder);
        }
    }

}
