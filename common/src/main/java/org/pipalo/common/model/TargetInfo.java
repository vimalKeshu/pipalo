package org.pipalo.common.model;

import lombok.Getter;
import org.apache.commons.collections.map.UnmodifiableMap;
import org.pipalo.common.constant.ApplicationConstant.ArgsConfig;
import org.pipalo.common.ops.FileSystemConfigOps;

public class TargetInfo {
    @Getter
    private String target;
    @Getter
    private UnmodifiableMap targetConfig;

    private TargetInfo(TargetInfoBuilder builder){
        this.target = builder.target;
        this.targetConfig = builder.targetConfig;
    }

    public static class TargetInfoBuilder{
        private static TargetInfoBuilder builder = new TargetInfoBuilder();
        private String target;
        private UnmodifiableMap targetConfig;
        private TargetInfoBuilder(){}

        public static TargetInfo build() throws Exception {
            builder.target = System.getProperty(ArgsConfig.TARGET.getName());
            builder.targetConfig = FileSystemConfigOps.getFileSystemInstance(builder.target);
            return new TargetInfo(builder);
        }
    }

}
