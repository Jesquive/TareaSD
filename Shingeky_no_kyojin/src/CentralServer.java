import javax.management.StringValueExp;
import java.io.*;
import java.net.*;
import java.util.*;
public class CentralServer {
    //Variables con el registro de los distritos y para recibir el input.
    public static List<List<String>> Distritos = new ArrayList<List<String>>();
    private static Scanner scanner;

    public static void main(String[] args) {
            CentralServerGet();
    }
    public static void CentralServerGet(){
        List<List<String>> Conectados = new ArrayList<List<String>>();
        try {
            //Se crea el actualizador automatico de distritos
            CSreceiver ActualizadorDistritos = new CSreceiver(new DatagramSocket(5005), Distritos);
            new Thread(ActualizadorDistritos,"CSreceiver").start();

            //Llegada de paquete udp con la info de conexion de un cliente
            while(true) {

                //Imprime los clientes conectados a x distrito y todos los conectados a ese distrito.
                if (Conectados.size() != 0){
                    System.out.println("[Servidor Central: ] Los conectados son: ");
                    for(int i = 0; i<Conectados.size();i++){
                        System.out.println("IP: "+ Conectados.get(i).get(0)+" en distrito: "+ Conectados.get(i).get(1));
                    }
                } else {
                    System.out.println("[Servidor Central: ] No hay usuarios conectados");
                }
                System.out.println("[Servidor Central: ] Los Servidores son: ");
                for(int i = 0; i<Distritos.size();i++){
                    System.out.println("Nombre: "+ Distritos.get(i).get(0));
                }

                DatagramSocket mySocket = new DatagramSocket(5004);
                byte[] buffer = new byte[10];
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                DataInput Di = new DataInputStream(bais);
                scanner = new Scanner(System.in);

                DatagramPacket datagram = new DatagramPacket(buffer, 10);
                mySocket.receive(datagram);

                String message = Di.readUTF();

                //Acepta o rechaza la conexion del cliente
                System.out.println("[Servidor Central: ] Dar autorizacion a " +
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

                }
                //Si es rechazado, le envia el mensaje correspondiente
                else{
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
}
