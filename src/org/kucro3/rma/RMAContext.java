package org.kucro3.rma;

public abstract class RMAContext {
	protected RMAContext()
	{
		this(null);
	}
	
	protected RMAContext(String name)
	{
		this.contextName = name;
	}
	
	public abstract void close();
	
	public abstract int size();
	
	public abstract boolean isClosed();
	
	public abstract void putByte(int index, byte byts);
	
	public abstract void putChar(int index, char ch);
	
	public abstract void putDouble(int index, double d);
	
	public abstract void putFloat(int index, float f);
	
	public abstract void putInt(int index, int i);
	
	public abstract void putLong(int index, long l);
	
	public abstract void putShort(int index, short s);
	
	public abstract byte getByte(int index);
	
	public abstract char getChar(int index);
	
	public abstract double getDouble(int index);
	
	public abstract float getFloat(int index);
	
	public abstract int getInt(int index);
	
	public abstract long getLong(int index);
	
	public abstract short getShort(int index);
	
	public abstract boolean isIndexInBound(int index);
	
	public final String getContextName()
	{
		return contextName;
	}
	
	private final String contextName;
}
