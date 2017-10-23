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
        while (cerrarThread) {
            try {
                //ArrayList<Titan> Titanes= new ArrayList<Titan>();
                //Crear Datagrama UDP
                byte[] buf = new byte[1000];
                packet = new DatagramPacket(buf,buf.length);
                //System.out.println("McastReceiver: waiting for packet");

                //Recibir paquete
                mSocket.receive(packet);

                //Crear el inputstream con los datos de llegada
                ByteArrayInputStream bistream =
                        new ByteArrayInputStream(packet.getData());
                DataInput Di = new DataInputStream(bistream);

                //Primer input me dice cuantos titanes a listar
                int iterator = Di.readInt();

                //SI RECIBE -300, SIGNIFICA QUE ES UN DATAGRAMA SOLO CON 1 TITAN QUE ES NUEVO,
                //POR LO QUE VUELVO EL ITERADOR A 1 PARA QUE LO IMPRIMA Y AGREGO MENSAJE DE NUEVO
                if(iterator == -300) {
                    System.out.println("\nNUEVO TITAN");
                    iterator = 1;
                } else if (iterator == 0){
                    System.out.println("No hay titanes en el Distrito");
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
                //El programa solo recibia 1
                /*ObjectInputStream ois = new ObjectInputStream(bistream);
                Titanes = (ArrayList<Titan>) ois.readObject();

                //Ignorar paquetes que salgan de mi
                if (!(packet.getAddress().equals(this.localHost))) {
                    if(Titanes.size() != 0) {
                        System.out.println("[Cliente:] Titanes del Distrito");
                        Titan[] TempArray = (Titan[]) Titanes.toArray(new Titan[0]);
                        for (Titan titan : TempArray) {
                            System.out.println("****************");
                            System.out.println("TITAN");
                            System.out.println("Nombre: "+titan.Name);
                            System.out.println("Id: "+titan.Id);
                            System.out.println("****************");

                        }
                    } else {
                        System.out.println("[Cliente:] No hay titanes ");

                    }*/


                //ois.close();
                bistream.close();




            } catch(IOException ioe) {
                System.out.println("Trouble reading multicast message");
                ioe.printStackTrace();  System.exit(1);
            } /*catch (ClassNotFoundException cnfe) {
                System.out.println("Class missing while reading mcast packet");
                cnfe.printStackTrace(); System.exit(1);
            }*/

        }
        mSocket.close();

    }
    public void terminar(){
        this.cerrarThread = false;
    }


}
