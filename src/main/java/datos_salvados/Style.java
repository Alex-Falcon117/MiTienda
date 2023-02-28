package datos_salvados;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Style extends Datos{
    
//    String url = "classes/saved_data/";
//
//    FileWriter fichero;
//    PrintWriter pw;
//
//    File archivo;
//    FileReader fr;
//    BufferedReader br;

    public void nuevoEstilo(String estilo, boolean animacion) throws IOException {
        
        //Se guarda el estilo seleccionado
        String opciones = estilo+","+ animacion;
        fichero = new FileWriter(url+"style.txt", false);
        pw = new PrintWriter(fichero);

        pw.print(opciones);

        fichero.close();
        pw.close();

    }

    public String[] estiloSeleccionado() {
        String[] opciones = new String[2];
        String opsionesString;
        try {
            //Cuando se selecciona un estilo
            archivo = new File(url+"style.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            opsionesString = br.readLine();
            opciones = opsionesString.split(",");

            fr.close();
            br.close();

            return opciones;
            
        } catch (IOException e) {
            
            //En caso de que ningun estilo aya sido eleguido
            opciones[0] = "style/style_celeste.css";
            opciones[1] = "false";
            
            return opciones;
        }

    }

}
