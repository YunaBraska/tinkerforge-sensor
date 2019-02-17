package com.tinkerforge;

public class ReceiveThread extends TinkerforgeThread {
    IPConnectionBase ipcon = null;

    ReceiveThread(IPConnectionBase ipcon) {
        super("Brickd-Receiver");

//        setDaemon(true);
        this.ipcon = ipcon;
    }

    @Override
    public void run() {
        byte[] pendingData = new byte[8192];
        int pendingLength = 0;
        long socketID = ipcon.socketID;

        while (!stop && ipcon.receiveFlag) {
            int length;

            try {
                length = ipcon.in.read(pendingData, pendingLength,
                        pendingData.length - pendingLength);
            } catch (java.net.SocketException e) {
                if (ipcon.receiveFlag) {
                    ipcon.handleDisconnectByPeer(IPConnectionBase.DISCONNECT_REASON_ERROR,
                            socketID, false);
                }

                return;
            } catch (Exception e) {
                if (ipcon.receiveFlag) {
                    e.printStackTrace();
                }

                return;
            }

            if (length <= 0) {
                if (ipcon.receiveFlag) {
                    ipcon.handleDisconnectByPeer(IPConnectionBase.DISCONNECT_REASON_SHUTDOWN,
                            socketID, false);
                }

                return;
            }

            pendingLength += length;

            while (!stop && ipcon.receiveFlag) {
                if (pendingLength < 8) {
                    // Wait for complete header
                    break;
                }

                length = IPConnectionBase.getLengthFromData(pendingData);

                if (pendingLength < length) {
                    // Wait for complete packet
                    break;
                }

                byte[] packet = new byte[length];

                System.arraycopy(pendingData, 0, packet, 0, length);
                System.arraycopy(pendingData, length, pendingData, 0, pendingLength - length);
                pendingLength -= length;
                ipcon.handleResponse(packet);
            }
        }
    }
}
