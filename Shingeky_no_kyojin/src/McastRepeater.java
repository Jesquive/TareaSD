import java.net.*;
import java.io.*;

public class McastRepeater implements Runnable {

    private DatagramSocket dgramSocket = null;
    int mcastPort = 0;
    InetAddress mcastAddr = null;
    InetAddress localHost = null;


    public McastRepeater (int port, InetAddress addr) {

        this.mcastPort = port;
        this.mcastAddr = addr;

        try {
            this.dgramSocket = new DatagramSocket();

        } catch (IOException ioe) {
            System.out.println("problems creating the datagram socket.");
            ioe.printStackTrace();
            System.exit(1);
        }

        try {
            this.localHost = InetAddress.getLocalHost();

        } catch (UnknownHostException uhe) {
            System.out.println("Problems identifying local host");
            uhe.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        // send multicast msg once per second
        DatagramPacket packet = null;
        int count = 0;

        while (true) {
            // careate the packet to sned.

            try {
                // serialize the multicast message
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream (bos);
                out.writeObject(new Integer(count++));
                out.flush();
                out.close();

                // Create a datagram packet and send it
                packet = new DatagramPacket(bos.toByteArray(),
                        bos.size(),
                        mcastAddr,
                        mcastPort);

                // send the packet
                dgramSocket.send(packet);
                System.out.println("sending multicast message");
                Thread.sleep(5000);

            } catch(InterruptedException ie) {
                ie.printStackTrace();

            } catch (IOException ioe) {
                System.out.println("error sending multicast");
                ioe.printStackTrace(); System.exit(1);

            }
        }
    }
}
