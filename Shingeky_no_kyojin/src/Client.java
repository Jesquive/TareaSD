import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    //Variables para recibir input y listas locales de titanes
    //capturados o asesinados
    private static Scanner scanner;
    static ArrayList<Titan> TitanesCap = new ArrayList<Titan>();
    static ArrayList<Titan> TitanesAse = new ArrayList<Titan>();


    public static void main(String[] args) {
        ConnectionRequest();

    }

    //Recibe el input para conectarse al servidor central, envia consulta
    //Si es aceptado, recibe los datos del distrito en cuestion, si se quiere
    //cambiar de distrito o salirse del programa se le da la opcion.
    public static void ConnectionRequest() {
        try {
            ByteArrayOutputStream baot = new ByteArrayOutputStream(10);
            DataOutput Do = new DataOutputStream(baot);

            scanner = new Scanner(System.in);

            //Recibir IP, puerto y Nombre de distrito a entrar
            System.out.println("[Cliente: ] Ingresar IP del Servidor Central");
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

            conectarMulticast(mySocket, name, ipmulti, puertomulti, ipdistrito, puertodistrito);

            //Si se quiere cambiar de distrito entra en este while
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

                    conectarMulticast(mySocket, name2, ipmulti2, puertomulti2, ipdistrito2, puertodistrito2);


                }

            }
            mySocket.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Se conecta al multicast con los datos obtenidos, queda como un menu para recibir inputs del cliente donde
    //envia peticiones al distrito (1,3,4) , listar los titanes que estan en esta clase (5,6) o cambiar de distrito (2)
    public static void conectarMulticast(DatagramSocket s, String name, String ip, String port, String ip2, String puerto2) {
        try {

            InetAddress grupoM = InetAddress.getByName(ip);
            MulticastSocket socket = new MulticastSocket(Integer.parseInt(port));

            McastReceiver th = new McastReceiver(Integer.parseInt(port),grupoM);
            new Thread(th,"McastReceiver").start();
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

                //Envia consulta y espera la respuesta, el primer elemento
                //de la respuesta es un entero que indica cuantos titanes se tendra que imprimir
                if (Objects.equals("1", temp)) {
                    ByteArrayOutputStream rec2 = new ByteArrayOutputStream(1000);
                    DataOutput Do2 = new DataOutputStream(rec2);

                    Do2.writeInt(1);

                    DatagramPacket enviar = new DatagramPacket(rec2.toByteArray(), rec2.size(), InetAddress.getByName(ip2), Integer.parseInt(puerto2));
                    s.send(enviar);

                    byte[] bufferSal = new byte[1024];
                    ByteArrayInputStream rec3 = new ByteArrayInputStream(bufferSal);
                    DataInput Di3 = new DataInputStream(rec3);
                    DatagramPacket data = new DatagramPacket(bufferSal, 1000);
                    s.receive(data);

                    int iterator = Di3.readInt();
                    //Imprimir titanes
                    if(iterator!= 0) {
                        System.out.println("[Cliente:] Titanes en el Distrito: ");
                        //IMPRIME TODOS LOS TITANES EN DATAGRAMA
                        for (int i = 0; i < iterator; i++) {
                            System.out.println("****************");
                            System.out.println("Id: "+ Di3.readInt());
                            System.out.println("Nombre: "+ Di3.readUTF());
                            System.out.println("Distrito: "+ Di3.readUTF());
                            System.out.println("Tipo: "+ Di3.readUTF());
                            System.out.println("****************");
                        }
                    } else {
                        System.out.println("*************************" +
                                           "*************************" +
                                           "[Cliente:] No hay titanes" +
                                           "*************************" +
                                           "*************************");
                    }

                }
                //Cierra el thread del multicast y vuelve a la funcion anterior para ver si
                //se conecta a otro distrito o se desconecta
                else if (Objects.equals("2", temp)) {
                    th.terminar();
                    break;

                }
                //Tanto 3 y 4, reciben el nombre de un titan en el distrito y lo matan/capturan
                //intentar no crear titanes con el mismo nombre, luego recibe la data entera del titan
                //y lo guarda en su respectiva lista local.
                else if (Objects.equals("3", temp)) {
                    ByteArrayOutputStream pet = new ByteArrayOutputStream(1000);
                    DataOutput Do4 = new DataOutputStream(pet);

                    System.out.println("[Cliente: ] ¿Que titan desea capturar?(Escriba el nombre");
                    String titan = scanner.nextLine();

                    Do4.writeInt(3);
                    Do4.writeUTF(titan);

                    DatagramPacket peticion = new DatagramPacket(pet.toByteArray(), pet.size(), InetAddress.getByName(ip2), Integer.parseInt(puerto2));
                    s.send(peticion);

                    byte[] bufferSal3 = new byte[1024];
                    ByteArrayInputStream reci3 = new ByteArrayInputStream(bufferSal3);
                    DataInput Di3 = new DataInputStream(reci3);
                    DatagramPacket data = new DatagramPacket(bufferSal3, 1000);

                    s.receive(data);

                    int id = Di3.readInt();
                    String nombre = Di3.readUTF();
                    String distrito = Di3.readUTF();
                    String tipo = Di3.readUTF();
                    TitanLocalCap(id, nombre, distrito, tipo);


                } else if (Objects.equals("4", temp)) {
                    ByteArrayOutputStream pet4 = new ByteArrayOutputStream(1000);
                    DataOutput Do5 = new DataOutputStream(pet4);

                    System.out.println("[Cliente: ] ¿Que titan desea matar?(Escriba el nombre");
                    String titan4 = scanner.nextLine();

                    Do5.writeInt(4);
                    Do5.writeUTF(titan4);

                    DatagramPacket peticion4 = new DatagramPacket(pet4.toByteArray(), pet4.size(), InetAddress.getByName(ip2), Integer.parseInt(puerto2));
                    s.send(peticion4);

                    byte[] bufferSal4 = new byte[1024];
                    ByteArrayInputStream reci4 = new ByteArrayInputStream(bufferSal4);
                    DataInput Di4 = new DataInputStream(reci4);
                    DatagramPacket data4 = new DatagramPacket(bufferSal4, 1000);

                    s.receive(data4);

                    int id = Di4.readInt();
                    String nombre = Di4.readUTF();
                    String distrito = Di4.readUTF();
                    String tipo = Di4.readUTF();
                    TitanLocalAS(id, nombre, distrito, tipo);

                }
                //Tanto 5 y 6 solo imprimen lo que hay en sus listas
                else if (Objects.equals("5", temp)) {
                    ListarTitanCap();
                } else if (Objects.equals("6", temp)) {
                    ListarTitanAS();
                }

            }


        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }

    }

    //Imprimir titanes capturados
    public static void ListarTitanCap() {

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

    //Imprimir titanes asesinados
    public static void ListarTitanAS() {
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

    //Agregar a la lista local de capturados.
    public static void TitanLocalCap(int Id,String Name,String distrito, String Tipo) {
        Titan Titan = new Titan(Id, Name, distrito, Tipo);
        TitanesCap.add(Titan);
    }

    //Agregar a la lista local de asesinados.
    public static void TitanLocalAS(int Id,String Name,String distrito, String Tipo) {
        Titan Titan = new Titan(Id, Name, distrito, Tipo);
        TitanesAse.add(Titan);
    }

}
