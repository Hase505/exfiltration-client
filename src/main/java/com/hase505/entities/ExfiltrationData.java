package com.hase505.entities;

import com.hase505.strategies.encode.EncodeStrategy;
import com.hase505.strategies.send.SendStrategy;

public class ExfiltrationData {
    private EncodeStrategy encodeStrategy;
    private SendStrategy sendStrategy;
    private byte[] data;

    public ExfiltrationData(byte[] data){
        this.data = data;
    }
    public ExfiltrationData(byte[] data, EncodeStrategy encodeStrategy, SendStrategy sendStrategy){
        this.data = data;
        this.encodeStrategy = encodeStrategy;
        this.sendStrategy = sendStrategy;
    }

    public void setEncodeStrategy(EncodeStrategy encodeStrategy) {
        this.encodeStrategy = encodeStrategy;
    }

    public void setSendStrategy(SendStrategy sendStrategy) {
        this.sendStrategy = sendStrategy;
    }

    public String encodeData(){
        return encodeStrategy.encode(data);
    }

    public void sendEncodedData(){
        String encodedData = encodeData();
        sendStrategy.send(encodedData);
    }
}
