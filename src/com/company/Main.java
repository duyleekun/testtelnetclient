package com.company;

import com.pnm.m090.in.services.TelnetServiceDeployed;
import com.pnm.m090.in.services.TelnetServiceOnGit;
import vn.tripath.in.services.PooledTelnetService;

import java.util.LinkedList;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        final PooledTelnetService pool = new PooledTelnetService();
        LinkedList<Thread> threads = new LinkedList<Thread>();

        for (int threadId =0; threadId< 5; threadId++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    for (int loopId = 0; loopId < 100; loopId++) {
                        System.out.println(pool.sendCommand(Thread.currentThread().getId() + " " + String.valueOf(loopId)));
                    }
                }
            };
            thread.start();
            threads.push(thread);
        }
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(String.format("active %d idle %d",pool.getNumActive(), pool.getNumIdle()));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
