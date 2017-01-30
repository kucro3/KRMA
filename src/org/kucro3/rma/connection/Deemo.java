package org.kucro3.rma.connection;

import java.util.*;

public class Deemo {
	public static final void main(String[] args) throws Exception
	{
		final String name = "test1";
		Thread t0 = new Thread(() -> {
			try {
				RMAPipelineOutlet p = RMAPipeline.openOutput("MemorySpace",name);
				for(int i = 0; i < 6; i++)
				{
					RMAPacket packet = RMAPacket.newPacket();
					packet.writeUTF("Hello world!" + i);
					p.appendPacket(packet.finish());
				}
				p.connect();
				p.write();
				System.out.println("[] write complete");
				for(int i = 0; ; i++)
				{
					RMAPacket packet = RMAPacket.newPacket();
					packet.writeUTF("Hello client! I am server." + i);
					p.appendPacket(packet.finish());
					p.write();
					try {
						Thread.sleep(0);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(i >= 200000) break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		Thread t1 = new Thread(() -> {
			try {
				RMAPipelineInlet pipeline = RMAPipeline.openInput("MemorySpace",name);
				for(RMAPacket p : pipeline)
					System.out.println(p.readUTF());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		t0.start();
		Thread.sleep(5000);
		t1.start();
	}
}
