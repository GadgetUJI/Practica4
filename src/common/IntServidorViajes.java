package common;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.rmi.RemoteException;

public interface IntServidorViajes {
    public void guardaDatos() throws RemoteException;
    public JSONArray consultaViajes(String origen) throws RemoteException;
    public JSONObject reservaViaje(String codviaje, String codcli) throws RemoteException;
    public JSONObject anulaReserva(String codviaje, String codcli) throws RemoteException;
    public JSONObject ofertaViaje(String codcli, String origen, String destino, String fecha, long precio, long numplazas) throws RemoteException;
    public JSONObject borraViaje(String codviaje, String codcli) throws RemoteException;
}
