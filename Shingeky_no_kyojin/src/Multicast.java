import javax.sound.midi.Soundbank;
import java.net.*;
import java.io.*;



public class Multicast {
    public static void main(String args[]){

        try{
            String name = "Yeison";
            String ip = "224.0.0.8";
            InetAddress grupo = InetAddress.getByName(ip);
            MulticastSocket socket = new MulticastSocket(6789);

            socket.joinGroup(grupo);

            byte[] m = name.getBytes();
            DatagramPacket mensajeSalida = new DatagramPacket(m, m.length, grupo, 6789);
            socket.send(mensajeSalida);
            byte[] bufer = new byte[1000];
            String linea;

            while (true){
                DatagramPacket mensajeEntrada = new DatagramPacket(bufer, bufer.length);
                socket.receive(mensajeEntrada);
                linea = new String(mensajeEntrada.getData(), 0, mensajeEntrada.getLength());
                System.out.println("Recibido: " + linea );
                if (linea.equals("Adios")) break;
            }

            socket.leaveGroup(grupo);

        }catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        }
    }
}
