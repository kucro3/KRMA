package org.kucro3.rma.connection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class RMAPacket implements DataOutput, DataInput {
	RMAPacket()
	{
	}
	
	public static RMAPacket newPacket()
	{
		return new RMAPacketImplA();
	}
	
	boolean finished()
	{
		return false;
	}
	
	public abstract byte[] getByteArray();

	public abstract RMAPacket finish();
	
	@Override
	public boolean readBoolean()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public byte readByte()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public char readChar()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public double readDouble()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public float readFloat()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void readFully(byte[] arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void readFully(byte[] arg0, int arg1, int arg2)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int readInt()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String readLine()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public long readLong()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public short readShort()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String readUTF()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int readUnsignedByte()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int readUnsignedShort()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int skipBytes(int arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(int arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(byte[] arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(byte[] arg0, int arg1, int arg2)
	{
		throw new UnsupportedOperationException();	
	}

	@Override
	public void writeBoolean(boolean arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeByte(int arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeBytes(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeChar(int arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeChars(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeDouble(double arg0) 
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeFloat(float arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeInt(int arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeLong(long arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeShort(int arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeUTF(String arg0)
	{
		throw new UnsupportedOperationException();
	}
}
