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


    //Constructor de Distritos entregando cada dato
    public District(String Name, InetAddress IpMC, int PMC, InetAddress IpPeticiones, int PPeticiones){
        this.Name = Name;
        this.IpMC = IpMC;
        this.PMC = PMC;
        this. IpPeticiones = IpPeticiones;
        this.PPeticiones = PPeticiones;
        this.Titanes = new ArrayList<Titan>();
    }

    //Crear un multicast para este distrito
    public void CreateMulticast(){

    }

    //Crear Servidor de peticiones para este distrito
    public void PetitionServer(){

    }

    //Crear titan en este distrito y agregarlo a la lista de titanes vivos
    public void NewTitan(int IdLastCreatedTitan,String Name,String Tipo){
        Titan Titan = new Titan(IdLastCreatedTitan,Name,this.Name,Tipo);
        Titanes.add(Titan);
        System.out.println("[Distrito: " + this.Name+" ] Se ha publicado el titan: "+Name);
        System.out.println("****************");
        System.out.println("Id: "+ IdLastCreatedTitan);
        System.out.println("Nombre: "+ Name);
        System.out.println("Tipo: "+ Tipo);
        System.out.println("Distrito: "+ this.Name);
        System.out.println("**************** \n");
    }

    //Capturar Titan
    public void CaptureTitan(int TitanId){
        //LLamar funcion que agrega a la lista de capturados local del cliente

        //Quitar de la lista de titanes activos del distrito
        Titan[] TempArray = (Titan[])Titanes.toArray(new Titan[0]);
        for (Titan titan : TempArray) {
            if (titan.Id == TitanId){
                Titanes.remove(titan); //SI ESTO NO FUNCIONA CAMBIARLO POR FORI Y REMOVE POR INDICE
            }
        }
    }

    //Matar titan
    public void KillTitan(int TitanId){
        //LLamar funcion que agrega a la lista de asesinados local del cliente

        //Quitar de la lista de titanes activos del distrito
        Titan[] TempArray = (Titan[])Titanes.toArray(new Titan[0]);
        for (Titan titan : TempArray) {
            if (titan.Id == TitanId){
                Titanes.remove(titan); //SI ESTO NO FUNCIONA CAMBIARLO POR FORI Y REMOVE POR INDICE
            }
        }
    }

    //Mostrar la lista de titanes
    public void ShowTitans(){

        //Imprimir titanes
        if(Titanes.size() != 0) {
            System.out.println("[Distrito: " + this.Name+" ] Titanes en el Distrito: ");
            Titan[] TempArray = (Titan[]) Titanes.toArray(new Titan[0]);
            for (Titan titan : TempArray) {
                System.out.println("****************");
                System.out.println("TITAN");
                System.out.println("Nombre: "+titan.Name);
                System.out.println("Id: "+titan.Id);
                System.out.println("****************");

            }
        } else {
            System.out.println("[Distrito: " + this.Name+" ] No hay titanes ");

        }
    }
}
