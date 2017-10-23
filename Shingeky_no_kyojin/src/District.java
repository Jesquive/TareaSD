
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.*;
import java.net.*;
import java.util.*;


public class District {
  //Datos necesarios para un distrito
  String Name;
  InetAddress IpMC;
  int PMC;
  InetAddress IpPeticiones;
  int PPeticiones;

  //
  ArrayList<Titan> Titanes;
  McastRepeater ActualizadorTitanes;
  SDReceivers actualizadorTITANES;

  //Constructor de Distritos entregando cada dato
  public District(String Name, InetAddress IpMC, int PMC, InetAddress IpPeticiones, int PPeticiones){
    this.Name = Name;
    this.IpMC = IpMC;
    this.PMC = PMC;
    this. IpPeticiones = IpPeticiones;
    this.PPeticiones = PPeticiones;
    this.Titanes = new ArrayList<Titan>();
    this.ActualizadorTitanes = new McastRepeater(this.PMC,this.IpMC,this.Titanes);
  }

  //Crear un multicast para este distrito
  public void CreateMulticast(){
      try{
          //Crear el servidor MC
          InetAddress grupo = this.IpMC;
          MulticastSocket socket = new MulticastSocket(this.PMC);
          socket.joinGroup(grupo);


      }catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
      }catch (IOException e){
            System.out.println("IO: " + e.getMessage());
      }

      //Crear thread que enviara actualizaciones cada 1 minuto de los titanes del distrito
      new Thread(this.ActualizadorTitanes,"McastRepeater: Distrito"+this.Name).start();
      PetitionServer();
  }

  //Crear Servidor de peticiones para este distrito
  public void PetitionServer(){
      try {
          DatagramSocket mySocket = new DatagramSocket(this.PPeticiones);
          SDReceivers th = new SDReceivers(mySocket, Titanes);
          new Thread(th,"SDReceiver: Distrito "+this.Name).start();
      }catch (Exception ex) {
          ex.printStackTrace( );
      }


  }

  //Crear titan en este distrito y agregarlo a la lista de titanes vivos
  public void NewTitan(int IdLastCreatedTitan,String Name,String Tipo){
    Titan Titan = new Titan(IdLastCreatedTitan,Name,this.Name,Tipo);
    Titanes.add(Titan);
    ActualizadorTitanes.SetTitanList(this.Titanes);
    System.out.println("[Distrito: " + this.Name+" ] Se ha publicado el titan: "+Name);
    System.out.println("****************");
    System.out.println("Id: "+ IdLastCreatedTitan);
    System.out.println("Nombre: "+ Name);
    System.out.println("Tipo: "+ Tipo);
    System.out.println("Distrito: "+ this.Name);
    System.out.println("**************** \n");


    //FUNCION en prueba
    SendMCNewTitan(IdLastCreatedTitan,Name,Tipo);
  }
    //REVISAR SI EL TEMPSOCK ENVIA
  public void SendMCNewTitan(int id,String TitanName,String TitanType){

      try {
          DatagramPacket packet;
          DatagramSocket TempSock = ActualizadorTitanes.GetMCsocket();

          ByteArrayOutputStream baot = new ByteArrayOutputStream();
          DataOutput Do = new DataOutputStream(baot);

          Do.writeInt(-300);
          Do.writeInt(id);
          Do.writeUTF(TitanName);
          Do.writeUTF(this.Name);
          Do.writeUTF(TitanType);

          // Create a datagram packet and send it
          packet = new DatagramPacket(baot.toByteArray(),
                  baot.size(),
                  this.IpMC,
                  this.PMC);

          // send the packet
          TempSock.send(packet);

      } catch (IOException ioe) {
          System.out.println("error sending multicast");
          ioe.printStackTrace(); System.exit(1);

      }


  }

  //Mostrar la lista de titanes
  public void ShowTitans(){

    //Imprimir titanes
    if(Titanes.size() != 0) {
      System.out.println("[Distrito: " + this.Name+" ] Titanes en el Distrito: ");
      Titan[] TempArray = (Titan[]) Titanes.toArray(new Titan[0]);
      for (Titan titan : TempArray) {
        System.out.println("***************");
        System.out.println("TITAN");
        System.out.println("Nombre: "+titan.Name);
        System.out.println("Id: "+titan.Id);
        System.out.println("***************");

      }
    } else {
      System.out.println("[Distrito: " + this.Name+" ] No hay titanes ");

    }
  }
}