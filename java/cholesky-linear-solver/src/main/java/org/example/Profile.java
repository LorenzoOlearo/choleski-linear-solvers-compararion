package org.example;

public class Profile {
    private long runtime;
    private double relativeError;
    private long memoryUsage;
    private int numOfNonZero;
    private int size;
    private String platform;
    private String host;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }

    public double getRelativeError() {
        return relativeError;
    }

    public void setRelativeError(double relativeError) {
        this.relativeError = relativeError;
    }

    public long getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public int getNumOfNonZero() {
        return numOfNonZero;
    }

    public void setNumOfNonZero(int numOfNonZero) {
        this.numOfNonZero = numOfNonZero;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
