package com.tinkerforge;

import java.util.UUID;

public class DummyDevice extends Device {

    public static final Integer DEVICE_IDENTIFIER = -1;
    private final Identity identity = prepareIdentity();

    public DummyDevice() {
        this(null, null);
    }

    public DummyDevice(final String uid, final IPConnection ipCon) {
        super(IPConnectionBaseProvider.base58Encode(System.currentTimeMillis()), new IPConnection());
        apiVersion[0] = 0;
        apiVersion[1] = 0;
        apiVersion[2] = 1;
    }

    @Override
    public Identity getIdentity() {
        return identity == null ? prepareIdentity() : identity;
    }

    private Identity prepareIdentity() {
        final Identity identity = new Identity();
        identity.uid = UUID.randomUUID().toString();
        identity.connectedUid = UUID.randomUUID().toString();
        identity.position = '\n';
        identity.firmwareVersion = apiVersion;
        identity.deviceIdentifier = -1;
        identity.hardwareVersion = apiVersion;
        return identity;
    }
}
