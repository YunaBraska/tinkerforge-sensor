package com.tinkerforge;

import static java.lang.String.format;

public class IPConnectionBaseProvider extends IPConnectionBase {

    private static final String ERROR = format("Not implemented - use [%s] instead", IPConnection.class.getSimpleName());

    public static String base58Encode(final long value){
        return IPConnectionBase.base58Encode(value);
    }

    public static long base58Decode(final String encoded){
        return IPConnectionBase.base58Decode(encoded);
    }

    @Override
    protected void callEnumerateListeners(final String s, final String s1, final char c, final short[] shorts, final short[] shorts1, final int i, final short i1) {
        throw new RuntimeException(ERROR);
    }

    @Override
    protected boolean hasEnumerateListeners() {
        throw new RuntimeException(ERROR);
    }

    @Override
    protected void callConnectedListeners(final short i) {
        throw new RuntimeException(ERROR);
    }

    @Override
    protected void callDisconnectedListeners(final short i) {
        throw new RuntimeException(ERROR);
    }

    @Override
    protected void callDeviceListener(final Device device, final byte b, final byte[] bytes) {
        throw new RuntimeException(ERROR);
    }
}
