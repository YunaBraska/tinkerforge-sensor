package tinkerforge;

import com.tinkerforge.IPConnection;

public class IPTestConnection extends IPConnection {

    public IPTestConnection() {
    }

    public void callEnumerateListeners(
            final String uid,
            final String connectedUid,
            final char position,
            final int deviceIdentifier,
            final short enumerationType
    ) {
        super.callEnumerateListeners(
                uid,
                connectedUid,
                position,
                new short[]{1, 2, 3},
                new short[]{3, 2, 1},
                deviceIdentifier,
                enumerationType
        );
    }
}
