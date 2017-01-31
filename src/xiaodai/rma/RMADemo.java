package xiaodai.rma;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created in 2017 01.30
 *
 * @author Xiaodai
 */
public class RMADemo {
    public static void main(String[] args) {
        try (RMAServer svr = new RMAServer("MemorySpace", "TestPackaging");
             RMAClient cli = new RMAClient("MemorySpace", "TestPackaging")) {
            svr.writeUTF("Hello World!");
            System.out.println(cli.readUTF());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (RMASocket r = new RMASocket("MemorySpace","1a","1a")) { //LOOP
            r.writeUTF("Hello World! 233");
            System.out.println(r.readUTF());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}