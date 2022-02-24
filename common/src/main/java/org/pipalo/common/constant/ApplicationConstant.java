package org.pipalo.common.constant;

public class ApplicationConstant {

    public enum HadoopConfig {
        HADOOP_HOME("HADOOP_HOME"),
        HADOOP_USER("HADOOP_USER"),
        HADOOP_KERBEROS_PRINCIPAL("HADOOP_KERBEROS_PRINCIPAL"),
        HADOOP_KERBEROS_KEYTAB_PATH("HADOOP_KERBEROS_KEYTAB_PATH");
        private String name;
        HadoopConfig(String name) {this.name=name;}
        public String getName() { return name; }
    }

    public enum ObjectStorageConfig {
        APPLICATION_CREDENTIALS("APPLICATION_CREDENTIALS");
        private String name;
        ObjectStorageConfig(String name) {this.name=name;}
        public String getName() { return name; }
    }

}
