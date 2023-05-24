package Datos;

public class Question {
    private String QNAME;
    private short QTYPE;
    private short QCLASS;

    public Question() {
    }

    public String getQNAME() {
        return QNAME;
    }
    public void setQNAME(String qNAME) {
        QNAME = qNAME;
    }
    public short getQTYPE() {
        return QTYPE;
    }
    public void setQTYPE(short qTYPE) {
        QTYPE = qTYPE;
    }
    public short getQCLASS() {
        return QCLASS;
    }
    public void setQCLASS(short qCLASS) {
        QCLASS = qCLASS;
    }

    @Override
    public String toString() {
        return "\nInformación Header:\n ->QNAME es: " + QNAME + 
                                    "\n ->QTYPE es: " + Integer.toBinaryString(QTYPE) +  
                                    "\n ->QCLASS es: " + Integer.toBinaryString(QCLASS) +"\n";
    }   
}
