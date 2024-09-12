package com.hase505.strategies.encode;

import java.util.List;
import java.util.ArrayList;

public class CompositeEncodingStrategy implements EncodeStrategy{
    private List<EncodeStrategy> strategies = new ArrayList<>();

    public void addStrategy(EncodeStrategy strategy){
        strategies.add(strategy);
    }

    @Override
    public String encode(byte[] data) {
        byte[] result = data;
        for(EncodeStrategy strategy : strategies){
            result = strategy.encode(result).getBytes();
        }
        return new String(result);

    }
}



