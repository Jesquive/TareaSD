import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static Scanner scanner;
    static ArrayList<Titan> TitanesCap = new ArrayList<Titan>();
    static ArrayList<Titan> TitanesAse = new ArrayList<Titan>();


    public static void main(String[] args) {
        ConnectionRequest();

    }

    public static void ConnectionRequest() {
        try {
            ByteArrayOutputStream baot = new ByteArrayOutputStream(10);
            DataOutput Do = new DataOutputStream(baot);

            scanner = new Scanner(System.in);

            //Recibir IP, puerto y Nombre de distrito a entrar
            System.out.println("[Cliente] Ingresar IP del Servidor Central");
            String input = scanner.nextLine();
            InetAddress receiverHost = InetAddress.getByName(input);

            System.out.println("[Cliente: ] Ingresar Puerto del Servidor Central");
            input = scanner.nextLine();
            int receiverPort = Integer.parseInt(input);

            System.out.println("[Cliente: ] Introducir Nombre de distrito a Investigar");
            input = scanner.nextLine();
            String dist = input;

            Do.writeUTF(dist);
            // Instanciar el datagrama para enviar
            DatagramSocket mySocket = new DatagramSocket();
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
            if(Objects.equals(name, "Rechazado")){
                System.out.println("Su conexion fue rechazada");
                return;
            }
            String ipmulti = Di.readUTF();
            String puertomulti = Di.readUTF();
            String ipdistrito = Di.readUTF();
            String puertodistrito = Di.readUTF();

            //System.out.println("El nombre es: " + name+", la ip multicast es: "+ipmulti+", el puerto multicast es: "+puertomulti+", la ip del distrito es: "+ipdistrito+" y el puerto del distrito es: "+ puertodistrito);
            conectarMulticast(name, ipmulti, puertomulti);
            while(true) {
                System.out.println("[Cliente: ] Introducir Nombre de distrito a Cambiar o escriba 'Salir' para terminar la conexion");
                input = scanner.nextLine();
                if(Objects.equals(input, "Salir")){
                    break;
                }else{
                    ByteArrayOutputStream baot2 = new ByteArrayOutputStream(10);
                    DataOutput Do2 = new DataOutputStream(baot2);
                    String dist2 = input;
                    Do2.writeUTF(dist2);

                    // Instanciar el datagrama para enviar
                    DatagramPacket datagram2 = new DatagramPacket(baot2.toByteArray(), baot2.size(), receiverHost, receiverPort);
                    mySocket.send(datagram2);


                    ///Espero respuesta del ServerCentral
                    // Construimos el DatagramPacket que contendrá la respuesta

                    byte[] buffer3 = new byte[1000];
                    ByteArrayInputStream bais2 = new ByteArrayInputStream(buffer3);
                    DataInput Di2 = new DataInputStream(bais2);
                    DatagramPacket data2 = new DatagramPacket(buffer3, 1000);
                    mySocket.receive(data2);

                    String name2 = Di2.readUTF();
                    if(Objects.equals(name2, "Rechazado")){
                        System.out.println("Su conexion fue rechazada");
                        return;
                    }
                    String ipmulti2 = Di2.readUTF();
                    String puertomulti2 = Di2.readUTF();
                    String ipdistrito2 = Di2.readUTF();
                    String puertodistrito2 = Di2.readUTF();

                    conectarMulticast(name2, ipmulti2, puertomulti2);


                }

            }




            mySocket.close();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void conectarMulticast(String name, String ip, String port) {
        try {
            InetAddress grupoM = InetAddress.getByName(ip);
            MulticastSocket socket = new MulticastSocket(Integer.parseInt(port));
            System.out.println("[Cliente]: uniendose a distrito" + name);
            socket.joinGroup(grupoM);
            System.out.println("[Cliente]: Bienvenido a Atack On Distribuidos!!-Distrito: " + name);

            while (true) {

                System.out.println("[Cliente: ] Consola");
                System.out.println("[Cliente: ] (1) Listar Titanes");
                System.out.println("[Cliente: ] (2) Cambiar Distrito");
                System.out.println("[Cliente: ] (3) Capturar Titan");
                System.out.println("[Cliente: ] (4) Asesinar Titan");
                System.out.println("[Cliente: ] (5) Listar Titanes Capturados");
                System.out.println("[Cliente: ] (6) Listar Titanes Asesinados");
                String temp = scanner.nextLine();


                if (Objects.equals("1", temp)) {

                } else if (Objects.equals("2", temp)) {
                    break;

                } else if (Objects.equals("3", temp)) {

                } else if (Objects.equals("4", temp)) {

                } else if (Objects.equals("5", temp)) {
                    ListarTitanCap();
                } else if (Objects.equals("6", temp)) {
                    ListarTitanAS();
                }

            }
            socket.leaveGroup(grupoM);


        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }

    }

    public static void ListarTitanCap() {
        //Imprimir titanes
        if(TitanesCap.size() != 0) {
            System.out.println("[Cliente:] Titanes Capturados");
            Titan[] TempArray = (Titan[]) TitanesCap.toArray(new Titan[0]);
            for (Titan titan : TempArray) {
                System.out.println("****************");
                System.out.println("TITAN");
                System.out.println("Nombre: "+titan.Name);
                System.out.println("Id: "+titan.Id);
                System.out.println("****************");

            }
        } else {
            System.out.println("[Cliente:] No hay titanes ");

        }

    }

    public static void ListarTitanAS() {
        //Imprimir titanes
        if(TitanesAse.size() != 0) {
            System.out.println("[Cliente:] Titanes Capturados");
            Titan[] TempArray = (Titan[]) TitanesAse.toArray(new Titan[0]);
            for (Titan titan : TempArray) {
                System.out.println("****************");
                System.out.println("TITAN");
                System.out.println("Nombre: "+titan.Name);
                System.out.println("Id: "+titan.Id);
                System.out.println("****************");

            }
        } else {
            System.out.println("[Cliente:] No hay titanes ");

        }

    }
}
