package mitienda;

//import it.sauronsoftware.junique.AlreadyLockedException;
//import it.sauronsoftware.junique.JUnique;

public class main {

    public static void main(String[] args) {
        App.main(args);
        //Para evitar que la aplicacion se abra muchas veces
//        String ID = "myAppMiTiendaRun";
//        boolean listo;
//        try {
//            JUnique.acquireLock(ID);
//            listo = true;
//
//        } catch (AlreadyLockedException e) {
//            listo = false;
//            System.out.println("La aplicacion ya esta corriendo");
//        }
//
//        if (listo) {
//            App.main(args);
//        }
    }
}
