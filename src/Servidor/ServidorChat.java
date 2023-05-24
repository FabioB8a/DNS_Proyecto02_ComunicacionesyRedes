package Servidor;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ServidorChat {
   
    
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		 
		LogicaServidor servidor= new LogicaServidor(53);
		
		servidor.escucharMensajes();

	}

}
