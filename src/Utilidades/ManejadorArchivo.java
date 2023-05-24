package Utilidades;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ManejadorArchivo {
    private HashMap<String,String> dominiosIP;

    public ManejadorArchivo() {
        this.dominiosIP = new HashMap<String,String>();
    }
    //1. Direccion de dominio    2. Direccion Ip
    public HashMap<String, String> getDominiosIP() {
        return dominiosIP;
    }

    public void setDominiosIP(HashMap<String, String> dominiosIP) {
        this.dominiosIP = dominiosIP;
    }

    public void cargarArchivo(String nombreArchivo) throws IOException,FileNotFoundException{
        InputStreamReader input = new InputStreamReader(new FileInputStream(nombreArchivo));
        BufferedReader leer = new BufferedReader(input);
        String cad = leer.readLine();
        String aux[];
        cad = leer.readLine();

        while(!cad.equalsIgnoreCase("Comunicaciones y Redes"))
        {
            aux = cad.split(" ");
            dominiosIP.put(aux[0],aux[1]);
            cad = leer.readLine();
        }
        leer.close();
    }

    public String obtenerDireccion(String referencia){
        return dominiosIP.get(referencia);
    }

    /* PREGUNTAR AL PROFESOR IMPLEMENTACIÓN DE LAS FUNCIONES */
    public void guardarArchivo(String nombreArchivo) {
        try {
            File file = new File(nombreArchivo);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            
            // Agregar la línea "Realizado por: Miguel Gonzalez, Fabio Buitrago, José Rodriguez"
            bw.write("Realizado por: Miguel Gonzalez, Fabio Buitrago, Jose Rodriguez");
            bw.newLine();
            
            // Agregar el contenido del HashMap
            for (Map.Entry<String, String> entry : dominiosIP.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                bw.write(key + " " + value);
                bw.newLine();
            }
            
            // Agregar la línea "Comunicaciones y Redes"
            bw.write("Comunicaciones y Redes");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    public void agregarDireccion(String dominio, String direccion){
        dominiosIP.put(dominio,direccion);
    }

    @Override
    public String toString() {
        return "ManejadorArchivo [dominiosIP=" + dominiosIP + "]";
    }

    
}
