import java.net.*;
import java.io.*;
import java.util.*;

public class SDReceivers implements Runnable {

    int petPort = 0;
    InetAddress petAddr = null;
    DatagramSocket mSocket;
    Boolean cerrarThread = true;
    ArrayList<Titan> TitanesS;
    //Asigna las variables necesarias para el MC
    public SDReceivers(DatagramSocket s) {
        this.mSocket = s;
    }


    @Override
    public void run() {
        byte[] bufferEnt = new byte[1024];
        byte[] bufferSal = new byte[1024];
        while(true){
            ByteArrayInputStream rec = new ByteArrayInputStream(bufferEnt);
            DataInput Di = new DataInputStream(rec);
            DatagramPacket data = new DatagramPacket(bufferEnt, 1000);
            try {
                mSocket.receive(data);
                int comando = Di.readInt();
                switch (comando) {
                    case 1:
                        ByteArrayOutputStream baot2 = new ByteArrayOutputStream(1000);
                        DataOutput Do2 = new DataOutputStream(baot2);


                        //Enviar cuantos titanes se escrbira
                        int iterator = this.TitanesS.size();
                        Do2.writeInt(iterator);

                        if(this.TitanesS.size() != 0) {
                            Titan[] TempArray = (Titan[]) this.TitanesS.toArray(new Titan[0]);
                            for (Titan titan : TempArray) {
                                Do2.writeInt(titan.Id);
                                Do2.writeUTF(titan.Name);
                                Do2.writeUTF(titan.District);
                                Do2.writeUTF(titan.Tipo);
                            }
                        }
                        DatagramPacket enviar = new DatagramPacket(baot2.toByteArray(), baot2.size(), data.getAddress(), data.getPort());

                        mSocket.send(enviar);
                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }



        }

    }
    public void terminar(){
        this.cerrarThread = false;
    }


}
