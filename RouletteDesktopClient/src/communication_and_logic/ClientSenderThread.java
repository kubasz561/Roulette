package communication_and_logic;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.kubasz561.roulette.common.JSONMessage;
import logic.Overseer;


public class ClientSenderThread extends Thread {
    public ObjectOutputStream clientToServer;
    public BlockingQueue<JSONMessage> outgoingQueue;
    public ClientSenderThread(ObjectOutputStream clientToServer)
    {
        this.clientToServer = clientToServer;
        this.outgoingQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run()
    {
        try
        {
            JSONMessage msg;
            while(Overseer.getInstance().isRunning)
            {
                msg = outgoingQueue.take();
                clientToServer.writeObject(msg);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("Closing sender thread");
            Overseer.getInstance().communicationThread.closeAll();

        }
    }
}