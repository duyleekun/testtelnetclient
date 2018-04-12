package vn.tripath.in.services;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.IOException;

public class PooledTelnetService {
    public static String prompt = "PPSVNM33P,>";

    private String host = "148.251.3.174";
    private String user = "user";
    private String password = "pas";


    private GenericObjectPool<MyTelnetClient> pool;
    private long timeout = 2000;



    public PooledTelnetService() {
        pool = new GenericObjectPool<MyTelnetClient>(new PooledObjectFactory<MyTelnetClient>() {
            public PooledObject<MyTelnetClient> makeObject() throws Exception {
                MyTelnetClient telnet = new MyTelnetClient();
                telnet.setDefaultTimeout(2000);
                telnet.connect(host, 23);
                telnet.readUntil("login: ");
                telnet.write(user);
                telnet.readUntil("INPwd: ");
                telnet.write(password);
                telnet.sessionId = telnet.readUntil(prompt);
                return new DefaultPooledObject<MyTelnetClient>(telnet);
            }

            public void destroyObject(PooledObject<MyTelnetClient> p) throws Exception {
                p.getObject().disconnect();
            }

            public boolean validateObject(PooledObject<MyTelnetClient> p) {
                try {
                    return p.getObject().sendAYT(timeout);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return false;
            }

            public void activateObject(PooledObject<MyTelnetClient> p) throws Exception {

            }

            public void passivateObject(PooledObject<MyTelnetClient> p) throws Exception {

            }
        });
        pool.setMinIdle(0);
        pool.setMaxIdle(2);
        pool.setMaxTotal(10);
    }

    public String sendCommand(String command) {
        String ret = null;
        int n = 0;
        do {
            MyTelnetClient myTelnetClient = null;
            try {
                if (n > 0) {
                    //Exponential back off
                    Thread.sleep((long) Math.min(1000 * Math.pow(2, n),30000));
                }

                myTelnetClient = pool.borrowObject();
                myTelnetClient.write(command);

                do {
                    ret = myTelnetClient.readUntil(prompt);
                }
                while (ret.equals(prompt));
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    // Any failure, just ditch the object
                    pool.invalidateObject(myTelnetClient);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } finally {
                if (myTelnetClient != null) {
                    pool.returnObject(myTelnetClient);
                }
            }
            n++;
        } while (ret == null);
        return ret;
    }

    public int getNumActive() {
        return pool.getNumActive();
    }

    public int getNumIdle() {
        return pool.getNumIdle();
    }

    public long getMeanActiveTimeMillis() {
        return pool.getMeanActiveTimeMillis();
    }

    public long getMeanIdleTimeMillis() {
        return pool.getMeanIdleTimeMillis();
    }
}