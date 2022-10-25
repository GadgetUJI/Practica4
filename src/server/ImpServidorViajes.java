package server;

import common.IntServidorViajes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

public class ImpServidorViajes extends UnicastRemoteObject implements IntServidorViajes {
    private GestorViajes gestor;

    public ImpServidorViajes() throws RemoteException{
        super();
        gestor = new GestorViajes();
    }

    @Override
    public void guardaDatos() throws RemoteException{
        gestor.guardaDatos();
    }
    @Override
    public JSONArray consultaViajes(String origen) throws RemoteException{
        return gestor.consultaViajes(origen);
    }
    public JSONObject reservaViaje(String codviaje, String codcli) throws RemoteException{
        return gestor.reservaViaje(codviaje, codcli);
    }
    public JSONObject anulaReserva(String codviaje, String codcli) throws RemoteException{
        return gestor.anulaReserva(codviaje, codcli);
    }
    public JSONObject ofertaViaje(String codcli, String origen, String destino, String fecha, long precio, long numplazas) throws RemoteException{
        return gestor.ofertaViaje(codcli, origen, destino, fecha, precio, numplazas);
    }
    public JSONObject borraViaje(String codviaje, String codcli) throws RemoteException{
        return gestor.borraViaje(codviaje, codcli);
    }

}
