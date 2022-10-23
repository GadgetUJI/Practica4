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
    private static FileWriter os;			// stream para escribir los datos en el fichero
    private static FileReader is;			// stream para leer los datos del fichero
    /**
     * 	Diccionario para manejar los datos en memoria.
     * 	La clave es el codigo único del viaje.
     */
    private static HashMap<String, Viaje> mapa;

    /**
     * Constructor del gestor de viajes
     * Crea o Lee un fichero con datos de prueba
     */
    protected ImpServidorViajes() throws RemoteException {
        mapa =  new HashMap<String, Viaje>();
        File file = new File("viajes.json");
        try {
            if (!file.exists() ) {
                // Si no existe el fichero de datos, los genera y escribe
                os = new FileWriter(file);
                generaDatos();
                escribeFichero(os);
                os.close();
            }
            // Si existe el fichero o lo acaba de crear, lo lee y rellena el diccionario con los datos
            is = new FileReader(file);
            leeFichero(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void guardaDatos(){
        File file = new File("viajes.json");
        try {
            os = new FileWriter(file);
            escribeFichero(os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * escribe en el fichero un array JSON con los datos de los viajes guardados en el diccionario
     *
     * @param os	stream de escritura asociado al fichero de datos
     */
    private void escribeFichero(FileWriter os) throws IOException {

        JSONArray datos = new JSONArray();

        for (Viaje v : mapa.values()) {
            JSONObject viaje = v.toJSON();
            datos.add(viaje);
        }

        os.write(datos.toJSONString());
    }

    /**
     * Genera los datos iniciales
     */
    private void generaDatos() {

        Viaje viaje = new Viaje("pedro", "Castellón", "Alicante", "28-05-2023", 16, 1);
        mapa.put(viaje.getCodviaje(), viaje);

        viaje = new Viaje("pedro", "Alicante", "Castellón", "29-05-2023", 16, 1);
        mapa.put(viaje.getCodviaje(), viaje);

        viaje = new Viaje("maria", "Madrid", "Valencia", "07-06-2023", 7, 2);
        mapa.put(viaje.getCodviaje(), viaje);

        viaje = new Viaje("carmen", "Sevilla", "Barcelona", "12-08-2023", 64, 1);
        mapa.put(viaje.getCodviaje(), viaje);

        viaje = new Viaje("juan", "Castellón", "Cordoba", "07-11-2023", 39, 3);
        mapa.put(viaje.getCodviaje(), viaje);

    }
    /**
     * Lee los datos del fichero en formato JSON y los añade al diccionario en memoria
     *
     * @param is	stream de lectura de los datos del fichero
     */
    private void leeFichero(FileReader is) {
        JSONParser parser = new JSONParser();
        try {
            // Leemos toda la información del fichero en un array de objetos JSON
            JSONArray array = (JSONArray) parser.parse(is);
            // Rellena los datos del diccionario en memoria a partir del JSONArray
            rellenaDiccionario(array);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /**
     * Rellena el diccionario a partir de los datos en un JSONArray
     *
     * @param array	JSONArray con los datos de los Viajes
     */
    private void rellenaDiccionario(JSONArray array) {
        for (Object o : array) {
            JSONObject obj = (JSONObject) o;
            Viaje v = new Viaje(obj);
            this.mapa.put(v.getCodviaje(),v);
        }
    }

    /**
     * Devuelve los viajes disponibles con un origen dado
     *
     * @param origen
     * @return JSONArray de viajes con un origen dado. Vacío si no hay viajes disponibles con ese origen
     */
    @Override
    public JSONArray consultaViajes(String origen) {

        JSONArray viajesDestino = new JSONArray();

        for (Viaje v : mapa.values()) {

            if (v.getOrigen().equals(origen)) {

                viajesDestino.add(v.toJSON());

            }

        }

        return viajesDestino;
    }


    @Override
    public JSONObject reservaViaje(String codviaje, String codcli) {

        if (this.mapa.containsKey(codviaje)) {

            Viaje v = this.mapa.get(codviaje);

            if (v.finalizado()){
                return new JSONObject();
            }

            if (v.quedanPlazas()) {

                if (v.getPasajeros().contains(codcli) || v.getCodprop().equals(codcli)) {
                    return new JSONObject();
                } else {
                    v.anyadePasajero(codcli);
                    JSONObject reservado = v.toJSON();
                    return reservado;
                }

            } else {
                return new JSONObject();
            }

        } else {
            return new JSONObject();
        }

    }

    @Override
    public JSONObject anulaReserva(String codviaje, String codcli) { //COMPLETADO

        if (mapa.containsKey(codviaje)) {

            Viaje v = mapa.get(codviaje);
            if (v.finalizado()){
                return new JSONObject();
            }
            if (v.borraPasajero(codcli)) {
                return v.toJSON();
            } else {
                return new JSONObject();
            }


        } else {
            return new JSONObject();
        }
    }

    private boolean es_fecha_valida(String fecha) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            LocalDate dia = LocalDate.parse(fecha, formatter);
            LocalDate hoy = LocalDate.now();

            return dia.isAfter(hoy);
        }
        catch (DateTimeParseException e) {
            System.out.println("Fecha invalida: " + fecha);
            return false;
        }

    }

    @Override
    public JSONObject ofertaViaje(String codcli, String origen, String destino, String fecha, long precio, long numplazas) { //COMPLETADO
        if (es_fecha_valida(fecha)){
            Viaje nuevo = new Viaje(codcli,origen,destino,fecha,precio,numplazas);
            mapa.put(nuevo.getCodviaje(),nuevo);
            return nuevo.toJSON();
        }
        return new JSONObject();
    }

    @Override
    public JSONObject borraViaje(String codviaje, String codcli) { //COMPLETADO

        if (this.mapa.containsKey(codviaje)) {
            Viaje v = this.mapa.get(codviaje);
            if (v.finalizado()){
                return new JSONObject();
            }
            if (v.getCodprop().equals(codcli)) {
                JSONObject eliminado = v.toJSON();
                this.mapa.remove(codviaje);
                return eliminado;
            } else {
                return new JSONObject();
            }
        } else {
            return new JSONObject();

        }
    }
}
