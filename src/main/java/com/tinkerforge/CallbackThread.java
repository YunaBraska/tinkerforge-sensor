package com.tinkerforge;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.LinkedBlockingQueue;

public class CallbackThread extends TinkerforgeThread {
    IPConnectionBase ipcon = null;
    LinkedBlockingQueue<IPConnectionBase.CallbackQueueObject> callbackQueue = null;
    Object mutex = new Object();
    boolean packetDispatchAllowed = false;

    CallbackThread(IPConnectionBase ipcon,
                   LinkedBlockingQueue<IPConnectionBase.CallbackQueueObject> callbackQueue) {
        super("Callback-Processor");

//        setDaemon(true);
        this.ipcon = ipcon;
        this.callbackQueue = callbackQueue;
//        this.setUncaughtExceptionHandler(new CallbackThreadRestarter(ipcon));
    }

    void setPacketDispatchAllowed(boolean allowed) {
        if (allowed) {
            packetDispatchAllowed = true;
        } else {
            if (Thread.currentThread() != this) {
                // FIXME: cannot lock callback mutex here because this can
                //        deadlock due to an ordering problem with the socket mutex
                /*synchronized (mutex)*/
                {
                    packetDispatchAllowed = false;
                }
            } else {
                packetDispatchAllowed = false;
            }
        }
    }

    void dispatchMeta(IPConnectionBase.CallbackQueueObject cqo) {
        switch (cqo.functionID) {
            case IPConnectionBase.CALLBACK_CONNECTED:
                ipcon.callConnectedListeners(cqo.parameter);

                break;

            case IPConnectionBase.CALLBACK_DISCONNECTED:
                // need to do this here, the receive loop is not allowed to
                // hold the socket mutex because this could cause a deadlock
                // with a concurrent call to the (dis-)connect function
                if (cqo.parameter != IPConnectionBase.DISCONNECT_REASON_REQUEST) {
                    synchronized (ipcon.socketMutex) {
                        // don't close the socket if it got disconnected or
                        // reconnected in the meantime
                        if (ipcon.socket != null && ipcon.socketID == cqo.socketID) {
                            ipcon.disconnectProbeThread.shutdown();

                            try {
                                ipcon.disconnectProbeThread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            ipcon.closeSocket();
                        }
                    }
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ipcon.callDisconnectedListeners(cqo.parameter);

                if (cqo.parameter != IPConnectionBase.DISCONNECT_REASON_REQUEST &&
                        ipcon.autoReconnect && ipcon.autoReconnectAllowed) {
                    ipcon.autoReconnectPending = true;
                    boolean retry = true;

                    while (!stop && retry) {
                        retry = false;

                        synchronized (ipcon.socketMutex) {
                            if (ipcon.autoReconnectAllowed && ipcon.socket == null) {
                                try {
                                    ipcon.connectUnlocked(true);
                                } catch (Exception e) {
                                    retry = true;
                                }
                            } else {
                                ipcon.autoReconnectPending = true;
                            }
                        }

                        if (retry) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                break;
        }
    }

    void dispatchPacket(IPConnectionBase.CallbackQueueObject cqo) {
        byte functionID = IPConnectionBase.getFunctionIDFromData(cqo.packet);

        if (functionID == IPConnectionBase.CALLBACK_ENUMERATE) {
            if (ipcon.hasEnumerateListeners()) {
                int length = IPConnectionBase.getLengthFromData(cqo.packet);
                ByteBuffer bb = ByteBuffer.wrap(cqo.packet, 8, length - 8);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                String uid_str = "";
                for (int i = 0; i < 8; i++) {
                    char c = (char) bb.get();
                    if (c != '\0') {
                        uid_str += c;
                    }
                }
                String connectedUid_str = "";
                for (int i = 0; i < 8; i++) {
                    char c = (char) bb.get();
                    if (c != '\0') {
                        connectedUid_str += c;
                    }
                }
                char position = (char) bb.get();
                short[] hardwareVersion = new short[3];
                hardwareVersion[0] = IPConnectionBase.unsignedByte(bb.get());
                hardwareVersion[1] = IPConnectionBase.unsignedByte(bb.get());
                hardwareVersion[2] = IPConnectionBase.unsignedByte(bb.get());
                short[] firmwareVersion = new short[3];
                firmwareVersion[0] = IPConnectionBase.unsignedByte(bb.get());
                firmwareVersion[1] = IPConnectionBase.unsignedByte(bb.get());
                firmwareVersion[2] = IPConnectionBase.unsignedByte(bb.get());
                int deviceIdentifier = IPConnectionBase.unsignedShort(bb.getShort());
                short enumerationType = IPConnectionBase.unsignedByte(bb.get());

                ipcon.callEnumerateListeners(uid_str, connectedUid_str, position,
                        hardwareVersion, firmwareVersion,
                        deviceIdentifier, enumerationType);
            }
        } else {
            long uid = IPConnectionBase.getUIDFromData(cqo.packet);
            Device device = ipcon.devices.get(uid);

            ipcon.callDeviceListener(device, functionID, cqo.packet);
        }
    }

    @Override
    public void run() {
        while (!stop) {
            IPConnectionBase.CallbackQueueObject cqo = null;

            try {
                cqo = callbackQueue.take();
            } catch (InterruptedException e) {
//                e.printStackTrace();
                continue;
            }

            if (cqo == null) {
                continue;
            }

            // FIXME: cannot lock callback mutex here because this can
            //        deadlock due to an ordering problem with the socket mutex
            /*synchronized (mutex)*/
            {
                switch (cqo.kind) {
                    case IPConnectionBase.QUEUE_EXIT:
                        return;

                    case IPConnectionBase.QUEUE_META:
                        dispatchMeta(cqo);
                        break;

                    case IPConnectionBase.QUEUE_PACKET:
                        // don't dispatch callbacks when the receive thread isn't running
                        if (packetDispatchAllowed) {
                            dispatchPacket(cqo);
                        }

                        break;
                }
            }
        }
    }
}
