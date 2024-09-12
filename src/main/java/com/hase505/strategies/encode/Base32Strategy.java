package com.hase505.strategies.encode;

import org.apache.commons.codec.binary.*;

public class Base32Strategy implements EncodeStrategy{
    private final Base32 base32 = new Base32();

    @Override
    public String encode(byte[] data){
        return base32.encodeAsString(data);
    }
}
