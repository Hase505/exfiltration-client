package com.hase505.entities;

import com.hase505.strategies.encode.EncodeStrategy;
import com.hase505.strategies.send.SendStrategy;

public class ExfiltrationData {
    private byte[] data;

    public ExfiltrationData(byte[] data){
        this.data = data;
    }

    public String encodeData(EncodeStrategy encodeStrategy){
        return encodeStrategy.encode(data);
    }

    public void sendEncodedData(SendStrategy sendStrategy, EncodeStrategy encodeStrategy){
        String encodedData = encodeStrategy.encode(data);
        sendStrategy.send(encodedData);
    }
}
