package org.example;

public class Configuration {

    private String matrices_path;
    private String platform;
    private String output;
    private String host;
    private String[] matrices;
    
    public String getOutput() {
    	return output;
    }
    
    public void setOutput(String output) {
    	this.output = output;
    }

    public String[] getMatrices() {
        return matrices;
    }

    public void setMatrices(String[] matrices) {
        this.matrices = matrices;
    }

    public String getMatrices_path() {
        return matrices_path;
    }

    public void setMatrices_path(String matrices_path) {
        this.matrices_path = matrices_path;
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
