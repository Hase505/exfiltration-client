package com.hase505.strategies.encode;

import org.apache.commons.codec.binary.Hex;

public class HexadecimalStrategy implements EncodeStrategy{
    @Override
    public String encode(byte[] data) {
        return Hex.encodeHexString(data);
    }

}
