import java.io.*;
import java.net.*;
import java.util.*;

public class DistrictServer {
  //Variable paa recibir input, los distritos instanciados y la idglobal de los titanes creados
  private static Scanner scanner;
  static ArrayList<District> Distritos = new ArrayList<District>();
  public static int IdLastCreatedTitan = 1;

  //Variables que me dicen si estoy en el menu de creacion de titanes de un distrito y de que distrito
  public static boolean InDistrict;
  public static District ActualDistrict;

  public static void main(String[] args) {
    boolean Ciclo = true;
    InDistrict = false;
    scanner = new Scanner(System.in);

    while (Ciclo){

        //Si no estoy en el menu interno de un distrito, tengo el menu de creacion
        //de distritos con las opciones de listar, acceder a uno (mostar menu de creacion de titanes)
        // o crear uno con todos los datos pertinentes.
      if(!InDistrict) {
        System.out.println("[Distritos] Inserte comando, para ayuda ingrese HELP: ");
        String temp = scanner.nextLine();

        if (Objects.equals("HELP", temp)) {
          System.out.println("[Distritos] Los comandos existentes son: ");
          System.out.println("1 -- Listar Distritos");
          System.out.println("2 -- Acceder a Distrito");
          System.out.println("3 -- Crear Distrito \n");
        } else if (Objects.equals("3", temp)) {
          InstantiateDistrict();
        } else if (Objects.equals("1", temp)) {
          ListDistricts();
        } else if(Objects.equals("2", temp)){
          EnterDistrict();
        }
      } else {
          //Menu interno para la creacion de titanes, puede listar los titanes actuales o crear nuevos. Puede
          //devolverse al menu anterion.
        System.out.println("[Distrito: " + ActualDistrict.Name+" ] Inserte comando, para ayuda ingrese HELP");
        String temp = scanner.nextLine();
        if (Objects.equals("HELP", temp)){
          System.out.println("[Distrito: " + ActualDistrict.Name+" ] Los comandos existentes en el distrito son: ");
          System.out.println("1 -- Listar Titanes");
          System.out.println("2 -- Crear Titan");
          System.out.println("3 -- Volver \n");
        }else if (Objects.equals("1", temp)) {
          ActualDistrict.ShowTitans();

        } else if(Objects.equals("2", temp)){
          System.out.println("[Distrito: " + ActualDistrict.Name+" ] Nombre del Titan: ");
          String TitanName = scanner.nextLine();

          System.out.println("[Distrito: " + ActualDistrict.Name+" ] Tipo del Titan (Poner numero): ");
          System.out.println("1 -- Normal");
          System.out.println("2 -- Excentrico");
          System.out.println("3 -- Cambiante \n");
          String temp2 = scanner.nextLine();
          int election = Integer.parseInt(temp2);
          String TitanType;
          switch (election){
            case 1: TitanType = "Normal"; break;
            case 2: TitanType = "Excentrico"; break;
            case 3: TitanType = "Cambiante"; break;
            default: TitanType = "Normal"; break;
          }

          ActualDistrict.NewTitan(IdLastCreatedTitan,TitanName,TitanType);
          IdLastCreatedTitan +=1;
        } else if(Objects.equals("3", temp)){
          InDistrict = false;
        }
      }

    }
  }


    //Al instanciar un nuevo distrito, se reciben los datos
    //para crear el multicast y el servidor de peticiones, y
    //se agrega a la lista de instanciados. Tambien se le envia
    //un datagrama con estos datos al servidor central para que los
    //Agrege a su registro automaticamente.
  public static void InstantiateDistrict(){
    scanner = new Scanner(System.in);
    try {
      DatagramSocket s = new DatagramSocket();

      ByteArrayOutputStream pet = new ByteArrayOutputStream(1000);
      DataOutput Dod = new DataOutputStream(pet);

      System.out.println("[Distritos] Nombre Servidor: ");
      String DistrictName = scanner.nextLine();
      Dod.writeUTF(DistrictName);

      System.out.println("[Distritos] Ip Multicast: ");
      String temp = scanner.nextLine();
      Dod.writeUTF(temp);
      InetAddress IpMC = InetAddress.getByName(temp);

      System.out.println("[Distritos] Puerto Multicast: ");
      temp = scanner.nextLine();
      Dod.writeUTF(temp);
      int MCport = Integer.parseInt(temp);

      System.out.println("[Distritos] Ip Peticiones: ");
      temp = scanner.nextLine();
      Dod.writeUTF(temp);
      InetAddress IpPetition = InetAddress.getByName(temp);

      System.out.println("[Distritos] Puerto Peticiones: ");
      temp = scanner.nextLine();
      Dod.writeUTF(temp);
      int PetitionPort = Integer.parseInt(temp);

      District Distrito = new District(DistrictName,IpMC,MCport,IpPetition,PetitionPort);
      Distrito.CreateMulticast();

      DatagramPacket EnviarDistrito = new DatagramPacket(pet.toByteArray(), pet.size(), InetAddress.getByName("10.10.2.107"),5005);
      s.send(EnviarDistrito);

      s.close();
      Distritos.add(Distrito);

    }  catch (Exception ex) {
      ex.printStackTrace( );
    }
  }

  //Listar los distritos instanciados
  public static void ListDistricts(){
    if(Distritos.size() != 0) {
      District[] TempArray = (District[]) Distritos.toArray(new District[0]);
      for (District distrito : TempArray) {
        System.out.println(distrito.Name);
      }
    } else {
      System.out.println("[Distritos] No hay Distritos");
    }
  }

    //Revisa si el distrito al que quieres entrar existem si es asi entra en el
  public static void EnterDistrict(){
    scanner = new Scanner(System.in);

    System.out.println("[Distritos] Ingrese Nombre de Servidor para entrar: ");
    String DistrictName = scanner.nextLine();

    if(Distritos.size() != 0) {
      District[] TempArray = (District[]) Distritos.toArray(new District[0]);
      for (District distrito : TempArray) {
        if (Objects.equals(distrito.Name, DistrictName)){
          InDistrict = true;
          ActualDistrict = distrito;
        }
      }
    } else {
      System.out.println("[Distritos] No hay Distritos");
    }


  }

}