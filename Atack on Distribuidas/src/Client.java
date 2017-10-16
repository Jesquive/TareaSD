import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static Scanner scanner;

    public static void main(String[] args) {
        ConnectionRequest();

    }

    public static void ConnectionRequest(){
        try {
            ByteArrayOutputStream baot = new ByteArrayOutputStream(10);
            DataOutput Do = new DataOutputStream(baot);

            scanner = new Scanner(System.in);

            //Recibir IP, puerto y Nombre de distrito a entrar
            System.out.println("[Cliente] Ingresar IP del Servidor Central");
            String input = scanner.nextLine();
            InetAddress receiverHost = InetAddress.getByName(input);

            System.out.println("[Cliente] Ingresar Puerto del Servidor Central");
            input = scanner.nextLine();
            int receiverPort = Integer.parseInt(input);

            System.out.println("[Cliente] Introducir Nombre de distrito a Investigar");
            input = scanner.nextLine();
            String dist = input;

            Do.writeUTF(dist);
            // Instanciar el datagrama para enviar
            DatagramSocket  mySocket = new DatagramSocket();
            DatagramPacket datagram =
                    new DatagramPacket(baot.toByteArray(), baot.size(), receiverHost, receiverPort);
            mySocket.send(datagram);
            mySocket.close( );


            // Instanciar el datagrama para enviar [Deprecated]
            /*DatagramSocket  mySocket = new DatagramSocket();
            byte[ ] buffer = message.getBytes( );
            DatagramPacket datagram =
                    new DatagramPacket(buffer, buffer.length,
                            receiverHost, receiverPort);
            mySocket.send(datagram);
            mySocket.close( );*/


        } catch (Exception ex) {
            ex.printStackTrace( );
        }
    }

    //Se encarga de recibir la informacion de conexion a los servers y el MC
    public static void InformationGet(){
        try {

        } catch (Exception ex) {
            ex.printStackTrace( );
        }
    }
}
