package dev.cromo29.durkcore.Util;


import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class MySQL {

    private String host, db, user, pass;
    private int port;
    private Connection c;
    private Statement ps;

    @Deprecated
    public MySQL(Plugin pl, String host, int port, String database, String user, String pass) {
        this(host, port, database, user, pass);
    }

    public MySQL(String host, int port, String database, String user, String pass) {
        this.host = host;
        this.port = port;
        this.db = database;
        this.user = user;
        this.pass = pass;
    }

    public boolean isConnected() {
        try {
            return c != null && !c.isClosed();
        } catch (SQLException ignored) {
        }
        return false;
    }

    public boolean ensureIsConnected() {

        if (!isConnected()) {
            c = null;
            ps = null;

            boolean connected = openConnection();

            if (connected) System.out.println("[MySQL] (database: " + db + ") Reconectado ao MySQL.");
            else System.out.println("[MySQL] (database: " + db + ") Não foi possível se reconectar ao MySQL.");

            return connected;
        }
        return true;

    }

    public boolean openConnection() {
        try {
            if (c != null && !c.isClosed()) {
                return true;
            }
        } catch (Exception ignored) {
        }

        String connectionURL = "jdbc:mysql://" + this.host + ":" + this.port;

        if (db != null) connectionURL = connectionURL + "/" + this.db + "?autoReconnect=true";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            c = DriverManager.getConnection(connectionURL, this.user, this.pass);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }

        try {
            ps = getConnection().createStatement();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return true;
    }

    public Connection getConnection() {
        return c;
    }

    public CompletableFuture<ResultSet> queryAsync(String query) {
        return CompletableFuture.supplyAsync(() -> query(query));
    }

    public CompletableFuture<ResultSet> queryStatementAsync(String query) {
        return CompletableFuture.supplyAsync(() -> queryStatement(query));
    }

    public ResultSet query(String query) {
        ensureIsConnected();

        try {
            if (ps.isClosed()) ps = getConnection().createStatement();
        } catch (Exception ignored) {
        }

        try {
            String[] update = {"update", "insert", "create", "delete"};
            boolean doUpdate = false;

            for (String check : update) {
                if (query.toLowerCase().startsWith(check)) {
                    doUpdate = true;
                    break;
                }
            }

            if (ps.isClosed()) return null;

            if (doUpdate) {
                ps.executeUpdate(query);

                return null;
            } else return ps.executeQuery(query);

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public ResultSet queryStatement(String query) {
        ensureIsConnected();

        try {
            if (ps.isClosed()) ps = getConnection().createStatement();
        } catch (Exception ignored) {
        }

        try {
            String[] update = {"update", "insert", "create", "delete"};
            boolean doUpdate = false;
            for (String check : update) {
                if (query.toLowerCase().startsWith(check)) {
                    doUpdate = true;
                    break;
                }
            }

            PreparedStatement preparedStatement = preparedStatement(query);

            if (doUpdate) {
                preparedStatement.executeUpdate();
                preparedStatement.close();

                return null;
            } else return preparedStatement.executeQuery();

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void updateAsync(String query) {
        ensureIsConnected();

        try {
            if (ps.isClosed()) ps = getConnection().createStatement();
        } catch (Exception ignored) {
        }

        CompletableFuture.supplyAsync(() -> query(query));
    }

    public Statement getStatement() {
        ensureIsConnected();
        return ps;
    }

    public PreparedStatement preparedStatement(String query) {
        ensureIsConnected();

        try {
            if (ps.isClosed()) ps = getConnection().createStatement();
        } catch (Exception ignored) {
        }

        try {
            return getConnection().prepareStatement(query);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void close(PreparedStatement ps) {
        try {
            ps.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (c != null && c.isClosed()) return;
        } catch (SQLException ignored) {
        }

        try {
            c.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
