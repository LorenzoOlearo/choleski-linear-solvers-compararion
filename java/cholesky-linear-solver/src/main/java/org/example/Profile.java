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
    private String runtimeVersion;
    private String library;
    private String libraryVersion;

    public Profile() {
    	runtimeVersion = Runtime.version().toString();
    	library = org.ejml.All.class.getPackage().getName();
    	//libraryVersion = org.ejml.All.class.getPackage().getImplementationVersion();
    	libraryVersion = "0.43";
    }
    
    @Override
    public String toString() {
    	StringBuilder string  = new StringBuilder();
    	string.append(getName()).append(",")
	        .append(getSize()).append(",")
	        .append(getNumOfNonZero()).append(",")
	        .append(getRuntime() / 1E9D).append(",")
	        .append(getMemoryUsage() / 1E6D).append(",")
	        .append(getRelativeError()).append(",")
	        .append(getHost()).append(",")
	        .append(getPlatform()).append(",")
	        .append(getRuntimeVersion()).append(",")
	        .append(getLibrary()).append(",")
	        .append(getLibraryVersion());
    	return string.toString();
    }
    
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

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}

	public String getRuntimeVersion() {
		return runtimeVersion;
	}

	public void setRuntimeVersion(String runtimeVersion) {
		this.runtimeVersion = runtimeVersion;
	}

	public String getLibraryVersion() {
		return libraryVersion;
	}

	public void setLibraryVersion(String libraryVersion) {
		this.libraryVersion = libraryVersion;
	}
	
}
