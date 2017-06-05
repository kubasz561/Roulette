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
    ServerSenderThread(ObjectOutputStream serverToClient)
    {
        this.serverToClient = serverToClient;
        this.outgoingQueue = new LinkedBlockingQueue<>();
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
            closeOutGoingStream();
        }
    }

    private void closeOutGoingStream() {
        try
        {
            serverToClient.close();
            //zamknac tez nasluchujacy
            //najpierw wyrejestrowac potem zamknac kolejke
            // potem close na socket
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Couldn't close output stream");
        }
    }
}
