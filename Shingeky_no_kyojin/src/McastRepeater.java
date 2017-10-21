import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class McastRepeater implements Runnable {

    private DatagramSocket dgramSocket = null;
    int mcastPort = 0;
    InetAddress mcastAddr = null;
    InetAddress localHost = null;
    ArrayList<Titan> TitanesS;



    public McastRepeater (int port, InetAddress addr, ArrayList<Titan> Titanes) {

        this.mcastPort = port;
        this.mcastAddr = addr;
        this.TitanesS = Titanes;
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

    public void SetTitanList(ArrayList<Titan> Titanes){
        this.TitanesS = Titanes;
    }

    public DatagramSocket GetMCsocket(){
        return dgramSocket;
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
                ByteArrayOutputStream baot = new ByteArrayOutputStream();
                DataOutput Do = new DataOutputStream(baot);



                //Enviar cuantos titanes se escrbira
                int iterator = this.TitanesS.size();
                Do.writeInt(iterator);

                if(this.TitanesS.size() != 0) {
                    Titan[] TempArray = (Titan[]) this.TitanesS.toArray(new Titan[0]);
                    for (Titan titan : TempArray) {
                        Do.writeInt(titan.Id);
                        Do.writeUTF(titan.Name);
                        Do.writeUTF(titan.District);
                        Do.writeUTF(titan.Tipo);
                    }
                }

                // Create a datagram packet and send it
                packet = new DatagramPacket(baot.toByteArray(),
                        baot.size(),
                        mcastAddr,
                        mcastPort);

                // send the packet
                dgramSocket.send(packet);
                System.out.println("sending multicast message");
                Thread.sleep(60000);

            } catch(InterruptedException ie) {
                ie.printStackTrace();

            } catch (IOException ioe) {
                System.out.println("error sending multicast");
                ioe.printStackTrace(); System.exit(1);

            }
        }
    }
}
