package com.company;

import com.pnm.m090.in.services.TelnetServiceOnGit;

public class Main {


    public static void main(String[] args) {
        TelnetServiceOnGit client = new TelnetServiceOnGit();
        String session = client.getINConnection();
        client.sendCommand("He");
        client.sendCommand("Lo");
        client.sendCommand("Wor");
        client.sendCommand("1");
        client.sendCommand("2");
        client.sendCommand("3");
        client.sendCommand("Lo");
        client.sendCommand("Wor");
        client.sendCommand("1");
        client.sendCommand("2");
        client.sendCommand("3");
        client.sendCommand("Lo");
        client.sendCommand("Wor");
        client.sendCommand("1");
        client.sendCommand("2");
        client.sendCommand("3");
    }
}
