package xiaodai.rma;

import org.kucro3.rma.connection.RMAPacket;
import org.kucro3.rma.connection.RMAPipeline;
import org.kucro3.rma.connection.RMAPipelineOutlet;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created in 2017 01.30
 *
 * @author Xiaodai
 */
public class RMAServer implements Closeable{

    private RMAPipelineOutlet outlet;

    public RMAServer(String pool,String id){
        this.outlet = RMAPipeline.openOutput(pool,id);
    }

    private void ensureConnection(){
        if(!outlet.isConnected()) outlet.connect();
    }

    public void writeUTF(String s) {
        RMAPacket packet = RMAPacket.newPacket();
        packet.writeUTF(s);
        ensureConnection();
        outlet.appendPacket(packet.finish());
        outlet.write();
    }

    @Override
    public void close() throws IOException {
        outlet.close();
    }
}
