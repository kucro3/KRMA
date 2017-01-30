package org.kucro3.rma.connection;

import java.io.*;

class RMAPacketImplB extends RMAPacket {
    RMAPacketImplB(byte[] byts) {
        this.byts = byts;
        this.bis = new ByteArrayInputStream(byts);
        this.dis = new DataInputStream(bis);
    }

    public byte[] getByteArray() {
        return byts;
    }

    @Override
    final boolean finished() {
        return true;
    }

    @Override
    public boolean readBoolean() {
        try {
            return dis.readBoolean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte readByte() {
        try {
            return dis.readByte();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public char readChar() {
        try {
            return dis.readChar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double readDouble() {
        try {
            return dis.readDouble();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public float readFloat() {
        try {
            return dis.readFloat();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readFully(byte[] arg0) {
        try {
            dis.readFully(arg0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readFully(byte[] arg0, int arg1, int arg2) {
        try {
            dis.readFully(arg0, arg1, arg2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int readInt() {
        try {
            return dis.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Deprecated
    public String readLine() {
        try {
            return dis.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long readLong() {
        try {
            return dis.readLong();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public short readShort() {
        try {
            return dis.readShort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readUTF() {
        try {
            return dis.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int readUnsignedByte() {
        try {
            return dis.readUnsignedByte();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int readUnsignedShort() {
        try {
            return dis.readUnsignedShort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int skipBytes(int arg0) {
        try {
            return dis.skipBytes(arg0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RMAPacket finish() {
        return this;
    }

    private final byte[] byts;

    private final ByteArrayInputStream bis;

    private final DataInputStream dis;
}
