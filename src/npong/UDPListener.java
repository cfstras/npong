package npong;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPListener extends Thread {

    static float gravity[] = new float[3];
    InetAddress toBroadcast;
    static DatagramSocket s;
    boolean player2;
    Pong pong;
    

    public UDPListener(boolean player2, Pong pong) {
        this.player2 = player2;
        this.pong=pong;
        try {
            s = new DatagramSocket(45454);
            //s=new DatagramSocket(new InetSocketAddress(null, 45454));
            System.out.println(s.getBroadcast());
            s.setBroadcast(true);
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (Main.run) {
            try {
                byte[] buf = new byte[200];
                DatagramPacket p = new DatagramPacket(buf, 200);
                s.receive(p);
                dataGot(buf);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void dataGot(byte[] buf) {


        String xstr, ystr, zstr;
        xstr = String.valueOf(gravity[0]);
        ystr = String.valueOf(gravity[1]);
        zstr = String.valueOf(gravity[2]);

        float x = (float) (toInt(buf, 0)) / 1000;
        float y = (float) (toInt(buf, 4)) / 1000;
        float z = (float) (toInt(buf, 8)) / 1000;
        System.out.println("Got data: x=" + x + "  y=" + y + "  z=" + z);
        
        int newpos= (int)Math.round((-y/7)*pong.height/2+ (pong.height/2) );
        System.out.println("pos: "+newpos);
        if (!player2)
            pong.newp1pos=newpos;
        else
            pong.newp2pos=newpos;
        
    }

    public static byte[] toByte(int data, byte[] arr, int offset) {

        arr[offset++] = (byte) ((data >> 24) & 0xff);
        arr[offset++] = (byte) ((data >> 16) & 0xff);
        arr[offset++] = (byte) ((data >> 8) & 0xff);
        arr[offset++] = (byte) ((data >> 0) & 0xff);
        return arr;
    }

    public static int toInt(byte[] data, int offset) {
        if (data == null || data.length - offset <= 4) {
            return 0x0;
        }
        // ----------
        return (int) ( // NOTE: type cast not necessary for int
                (0xff & data[offset++]) << 24
                | (0xff & data[offset++]) << 16
                | (0xff & data[offset++]) << 8
                | (0xff & data[offset++]) << 0);
    }
}
