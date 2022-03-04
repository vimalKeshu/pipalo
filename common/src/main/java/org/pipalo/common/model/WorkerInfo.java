package org.pipalo.common.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class WorkerInfo {
    private final String id;
    private final String host;
    private final int port;

    public WorkerInfo(String id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerInfo that = (WorkerInfo) o;
        return port == that.port && id.equals(that.id) && host.equals(that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, host, port);
    }
}
