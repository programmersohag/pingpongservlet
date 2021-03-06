package com.sohaq;

public class PingResponse {

    private String ip;
    private int port;
    private String host;
    private long delay;
    private String timeout;

    public PingResponse(String timeout) {
        this.timeout = timeout;
    }

    public PingResponse(String ip, long delay) {
        this.ip = ip;
        this.delay = delay;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}
