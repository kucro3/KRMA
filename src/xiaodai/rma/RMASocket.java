package xiaodai.rma;

import org.kucro3.rma.connection.RMAPacket;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created in 2017 01.31
 *
 * @author Xiaodai
 */

@SuppressWarnings("unused")
public class RMASocket implements Closeable {

    private RMAServer svr;
    private RMAClient cli;

    public RMASocket(String pool, String outId, String inId) {
        this.svr = new RMAServer(pool, outId);
        this.cli = new RMAClient(pool, inId);
    }

    public void connect() {
        svr.connect();
    }

    public boolean isConnected(){
        return svr.isConnected();
    }

    @Override
    public void close() throws IOException {
        svr.close();
        cli.close();
    }
    
    /* Client Part */

    public boolean hasNext() {
        return cli.hasNext();
    }

    /**
     * Read all packages.
     */
    public void forEach(Consumer<? super RMAPacket> c) {
        cli.forEach(c);
    }

    public boolean readBoolean() {
        return cli.readBoolean();
    }

    public byte readByte() {
        return cli.readByte();
    }

    public char readChar() {
        return cli.readChar();
    }

    public double readDouble() {
        return cli.readDouble();
    }

    public float readFloat() {
        return cli.readFloat();
    }

    public void readFully(byte[] arg0) {
        cli.readFully(arg0);
    }

    public void readFully(byte[] arg0, int arg1, int arg2) {
        cli.readFully(arg0, arg1, arg2);
    }

    public int readInt() {
        return cli.readInt();
    }

    public String readLine() {
        return cli.readLine();
    }

    public long readLong() {
        return cli.readLong();
    }

    public short readShort() {
        return cli.readShort();
    }

    public String readUTF() {
        return cli.readUTF();
    }

    public int readUnsignedByte() {
        return cli.readUnsignedByte();
    }

    public int readUnsignedShort() {
        return cli.readUnsignedShort();
    }

    public int skipBytes(int arg0) {
        return cli.skipBytes(arg0);
    }

    /* Server part */

    public void writeUTF(String s) {
        svr.writeUTF(s);
    }
    
}
