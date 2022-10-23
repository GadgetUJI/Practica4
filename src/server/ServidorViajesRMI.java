package server;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class ServidorViajesRMI {

    public static void main(String args[]) {
        try{
            startRegistry(1099);
            ImpServidorViajes exportedObj = new ImpServidorViajes(); //Instancia creada
            String registryURL = "rmi://localhost:1099/rmi";
            Naming.rebind(registryURL, exportedObj);
            System.out.println("RMI Server ready.");
        } catch (Exception re) {
            System.out.println("Exception in ServidorViajesRMI.main: " + re);
        } // end catch
    } // end main
    //This method starts a RMI registry on the local host, if
    //it does not already exists at the specified port number.
    private static void startRegistry(int RMIPortNum) throws RemoteException {
        // Known code from the book of Liu is here
    } // end startRegistry


}
