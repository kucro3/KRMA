package org.kucro3.rma.connection;

import java.util.Random;

/**
 * Pipeline struction:
 * +----------+----------+-------------+----------+-----------+
 * | Magic value | Timestamp | Functional bits  | Buffer Size  | Data buffer   |
 * +----------+----------+-------------+----------+-----------+
 *
 * @author Kumonda221
 */

public class RMAPipeline {
    RMAPipeline(String ctxName) {

    }

    public static RMAPipelineInlet openInput(String file, String name) {
        return new RMAPipelineInlet(file, name);
    }

    public static RMAPipelineOutlet openOutput(String file, String name) {
        return openOutput(file, name, DEFAULT_BUFFER_SIZE);
    }

    public static RMAPipelineOutlet openOutput(String file, String name, int bufferSize) {
        return new RMAPipelineOutlet(file,name, bufferSize);
    }

    public static long newTimestamp() {
        return System.currentTimeMillis() ^ random.nextLong();
    }

    static int headSize() {
        return SIZE_MAGIC_VALUE + SIZE_TIMESTAMP + SIZE_FUNCTIONAL_BITS + SIZE_BUFFER_SIZE;
    }

    static int realSize(int bufferSize) {
        if (bufferSize < 0) throw new IllegalArgumentException();
        return bufferSize + headSize();
    }

    static boolean closed(int fb) {
        return (fb & RMAPipeline.FUNCTIONAL_BITS_PIPELINE_CLOSED) != 0;
    }

    static boolean connected(int fb) {
        return (fb & RMAPipeline.FUNCTIONAL_BITS_PIPELINE_CONNECTED) != 0;
    }

    private static final Random random = new Random();

    public static final int DEFAULT_BUFFER_SIZE = 2048;

    static final int SIZE_MAGIC_VALUE = 4;

    static final int SIZE_TIMESTAMP = 8;

    static final int SIZE_FUNCTIONAL_BITS = 4;

    static final int SIZE_BUFFER_SIZE = 4;

    static final int MAGIC_VALUE = 0xC8030221;

    static final int MASK_FB_INIT_STATE = 0x0FFFFFFF0;

    static final int MASK_FB_RW_STATE = 0xFF0FFFFF;

    static final int MASK_FB_ADV_STATE = 0xF0FFFFFF;

    static final int FUNCTIONAL_BITS_PIPELINE_READY = 0xF0000001;

    static final int FUNCTIONAL_BITS_PIPELINE_CONNECTED = 0x00000002;

    static final int FUNCTIONAL_BITS_PIPELINE_WRITING = 0x00200000;

    static final int FUNCTIONAL_BITS_PIPELINE_CONTINUE = 0x00400000;

    static final int FUNCTIONAL_BITS_PIPELINE_CLOSED = 0x08000000;

    static final int BUFFER_SIGNAL_FRAGMENT = -1;
}
