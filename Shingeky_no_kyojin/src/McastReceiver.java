import java.net.*;
import java.io.*;

public class McastReceiver implements Runnable {

    int mcastPort = 0;
    InetAddress mcastAddr = null;
    InetAddress localHost = null;

    //Asigna las variables necesarias para el MC
    public  McastReceiver(int port,InetAddress addr){
        this.mcastPort = port;
        this.mcastAddr = addr;

        try{
            this.localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException uhe){
            System.out.println("Problems identifying local host");
            uhe.printStackTrace();  System.exit(1);
        }

    }

    @Override
    public void run() {
        MulticastSocket mSocket = null;

        //INSTANCIAR EL RECIBIDOR MULTICAST
        try {

            System.out.println("Setting up multicast receiver");
            mSocket = new MulticastSocket(this.mcastPort);
            System.out.println(this.mcastAddr);
            mSocket.joinGroup(this.mcastAddr);

        } catch(IOException ioe) {
            System.out.println("Trouble opening multicast port");
            ioe.printStackTrace();      System.exit(1);

        }

        DatagramPacket packet;
        System.out.println("Multicast receiver set up ");

        //Esperar a que llegue algo
        while (true) {
            try {

                //Crear Datagrama UDP
                byte[] buf = new byte[1000];
                packet = new DatagramPacket(buf,buf.length);
                //System.out.println("McastReceiver: waiting for packet");

                //Recibir paquete
                mSocket.receive(packet);

                //Crear el inputstream con los datos de llegada
                ByteArrayInputStream bistream =
                        new ByteArrayInputStream(packet.getData());

                //El programa solo recibia 1
                ObjectInputStream ois = new ObjectInputStream(bistream);
                Integer value = (Integer) ois.readObject();

                //Ignorar paquetes que salgan de mi
                if (!(packet.getAddress().equals(this.localHost))) {
                    System.out.println("Received multicast packet: "+
                            value.intValue() + " from: " + packet.getAddress());
                }

                ois.close();
                bistream.close();

            } catch(IOException ioe) {
                System.out.println("Trouble reading multicast message");
                ioe.printStackTrace();  System.exit(1);
            } catch (ClassNotFoundException cnfe) {
                System.out.println("Class missing while reading mcast packet");
                cnfe.printStackTrace(); System.exit(1);
            }

        }

    }
}
