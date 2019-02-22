package com.tinkerforge;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

// NOTE: the disconnect probe thread is not allowed to hold the socketMutex at any
//       time because it is created and joined while the socketMutex is locked
public class DisconnectProbeThread extends TinkerforgeThread {
    IPConnectionBase ipcon = null;
    byte[] request = null;
    LinkedBlockingQueue<Boolean> queue = new LinkedBlockingQueue<Boolean>();

    final static byte FUNCTION_DISCONNECT_PROBE = (byte) 128;
    final static int DISCONNECT_PROBE_INTERVAL = 5000;

    DisconnectProbeThread(IPConnectionBase ipcon) {
        super("Disconnect-Prober");

//        setDaemon(true);
        this.ipcon = ipcon;
        this.request = ipcon.createRequestPacket((byte) 8, FUNCTION_DISCONNECT_PROBE, null).array();
    }

    void shutdown() {
        try {
            queue.put(Boolean.TRUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Boolean item = null;

        while (!stop) {
            try {
                item = queue.poll(DISCONNECT_PROBE_INTERVAL, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (item != null) {
                break;
            }

            if (ipcon.disconnectProbeFlag) {
                try {
                    synchronized (ipcon.socketSendMutex) {
                        ipcon.out.write(request);
                    }
                } catch (java.net.SocketException e) {
                    ipcon.handleDisconnectByPeer(IPConnectionBase.DISCONNECT_REASON_ERROR,
                            ipcon.socketID, false);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ipcon.disconnectProbeFlag = true;
            }
        }
    }
}
