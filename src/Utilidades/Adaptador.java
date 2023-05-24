package Utilidades;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Adaptador {

	private ByteArrayOutputStream byteArrayOutputStream;
	
	public Adaptador() {
		byteArrayOutputStream = new ByteArrayOutputStream();
    }

	public ByteArrayOutputStream getByteArrayOutputStream() {
		System.out.println(byteArrayOutputStream);
		return byteArrayOutputStream;
	}

	public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
		this.byteArrayOutputStream = byteArrayOutputStream;
	}

    public String parseQName(DataInputStream dataInputStream) throws IOException
    {
		byteArrayOutputStream = new ByteArrayOutputStream();
        StringBuilder qnameBuilder = new StringBuilder();
		int recLen;
		while ((recLen = dataInputStream.readByte()) > 0) 
        {
    		byte[] record = new byte[recLen];
    		dataInputStream.readFully(record);
    		String recordString = new String(record, StandardCharsets.UTF_8);
			byteArrayOutputStream.write(recLen);
			byteArrayOutputStream.write(record);
    		qnameBuilder.append(recordString).append(".");
		}

		String QNAME = qnameBuilder.toString();
		if (QNAME.endsWith(".")) 
        {
    		QNAME = QNAME.substring(0, QNAME.length() - 1);
		}
        return QNAME;
    }
	
}
