import javax.management.StringValueExp;
import java.io.*;
import java.net.*;
import java.util.*;
public class CentralServer {

    //Arreglos que con tendran los servers
    //y los clientes actuales
    List serversNames;
    List currentClients;
    private static Scanner scanner;
    public static void main(String[] args) {
            CentralServerGet();
    }
    public static void CentralServerGet(){
        List<List<String>> Conectados = new ArrayList<List<String>>();
        List<List<String>> Distritos = new ArrayList<List<String>>();


        try {

            //Llegada de paquete udp con la info de conexion

            Distritos.add(new ArrayList<String>());
            Distritos.get(Distritos.size()-1).add("Frost");
            Distritos.get(Distritos.size()-1).add("224.0.0.7");
            Distritos.get(Distritos.size()-1).add("5400");
            Distritos.get(Distritos.size()-1).add("10.10.2.135");
            Distritos.get(Distritos.size()-1).add("5002");
            Distritos.add(new ArrayList<String>());
            Distritos.get(Distritos.size()-1).add("Hot");
            Distritos.get(Distritos.size()-1).add("224.0.0.8");
            Distritos.get(Distritos.size()-1).add("5401");
            Distritos.get(Distritos.size()-1).add("10.10.2.135");
            Distritos.get(Distritos.size()-1).add("5700");
            while(true) {
                for(int i = 0; i<Conectados.size();i++){
                    System.out.println("IP: "+ Conectados.get(i).get(0)+" en distrito: "+ Conectados.get(i).get(1));
                }

                DatagramSocket mySocket = new DatagramSocket(5004);
                byte[] buffer = new byte[10];
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                DataInput Di = new DataInputStream(bais);
                scanner = new Scanner(System.in);

                DatagramPacket datagram = new DatagramPacket(buffer, 10);
                mySocket.receive(datagram);

                String message = Di.readUTF();

                System.out.println("[Servidor Central] Dar autorizacion a " +
                        datagram.getAddress() + " para el Distrito " + message
                        + "\n 1.-SI \n 2.-NO ");
                String input = scanner.nextLine();
                //Si es aceptado, entregarle los datos del multicast y el ip-consultas
                if ("1".equals(input)) {
                    Conectados.add(new ArrayList<String>());
                    Conectados.get(Conectados.size() - 1).add(datagram.getAddress().toString());
                    Conectados.get(Conectados.size() - 1).add(message);
                    for (int i = 0; i < Distritos.size(); i++) {
                        if (Objects.equals(message, Distritos.get(i).get(0))) {
                            ByteArrayOutputStream baot = new ByteArrayOutputStream(1000);
                            DataOutput Do = new DataOutputStream(baot);
                            Do.writeUTF(Distritos.get(i).get(0));
                            Do.writeUTF(Distritos.get(i).get(1));
                            Do.writeUTF(Distritos.get(i).get(2));
                            Do.writeUTF(Distritos.get(i).get(3));
                            Do.writeUTF(Distritos.get(i).get(4));


                            DatagramPacket respuesta = new DatagramPacket(baot.toByteArray(), baot.size(), datagram.getAddress(), datagram.getPort());
                            // Enviamos la respuesta, que es un eco
                            mySocket.send(respuesta);


                        }
                    }

                }else{
                    ByteArrayOutputStream baot = new ByteArrayOutputStream(1000);
                    DataOutput Do = new DataOutputStream(baot);
                    Do.writeUTF("Rechazado");

                    DatagramPacket respuesta = new DatagramPacket(baot.toByteArray(), baot.size(), datagram.getAddress(), datagram.getPort());
                    // Enviamos la respuesta, que es un eco
                    mySocket.send(respuesta);
                }
                mySocket.close();
            }


        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Envia Informacion de conexion a un cliente
    public static void InformationSender(String Server,String ipMC, int PuertoMC,String ipPET,int PuertoPET){

    }

}
