import java.net.*;
import java.io.*;
import java.util.*;

public class McastReceiver implements Runnable {

    int mcastPort = 0;
    InetAddress mcastAddr = null;
    InetAddress localHost = null;
    Boolean cerrarThread = true;
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

        //Instancia el multicast
        try {

            //System.out.println("Setting up multicast receiver");
            mSocket = new MulticastSocket(this.mcastPort);
            System.out.println(this.mcastAddr);
            mSocket.joinGroup(this.mcastAddr);

        } catch(IOException ioe) {
            System.out.println("[Cliente: ] Trouble opening multicast port");
            ioe.printStackTrace();      System.exit(1);

        }

        DatagramPacket packet;
        System.out.println("[Cliente: ] Conectado al Distrito");

        //Esperar a que llegue algo
        while (cerrarThread) {
            try {
                byte[] buf = new byte[1000];
                packet = new DatagramPacket(buf,buf.length);
                //System.out.println("McastReceiver: waiting for packet");
                mSocket.receive(packet);

                ByteArrayInputStream bistream = new ByteArrayInputStream(packet.getData());
                DataInput Di = new DataInputStream(bistream);

                //Primer input me dice cuantos titanes a listar
                int iterator = Di.readInt();

                //SI RECIBE -300, es un datagrama de que posee solo un titan y es uno nuevo.
                //Se imprime mensaje indicando su estado y se devuelve el iterador a 1 para imprimirlo
                if(iterator == -300) {
                    System.out.println("\n[Cliente: ] NUEVO TITAN ATACA");
                    iterator = 1;
                } else if (iterator == 0){
                    System.out.println("[Cliente: ] No hay titanes en el Distrito");
                }

                //IMPRIME TODOS LOS TITANES EN DATAGRAMA
                for (int i = 0; i < iterator; i++) {
                    System.out.println("****************");
                    System.out.println("Id: "+Di.readInt());
                    System.out.println("Nombre: "+Di.readUTF());
                    System.out.println("Distrito: "+Di.readUTF());
                    System.out.println("Tipo: "+Di.readUTF());
                    System.out.println("****************");
                }
                bistream.close();




            } catch(IOException ioe) {
                System.out.println("[Cliente: ] 9Trouble reading multicast message");
                ioe.printStackTrace();  System.exit(1);
            }

        }
        mSocket.close();

    }
    //Funcion que cierra el thread
    public void terminar(){
        this.cerrarThread = false;
    }


}
