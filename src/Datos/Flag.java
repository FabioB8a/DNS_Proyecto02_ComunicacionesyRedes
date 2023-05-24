package Datos;

public class Flag {
    
    private boolean QR;
    private byte Opcode;
    private boolean AA;
    private boolean TC;
    private boolean RD;
    private boolean RA;
    private byte Z;
    private byte RCODE;
    
    public Flag() {}
    public boolean isQR() {
        return QR;
    }
    public void setQR(boolean qR) {
        QR = qR;
    }
    public byte getOpcode() {
        return Opcode;
    }
    public void setOpcode(byte opcode) {
        Opcode = opcode;
    }
    public boolean isAA() {
        return AA;
    }
    public void setAA(boolean aA) {
        AA = aA;
    }
    public boolean isTC() {
        return TC;
    }
    public void setTC(boolean tC) {
        TC = tC;
    }
    public boolean isRD() {
        return RD;
    }
    public void setRD(boolean rD) {
        RD = rD;
    }
    public boolean isRA() {
        return RA;
    }
    public void setRA(boolean rA) {
        RA = rA;
    }
    public byte getZ() {
        return Z;
    }
    public void setZ(byte z) {
        Z = z;
    }
    public byte getRCODE() {
        return RCODE;
    }
    public void setRCODE(byte rCODE) {
        RCODE = rCODE;
    }

    public short AnswertoShort() {
        short result = 0;

        if (!isQR()) {
            result |= 1 << 15;
        }

        result |= (getOpcode() & 0xF) << 11;

        if (isAA()) {
            result |= 1 << 10;
        }

        if (isTC()) {
            result |= 1 << 9;
        }

        if (isRD()) {
            result |= 1 << 8;
        }

        if (!isRA()) {
            result |= 1 << 7;
        }

        result |= (getZ() & 0x7) << 4;
        result |= getRCODE() & 0xF;

        return result;
    }

    @Override
    public String toString() {
    return "Flag [QR=" + (QR ? 1 : 0) + ", Opcode=" + Opcode + ", AA=" + (AA ? 1 : 0) + ", TC=" + (TC ? 1 : 0)
            + ", RD=" + (RD ? 1 : 0) + ", RA=" + (RA ? 1 : 0) + ", Z=" + Z + ", RCODE=" + RCODE + "]";
    }
}
