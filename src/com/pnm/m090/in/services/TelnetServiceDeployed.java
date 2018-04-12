/*
 * Decompiled with CFR 0_115.
 *
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.commons.net.telnet.TelnetClient
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.scheduling.annotation.Scheduled
 *  org.springframework.stereotype.Service
 */
package com.pnm.m090.in.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import org.apache.commons.net.telnet.TelnetClient;

public class TelnetServiceDeployed {
    private String host = "148.251.3.174";
    private String user = "user";
    private String password = "pass";
    private int port;
    private String promt;
    private final int waitTime = 50;
    private TelnetClient telnet = new TelnetClient();
    private InputStream in;
    private PrintStream out;
    public static String prompt = "PPSVNM33P,>";
    private String sessionId = "";
    int count = 1;

//    @Scheduled(fixedRate=30000)
//    private void autoKeepSession() {
//        logger.info("Count value =" + this.count);
//        logger.info(this.sessionId);
//        --this.count;
//        logger.info("Count value =" + this.count);
//        if (this.count > 0) return;
//        logger.info("Keep Alive Session" + new Date());
//        logger.info(this.getINConnection());
//    }

    public synchronized String getINConnection() {
//        if (!StringUtils.isBlank((CharSequence)this.sessionId)) {
//            this.sendCommand("\r\n");
//            return this.sessionId;
//        }
        try {
            this.telnet.setConnectTimeout(5000);
            this.telnet.connect(this.host, 23);
            this.in = this.telnet.getInputStream();
            this.out = new PrintStream(this.telnet.getOutputStream());
            this.readUntil("login: ");
            this.write(this.user);
            this.readUntil("INPwd: ");
            this.write(this.password);
            this.sessionId = this.readUntil(prompt);
            return this.sessionId;
        }
        catch (Exception e) {
            e.printStackTrace();
//            logger.error(e.getMessage());
            return this.sessionId;
        }
    }

    public String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            boolean found = false;
            char ch = (char)this.in.read();
            do {
                sb.append(ch);
                if (ch == lastChar && sb.toString().endsWith(pattern)) {
                    return sb.toString();
                }
                ch = (char)this.in.read();
            } while (true);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void write(String value) {
        try {
            this.out.println(value);
            this.out.flush();
            System.out.println(value);
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkAYT() {
        boolean ret = false;
        try {
            return this.telnet.sendAYT(500);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ret;
        }
        catch (IOException e) {
            e.printStackTrace();
            return ret;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public synchronized String sendCommand(String command) {
        String ret = "";
        try {
            this.write(command);
            this.count = 2;
            if (command.equalsIgnoreCase("\r\n")) {
                if (this.telnet.sendAYT(1000)) return ret;
                this.sessionId = "";
                return ret;
            }
            if (!this.telnet.sendAYT(1000)) {
                this.sessionId = "";
            }
            while ((ret = this.readUntil(prompt)).equals(prompt)) {
            }
            return ret;
        }
        catch (Exception e) {
            e.printStackTrace();
//            logger.error(e.getMessage());
            this.sessionId = "";
        }
        return ret;
    }

    public void disconnect() {
        try {
            this.write("exit");
            this.telnet.disconnect();
            this.sessionId = "";
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionId() {
        return this.sessionId;
    }
}

