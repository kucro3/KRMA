package org.kucro3.rma.connection;

import java.io.*;

class RMAPacketImplA extends RMAPacket {
	RMAPacketImplA()
	{
		this.bos = new ByteArrayOutputStream();
		this.dos = new DataOutputStream(bos);
	}
	
	@Override
	public RMAPacket finish() 
	{
		return new RMAPacketImplB(bos.toByteArray());
	}
	
	@Override
	public void writeUTF(String s)
	{
		try {
			dos.writeUTF(s);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public byte[] getByteArray()
	{
		return bos.toByteArray();
	}
	
	private final ByteArrayOutputStream bos;
	
	private final DataOutputStream dos;
}
