package communication;

import com.kubasz561.roulette.common.JSONMessage;
import game_logic.ServerOverseer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by sackhorn on 29.05.17.
 */
public class ServerSenderThread extends Thread {
    public ObjectOutputStream serverToClient;
    public BlockingQueue<JSONMessage> outgoingQueue;
    private CommunicationManagerServer communicationManagerServer;

    public ServerSenderThread(ObjectOutputStream serverToClient, CommunicationManagerServer communicationManagerServer)
    {
        this.serverToClient = serverToClient;
        this.outgoingQueue = new LinkedBlockingQueue<>();
        this.communicationManagerServer = communicationManagerServer;
    }

    @Override
    public void run()
    {
        try
        {
            JSONMessage msg;
            while(ServerOverseer.getInstance().isRunning)
            {
                msg = outgoingQueue.take();
                serverToClient.writeObject(msg);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("Closing sender thread");
            communicationManagerServer.closeAll();
        }
    }

}
