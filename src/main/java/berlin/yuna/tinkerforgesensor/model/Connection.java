package berlin.yuna.tinkerforgesensor.model;

import berlin.yuna.tinkerforgesensor.model.exception.NetworkConnectionException;
import com.tinkerforge.IPConnection;

public class Connection {

    private final String id;
    private final String host;
    private final String password;
    private final Integer port;
    private final boolean ignoreConnectionError;
    private final IPConnection ipConnection = new IPConnection();

    public Connection() {
        this(null, null, null, false);
    }

    public Connection(final String host, final Integer port) {
        this(host, port == null? 4223 : port, null, false);
    }

    public Connection(final String host, final Integer port, final boolean ignoreConnectionError) {
        this(host, port == null? 4223 : port, null, ignoreConnectionError);
    }

    public Connection(final String host, final Integer port, final String password) throws NetworkConnectionException {
        this(host, port == null? 4223 : port, password, false);
    }

    public Connection(final String host, final Integer port, final String password, final boolean ignoreConnectionError) {
        this.host = host;
        this.password = password;
        this.port = port;
        this.ignoreConnectionError = ignoreConnectionError;
        this.id = host + ":" + port;
    }

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public Integer getPort() {
        return port;
    }

    public boolean isIgnoreConnectionError() {
        return ignoreConnectionError;
    }

    public IPConnection getIpConnection() {
        return ipConnection;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Connection that = (Connection) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "stackId='" + id + '\'' +
                ", host='" + host + '\'' +
                ", password='" + (password == null? null : "****") + '\'' +
                ", port=" + port +
                ", ignoreConnectionError=" + ignoreConnectionError +
                ", ipConnection=" + ipConnection +
                '}';
    }
}
