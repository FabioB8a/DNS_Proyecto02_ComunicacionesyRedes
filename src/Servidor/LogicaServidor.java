package Servidor;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import Datos.Header;
import Datos.Question;

import Utilidades.Adaptador;
import Utilidades.ManejadorArchivo;

public class LogicaServidor {

	private int puerto;
	private DatagramSocket socketUDP;
	private boolean salir;

	private Header head;
	private Question quest;

	private Adaptador adaptador;
	private ManejadorArchivo manejador;
	
	private static String nombreArchivo="C:/Users/Estudiante/Documents/DNS_Proyecto02_ComunicacionesyRedes/src/MasterFile.txt";

	LogicaServidor(int numPuerto) throws FileNotFoundException, IOException{
		this.puerto= numPuerto;
		this.salir=false;
		adaptador = new Adaptador();
		manejador = new ManejadorArchivo();
		manejador.cargarArchivo(nombreArchivo);
		iniciarSocket();
	}
	
	 public void iniciarSocket(){
		System.out.println("Iniciado el servidor UDP");
        try {
			this.socketUDP = new DatagramSocket(this.puerto);
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	 
	 public void terminarSocket(){
		System.out.println("Terminando el servidor UDP");
        this.socketUDP.close();
	}
	 
	public void escucharMensajes(){
		
		byte bufferRecepcion[]= new byte[1024];
		
	    try {
		    while (true && !this.salir) {

	        	DatagramPacket datagramaRecibido = new DatagramPacket(bufferRecepcion, bufferRecepcion.length);
	            this.socketUDP.receive(datagramaRecibido);
				DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(datagramaRecibido.getData()));
				obtenerDatosPaquete(dataInputStream);
				
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
				enviarDatosPaquete(dataOutputStream);

				byte[] bufferEnviar = byteArrayOutputStream.toByteArray();
				DatagramPacket respuesta = new DatagramPacket(bufferEnviar, bufferEnviar.length, datagramaRecibido.getAddress(), datagramaRecibido.getPort());
				this.socketUDP.send(respuesta);

		    }
	    }
		 catch (IOException e) {
			e.printStackTrace();
		 }
	}

	/* OPCIONAL, colocar la obtención de datos en esta parte para un mejor ordenamiento de código */
	public void obtenerDatosPaquete(DataInputStream dataInputStream) throws IOException{
	
		/* Comienzo Lectura de Header */

		head = new Header();

		head.setID(dataInputStream.readShort());
		
		head.setFlag(dataInputStream.readShort());

		head.setQDCOUNT(dataInputStream.readShort());

		head.setANCOUNT(dataInputStream.readShort());

		head.setNSCOUNT(dataInputStream.readShort());

		head.setARCOUNT(dataInputStream.readShort());

		/* Finalización Lectua de Header */

		/* Impresión de Header para verificación */

		System.out.println(head.toString());

		/* Finalización Impresión de Header */

		/* Comienzo Lectura de Question */
		/* NOTA: Recordar verificación de QTYPE para IPV6 (AAAA -> 28) */

		quest = new Question();

		quest.setQNAME(adaptador.parseQName(dataInputStream));

		quest.setQTYPE(dataInputStream.readShort());

		quest.setQCLASS(dataInputStream.readShort());

		/* Finalización Lectura de Question */

		/* Impresión de Question para verificación */

		System.out.println(quest.toString());

		/* Finalización Impresión de Question */
	}
	
	public void enviarDatosPaquete(DataOutputStream dataOutputStream) throws IOException
	{
		/* Escritura de la cabecera */
		dataOutputStream.writeShort(head.getID());

		dataOutputStream.writeShort(head.getFlag().AnswertoShort());

		dataOutputStream.writeShort(head.getQDCOUNT());

		dataOutputStream.writeShort((short)1);

		dataOutputStream.writeShort(head.getNSCOUNT());
		dataOutputStream.writeShort(head.getARCOUNT());
		/* Fin de la escritura de la cabecera */
		
		/* Escritura de Question */
		int pointerPaquete = dataOutputStream.size();
		int pointerOffset = 0xC000 | pointerPaquete;
		dataOutputStream.write(adaptador.getByteArrayOutputStream().toByteArray());
		dataOutputStream.writeByte(0);

		dataOutputStream.writeShort(quest.getQTYPE());
		dataOutputStream.writeShort(quest.getQCLASS());
		/* Finalización Escritura de Question */
		
		/* Escritura de Respuesta */

		/* Escritura de NAME */
		dataOutputStream.writeShort(pointerOffset);

		/* TYPE: 1 (A) */
		dataOutputStream.writeShort((short)1);

		/* CLASS: 1 (IN) */
		dataOutputStream.writeShort((short)1);

		/* TTL: 598 (Tiempo) <- Colocar la descripción */
		dataOutputStream.writeShort(0);
		dataOutputStream.writeShort((short)500);

		/* Finalización Escritura de Respuesta */

		if(quest.getQTYPE()==1)
		{
			String ipAsociada=obtenerDireccionIp();
			String arregloIP[] = ipAsociada.split("\\.");
			dataOutputStream.writeShort((short)4);

			/* RDATA */
			Integer ipParse;
            for(String str:arregloIP)
			{                
            	ipParse = Integer.valueOf(str);
            	dataOutputStream.writeByte(ipParse.byteValue());
            }

		}
		if(quest.getQTYPE()==28)
		{
			String ipAsociada=obtenerDireccionIp();
			String arregloIP[] = ipAsociada.split("\\.");
			dataOutputStream.writeShort((short)128);

			/* RDATA */
			Integer ipParse;
            for(String str:arregloIP)
			{                
            	ipParse = Integer.valueOf(str);
            	dataOutputStream.writeByte(ipParse.byteValue());
            }
		}
	}

	public String obtenerDireccionIp()
	{
		try{
			String direccionIpAsociada=manejador.obtenerDireccion(quest.getQNAME());
			if(direccionIpAsociada==null)
			{
				InetAddress direccionIP = InetAddress.getByName(quest.getQNAME());
				manejador.agregarDireccion(quest.getQNAME(),direccionIP.getHostAddress());
				manejador.guardarArchivo(nombreArchivo);
				direccionIpAsociada=direccionIP.getHostAddress();
				System.out.println(direccionIP.getHostAddress());
			}
			return direccionIpAsociada;
		}
		catch(UnknownHostException e)
		{
			System.out.println("la extension no es valida");
		}
		return null;
	}
}