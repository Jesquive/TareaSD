import java.net.*;
import java.io.*;
import java.util.*;


public class SDReceivers implements Runnable {

    int petPort = 0;
    InetAddress petAddr = null;
    DatagramSocket mSocket;
    Boolean cerrarThread = true;
    ArrayList<Titan> TitanesS;

    //Asigna las variables necesarias
    public SDReceivers(DatagramSocket s, ArrayList<Titan> titanes) {

        this.mSocket = s;
        this.TitanesS = titanes;
    }


    @Override
    public void run() {
        //Espera y recibe 1 peticion
        byte[] bufferEnt = new byte[1024];
        while(cerrarThread){
            ByteArrayInputStream rec = new ByteArrayInputStream(bufferEnt);
            DataInput Di = new DataInputStream(rec);
            DatagramPacket data = new DatagramPacket(bufferEnt, 1000);
            try {
                mSocket.receive(data);
                int comando = Di.readInt();
                switch (comando) {
                    //Listar titanes
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
                        //Capturar titan
                        ByteArrayOutputStream pet3 = new ByteArrayOutputStream(1000);
                        DataOutput Do3 = new DataOutputStream(pet3);
                        String nombreTitan = Di.readUTF();
                        if(this.TitanesS.size() != 0) {
                            Titan[] TempArray = (Titan[]) this.TitanesS.toArray(new Titan[0]);
                            for (Titan titan : TempArray) {
                                if(titan.Name.equals(nombreTitan)){
                                    Do3.writeInt(titan.Id);
                                    Do3.writeUTF(titan.Name);
                                    Do3.writeUTF(titan.District);
                                    Do3.writeUTF(titan.Tipo);
                                    CaptureTitan(titan.Id);

                                    break;
                                }

                            }
                        }
                        DatagramPacket enviar3 = new DatagramPacket(pet3.toByteArray(), pet3.size(), data.getAddress(), data.getPort());
                        mSocket.send(enviar3);

                        break;
                    case 4:
                        //Asesinar Titan
                        ByteArrayOutputStream pet4 = new ByteArrayOutputStream(1000);
                        DataOutput Do4 = new DataOutputStream(pet4);
                        String nombreTitan4 = Di.readUTF();
                        if(this.TitanesS.size() != 0) {
                            Titan[] TempArray = (Titan[]) this.TitanesS.toArray(new Titan[0]);
                            for (Titan titan : TempArray) {
                                if(titan.Name.equals(nombreTitan4)){
                                    Do4.writeInt(titan.Id);
                                    Do4.writeUTF(titan.Name);
                                    Do4.writeUTF(titan.District);
                                    Do4.writeUTF(titan.Tipo);
                                    KillTitan(titan.Id);

                                    break;
                                }

                            }
                        }
                        DatagramPacket enviar4 = new DatagramPacket(pet4.toByteArray(), pet4.size(), data.getAddress(), data.getPort());
                        mSocket.send(enviar4);


                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }

    //Termina con el thread
    public void terminar(){
        this.cerrarThread = false;
    }

    //Capturar Titan
    public void CaptureTitan(int TitanId){
        Titan[] TempArray = (Titan[])TitanesS.toArray(new Titan[0]);
        for (Titan titan : TempArray) {
            if (titan.Id == TitanId){
                TitanesS.remove(titan);
            }
        }
    }
    //Matar titan
    public void KillTitan(int TitanId){
        Titan[] TempArray = (Titan[])TitanesS.toArray(new Titan[0]);
        for (Titan titan : TempArray) {
            if (titan.Id == TitanId){
                TitanesS.remove(titan);
            }
        }
    }


}
