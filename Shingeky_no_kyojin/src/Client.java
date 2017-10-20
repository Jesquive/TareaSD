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
            DatagramPacket datagram = new DatagramPacket(baot.toByteArray(), baot.size(), receiverHost, receiverPort);
            mySocket.send(datagram);

            ///Espero respuesta del ServerCentral
            // Construimos el DatagramPacket que contendrá la respuesta

            byte[] buffer2 = new byte[1000];
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer2);
            DataInput Di = new DataInputStream(bais);
            DatagramPacket data = new DatagramPacket(buffer2, 1000);
            mySocket.receive(data);

            String name = Di.readUTF();
            String ipmulti = Di.readUTF();
            String puertomulti = Di.readUTF();
            String ipdistrito = Di.readUTF();
            String puertodistrito = Di.readUTF();

            //System.out.println("El nombre es: " + name+", la ip multicast es: "+ipmulti+", el puerto multicast es: "+puertomulti+", la ip del distrito es: "+ipdistrito+" y el puerto del distrito es: "+ puertodistrito);
            conectarMulticast(name, ipmulti, puertomulti);

            mySocket.close( );


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
    public static void conectarMulticast(String name, String ip, String port){
        try{
            InetAddress grupoM = InetAddress.getByName(ip);
            MulticastSocket socket = new MulticastSocket(Integer.parseInt(port));
            System.out.println("[Cliente]: uniendose a distrito" + name);
            socket.joinGroup(grupoM);
            System.out.println("[Cliente]: Bienvenido a Atack On Distribuidos!!-Distrito: "+ name);

            String names = "Yeison";

            byte[] m = names.getBytes();
            DatagramPacket mensajeSalida = new DatagramPacket(m, m.length, grupoM, Integer.parseInt(port));
            socket.send(mensajeSalida);
            byte[] bufer3 = new byte[1000];
            String linea;


            while (true){
                DatagramPacket mensajeEntrada = new DatagramPacket(bufer3, bufer3.length);
                socket.receive(mensajeEntrada);
                linea = new String(mensajeEntrada.getData(), 0, mensajeEntrada.getLength());
                System.out.println("Recibido: " + linea );
                if (linea.equals("Adios")) break;



            }
            socket.leaveGroup(grupoM);


        }catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        }
    }
}
