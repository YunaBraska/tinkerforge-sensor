package com.tinkerforge;

public class CallbackThreadRestarter implements Thread.UncaughtExceptionHandler {
    IPConnectionBase ipcon = null;

    CallbackThreadRestarter(final IPConnectionBase ipcon) {
        this.ipcon = ipcon;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable exception) {
        System.err.print("Exception in thread \"" + thread.getName() + "\" ");
        exception.printStackTrace();

        ipcon.callbackThread = new CallbackThread(ipcon, ((CallbackThread) thread).callbackQueue);
        ipcon.callbackThread.start();
    }
}
