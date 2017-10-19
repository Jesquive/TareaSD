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
        try {
            byte[ ] buffer = new byte[10];
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            DataInput Di = new DataInputStream(bais);
            scanner = new Scanner(System.in);

            //Llegada de paquete udp con la info de conexion [DEPRECATED]
           /* DatagramSocket  mySocket = new DatagramSocket(8001);
            byte[ ] buffer = new byte[10];
            DatagramPacket datagram = new DatagramPacket(buffer, 10);
            mySocket.receive(datagram);
            String message = new String(buffer);
            mySocket.close( );*/

            //Llegada de paquete udp con la info de conexion
            DatagramSocket  mySocket = new DatagramSocket(8001);
            DatagramPacket datagram = new DatagramPacket(buffer, 10);
            mySocket.receive(datagram);

            String message = Di.readUTF();

            System.out.println("[Servidor Central] Dar autorizacion a " +
                    datagram.getAddress() +" para el Distrito "+ message
                    + "\n 1.-SI \n 2.-NO ");
            String input = scanner.nextLine();
            //Si es aceptado, entregarle los datos del multicast y el ip-consultas
            if (Objects.equals("1", input)){

            }
            mySocket.close( );

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Envia Informacion de conexion a un cliente
    public static void InformationSender(String Server,String ipMC, int PuertoMC,String ipPET,int PuertoPET){

    }

}
