package org.pipalo.common.constant;

public class ApplicationConstant {

    public interface Config {
        String getName();
    }

    public enum ArgsConfig implements Config {
        SOURCE("SOURCE"),
        TARGET("TARGET");
        private String name;
        ArgsConfig(String name) {this.name=name;}
        public String getName() { return name; }
    }

    public enum HadoopConfig implements Config{
        HADOOP_HOME("HADOOP_HOME"),
        HADOOP_USER("HADOOP_USER"),
        HADOOP_KERBEROS_PRINCIPAL("HADOOP_KERBEROS_PRINCIPAL"),
        HADOOP_KERBEROS_KEYTAB_PATH("HADOOP_KERBEROS_KEYTAB_PATH");
        private String name;
        HadoopConfig(String name) {this.name=name;}
        public String getName() { return name; }
    }

    public enum ObjectStorageConfig implements Config {
        APPLICATION_CREDENTIALS("APPLICATION_CREDENTIALS");
        private String name;
        ObjectStorageConfig(String name) {this.name=name;}
        public String getName() { return name; }
    }

}
