package vn.tripath.in.services;

import java.io.PrintStream;

public class MyTelnetClient extends org.apache.commons.net.telnet.TelnetClient {


    public String sessionId;

    public MyTelnetClient() {
        super();
    }

    public String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            boolean found = false;
            char ch = (char) this.getInputStream().read();
            do {
                sb.append(ch);
                if (ch == lastChar && sb.toString().endsWith(pattern)) {
                    return sb.toString();
                }
                ch = (char) this.getInputStream().read();
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void write(String value) {
        PrintStream printStream = new PrintStream(this.getOutputStream());
        printStream.println(value);
        printStream.flush();
    }
}
