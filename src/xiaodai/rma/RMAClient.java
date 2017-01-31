package xiaodai.rma;

import org.kucro3.rma.connection.RMAPacket;
import org.kucro3.rma.connection.RMAPipeline;
import org.kucro3.rma.connection.RMAPipelineInlet;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created in 2017 01.30
 *
 * @author Xiaodai
 */

@SuppressWarnings("unused")
public class RMAClient implements Closeable {

    private RMAPipelineInlet inlet;

    public RMAClient(String pool, String id) {
        this.inlet = RMAPipeline.openInput(pool, id);
    }

    @Override
    public void close() throws IOException {
        inlet.close();
    }

    public boolean hasNext(){
        return inlet.hasNext();
    }

    /**
     * Read all packages.
     */
    public void forEach(Consumer<? super RMAPacket> c){
        inlet.forEach(c);
    }

    public boolean readBoolean() {
        return inlet.next().readBoolean();
    }

    public byte readByte() {
        return inlet.next().readByte();
    }

    public char readChar() {
        return inlet.next().readChar();
    }


    public double readDouble() {
        return inlet.next().readDouble();
    }


    public float readFloat() {
        return inlet.next().readFloat();
    }


    public void readFully(byte[] arg0) {
        inlet.next().readFully(arg0);
    }


    public void readFully(byte[] arg0, int arg1, int arg2) {
        inlet.next().readFully(arg0, arg1, arg2);
    }


    public int readInt() {
        return inlet.next().readInt();
    }


    public String readLine() {
        return inlet.next().readLine();
    }


    public long readLong() {
        return inlet.next().readLong();
    }


    public short readShort() {
        return inlet.next().readShort();
    }


    public String readUTF() {
        return inlet.next().readUTF();
    }


    public int readUnsignedByte() {
        return inlet.next().readUnsignedByte();
    }


    public int readUnsignedShort() {
        return inlet.next().readUnsignedShort();
    }

    public int skipBytes(int arg0){
        return inlet.next().skipBytes(arg0);
    }
}
