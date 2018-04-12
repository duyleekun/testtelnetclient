package com.pnm.m090.in.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;

import org.apache.commons.net.telnet.TelnetClient;


public class TelnetServiceOnGit {

	/*private String host;
    private int port;
	private String user;
	private String password;*/


    private String host = "148.251.3.174";

    private String user = "user";

    private String password = "pas";

    private int port;

    private String promt;

    private final int waitTime = 50;

    private TelnetClient telnet = new TelnetClient();
    private InputStream in;
    private PrintStream out;
    public static String prompt = "PPSVNM33P,>";
    private String sessionId = "";




    public synchronized String getINConnection() {
        if (this.telnet.isConnected()) {
            try {
                this.telnet.disconnect();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        try {
            this.telnet.setDefaultTimeout(6000);
            this.telnet.connect(this.host, 23);
            this.in = this.telnet.getInputStream();
            this.out = new PrintStream(this.telnet.getOutputStream());
            this.readUntil("login: ");
            this.write(user);
            this.readUntil("INPwd: ");
            this.write(password);
            sessionId = this.readUntil(prompt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionId;
    }


    public String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            //logger.info("LastChar = "+ lastChar);
            StringBuffer sb = new StringBuffer();
            boolean found = false;
            char ch = (char) this.in.read();
// 	            logger.info(String.valueOf(ch));
            do {
                //System.out.print(ch);
                //logger.info("Ch = "+String.valueOf(ch));
                sb.append(ch);
                if (ch == lastChar && sb.toString().endsWith(pattern)) {
                    //logger.info("SB toString = "+sb.toString());
                    return sb.toString();
                }
                ch = (char) this.in.read();
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void write(String value) {
        try {
            this.out.println(value);
            this.out.flush();
            System.out.println(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkAYT() {
        boolean ret = false;
        try {
            ret = this.telnet.sendAYT(1000);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    public synchronized String sendCommand(String command) {
        String ret = "";
        try {
            this.write(command);
            if (command.equalsIgnoreCase("\r\n")) {
                if (!this.telnet.sendAYT(1000)) {
                    // not received response, disconnect, reset sessionId
                    sessionId = "";
                    //getINConnection();
                }

            } else {
                if (!this.telnet.sendAYT(1000)) {
                    // not received response, disconnect, reset sessionId
                    sessionId = "";
                    getINConnection();
                    this.write(command);
                }


                do {
                    ret = this.readUntil(prompt);
                }
                while (ret.equals(prompt));
            }
 	            /*this.write("exit");
 	            System.out.println("send Command:" + ret);*/
            //disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            sessionId = "";
            // disconnect();
        }
        return ret;
    }


    public void disconnect() {
        try {
            // this.write("exit");
            this.telnet.disconnect();
            sessionId = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getSessionId() {
        return sessionId;
    }

}
