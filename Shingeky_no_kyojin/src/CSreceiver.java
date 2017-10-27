import java.net.*;
import java.io.*;
import java.util.*;

public class CSreceiver implements Runnable {

    int petPort = 0;
    InetAddress petAddr = null;

    DatagramSocket mSocket;
    Boolean cerrarThread = true;

    List<List<String>> DistritosExistentes;

    //Asigna las variables necesarias para el MC

    public CSreceiver(DatagramSocket s, List<List<String>> Distritos) {
        this.mSocket = s;
        this.DistritosExistentes = Distritos;
    }


    @Override
    public void run() {
        //Lee la informacion del nuevo distrito y las agrega al registro.
        byte[] bufferEnt = new byte[1024];
        while(true){
            ByteArrayInputStream rec = new ByteArrayInputStream(bufferEnt);
            DataInput Di = new DataInputStream(rec);
            DatagramPacket data = new DatagramPacket(bufferEnt, 1000);
            try {
                //Recibe siempre un distrito en orden
                mSocket.receive(data);
                String Name = Di.readUTF();
                String IpMC = Di.readUTF();
                String PMC = Di.readUTF();
                String IpPet = Di.readUTF();
                String PPeticiones = Di.readUTF();

                DistritosExistentes.add(new ArrayList<String>());
                DistritosExistentes.get(DistritosExistentes.size()-1).add(Name);
                DistritosExistentes.get(DistritosExistentes.size()-1).add(IpMC);
                DistritosExistentes.get(DistritosExistentes.size()-1).add(PMC);
                DistritosExistentes.get(DistritosExistentes.size()-1).add(IpPet);
                DistritosExistentes.get(DistritosExistentes.size()-1).add(PPeticiones);

                System.out.println("[Servidor Central: ] Se agrego un nuevo distrito" +Name);

            } catch (IOException e) {
                e.printStackTrace();
            }



        }

    }
    public void terminar(){
        this.cerrarThread = false;
    }
}