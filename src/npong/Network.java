/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package npong;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author claus
 */
public class Network {

    public static void main(String[] args){
        new Network();
    }


    HashSet<InetAddress> servers=new HashSet<InetAddress>();
    Broadcaster bc;
    public Network(){
        bc=new Broadcaster();
        bc.start();

        //bc.isActive=false;
    }


    //use same UDP sockets for connection
    //listen
    //when connected, start sending pong variables
    //wait for other player to load first



    class Broadcaster extends Thread{
    byte[] code="cfs.q1cc.net npong v1".getBytes();
    int packetLength=code.length;
    DatagramSocket udpSocket;

    boolean isActive=true;

    int firstPort=48535;
    int lastport=48545;
    
    public Broadcaster() {
        boolean success=false;
        int port=firstPort;
        while (!success && port<=lastport){
            try {
                udpSocket = new DatagramSocket(port);
                udpSocket.setBroadcast(true);
                
                success=true;
                return;
            } catch (SocketException ex) {
                ex.printStackTrace();
                port++;
            }
        }
        JOptionPane.showConfirmDialog(null, "Error: could not bind to any of the port(s) "+firstPort+"-"+lastport+", can't connect that way. Committing suicide...");

    }


    @Override public void run() {
        startBroadcasting();
        //receiveLoop
        while(isActive) {
            try {
                DatagramPacket p = new DatagramPacket(new byte[packetLength],packetLength);
                udpSocket.receive(p);
                System.out.println("Got broadcast from "+p.getAddress()+" string: "+new String(p.getData()));
                if(Arrays.equals(code,p.getData())) {
                    if( Network.this.servers.add(p.getAddress())){
                        System.out.println("Added "+p.getAddress()+" to server list.");
                    }
                    //HashSets ignore duplicates.
                } else {
                    //I don't want this packet! Go away!
                }
            } catch (IOException ex) {}
        }

    }

    void startBroadcasting(){
        new Thread(){
                @Override
            public void run() {
                doBroadcasting();
            }
        }.start();
    }

    private void doBroadcasting() {
        //get List of Interfaces
        ArrayList<InetAddress> toBeScannedIPs=new ArrayList<InetAddress>();
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            NetworkInterface ni;
            while (nis.hasMoreElements()) {
                ni = nis.nextElement();
                if (!ni.isLoopback()) {
                    List<InterfaceAddress> addresses = ni.getInterfaceAddresses();
                    //get Broadcast addresses
                    for (InterfaceAddress ia : addresses) {
                        InetAddress a = ia.getBroadcast();
                        if (a != null) {
                            toBeScannedIPs.add(a);
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
            try {
                toBeScannedIPs.add(InetAddress.getByName("127.0.0.1")); // i wanna find myself because i have no friends.
            } catch (UnknownHostException ex) {}

        while (isActive){
            for(InetAddress ad:toBeScannedIPs) {
                broadcastTo(ad);
            }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {}
        }
    }

    void broadcastTo(InetAddress ip) {
        for(int p=firstPort;p<=lastport;p++){ //broadcast to all ports
            try {
                DatagramPacket pak = new DatagramPacket(code, packetLength, ip, p);
                udpSocket.send(pak);
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
        }
        System.out.println("sent broadcasts to "+ip);
    }
}
}
