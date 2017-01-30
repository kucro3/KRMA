package org.kucro3.rma;

import java.io.IOException;

public final class RemoteMemoryAccess {
    private RemoteMemoryAccess() {
    }

    public static RMAContext openContext(String file,String name, int size) {
        return openContext(file,name, size, 0);
    }

    public static RMAContext openContext(String file,String name, int size, int pos) {
        try {
            return new RMAContextImpl(file,name, size, pos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeContext(RMAContext ctx) {
        ctx.close();
    }
}
