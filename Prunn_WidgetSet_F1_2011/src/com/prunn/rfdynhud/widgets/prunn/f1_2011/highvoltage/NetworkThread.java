
package com.prunn.rfdynhud.widgets.prunn.f1_2011.highvoltage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

//import net.ctdp.rfdynhud.util.RFDHLog;

/**
 * 
 * 
 * @author Prunn
 */
public class NetworkThread extends Thread
{
    public String get_drs_message = "Znabled:0\0Activated:0\0CanBeActivated:0\0CurrentZone:0\0";
    private int listening_port;
    private DatagramSocket datagramSocket_drs;
    private boolean listenning = true;
    public void StopListenning()
    {
        listenning = false;
    }
    public void Restart()
    {
        start();
    }
    public NetworkThread(int port)
    {
        listening_port = port;
        start();
    }
     
    @SuppressWarnings( "deprecation" )
    @Override
    public void run()
    {
                
        while(listenning)//for(int i=0;i<=50;i++) 
        {
            try
            {
                byte[] buffer = new byte[53];
                datagramSocket_drs = new DatagramSocket(listening_port);
                //datagramSocket_drs.setReuseAddress( true );
                if(datagramSocket_drs != null)
                {
                    while(listenning) 
                    {
                        try
                        {
                            DatagramPacket packet_drs = new DatagramPacket(buffer, buffer.length);
                            datagramSocket_drs.receive(packet_drs);
                            ByteArrayInputStream byteIn_drs = new ByteArrayInputStream (packet_drs.getData (), 0, packet_drs.getLength ());
                            DataInputStream dataIn_drs = new DataInputStream (byteIn_drs);
                            get_drs_message = dataIn_drs.readLine();
                            //RFDHLog.exception(get_drs_message);
                            
                        }
                        catch ( IOException e )
                        {
                            e.printStackTrace();
                        }
                        
                    }
                }
                
            }
            catch ( SocketException e2 )
            {
                e2.printStackTrace();
                //RFDHLog.exception(e2);
                try
                {
                    Thread.sleep(1000);
                }
                catch ( InterruptedException e )
                {
                    e.printStackTrace();
                }
            }
            if(datagramSocket_drs != null)
                datagramSocket_drs.close();
            

        }
    }
    
}
