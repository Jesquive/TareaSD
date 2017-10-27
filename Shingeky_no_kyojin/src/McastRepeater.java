import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class McastRepeater implements Runnable {

    private DatagramSocket dgramSocket = null;
    int mcastPort = 0;
    InetAddress mcastAddr = null;
    InetAddress localHost = null;
    ArrayList<Titan> TitanesS;


    //Constructor
    public McastRepeater (int port, InetAddress addr, ArrayList<Titan> Titanes) {

        this.mcastPort = port;
        this.mcastAddr = addr;
        this.TitanesS = Titanes;
        try {
            this.dgramSocket = new DatagramSocket();

        } catch (IOException ioe) {
            System.out.println("[Distrito: ] problems creating the datagram socket.");
            ioe.printStackTrace();
            System.exit(1);
        }

        try {
            this.localHost = InetAddress.getLocalHost();

        } catch (UnknownHostException uhe) {
            System.out.println("[Distrito: ] Problems identifying local host");
            uhe.printStackTrace();
            System.exit(1);
        }
    }

    //Mantiene la lista actualizada
    public void SetTitanList(ArrayList<Titan> Titanes){
        this.TitanesS = Titanes;
    }

    //Devuelve el socket del multicas para uso en otras clases
    public DatagramSocket GetMCsocket(){
        return dgramSocket;
    }

    @Override
    public void run() {
        //Paquete a enviar
        DatagramPacket packet = null;

        while (true) {
            //Se hace cada 60 segundos
            try {

                ByteArrayOutputStream baot = new ByteArrayOutputStream();
                DataOutput Do = new DataOutputStream(baot);

                //Enviar cuantos titanes se escrbira
                int iterator = this.TitanesS.size();
                Do.writeInt(iterator);

                //Serializa la cantidad de titanes
                if(this.TitanesS.size() != 0) {
                    Titan[] TempArray = (Titan[]) this.TitanesS.toArray(new Titan[0]);
                    for (Titan titan : TempArray) {
                        Do.writeInt(titan.Id);
                        Do.writeUTF(titan.Name);
                        Do.writeUTF(titan.District);
                        Do.writeUTF(titan.Tipo);
                    }
                }

                packet = new DatagramPacket(baot.toByteArray(),
                        baot.size(),
                        mcastAddr,
                        mcastPort);

                // Envia el paquete
                dgramSocket.send(packet);
               // System.out.println("[Distrito: ] Enviando Actualizacion de titanes");
                Thread.sleep(60000);

            } catch(InterruptedException ie) {
                ie.printStackTrace();

            } catch (IOException ioe) {
                System.out.println("[Distrito: ] error sending multicast");
                ioe.printStackTrace(); System.exit(1);

            }
        }
    }
}
