package hepl.dacsc.ServerGeneriqueTCP;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Logger;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class ThreadServer extends Thread {
    protected int port;
    protected Protocol protocol;
    protected Logger logger;
    protected ServerSocket ssocket;

    public ThreadServer(int port, Protocol protocol, Logger logger) throws IOException
    {
        super("TH Serveur (port=" + port + ",protocole=" + protocol.getNom() + ")");
        this.port = port;
        this.protocol = protocol;
        this.logger = logger;

        ssocket = new ServerSocket(port);
    }
}
