package org.kucro3.rma;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.StandardOpenOption;
import java.lang.ref.*;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class RMAContextImpl extends RMAContext {
	RMAContextImpl(String file,String name, long size) throws IOException
	{
		this(file,name, size, 0);
	}
	
	RMAContextImpl(String pool,String name, long size, long pos) throws IOException
	{
        super(name);

        File p = new File(pool);
        if(!p.exists()) p.mkdir();

		File file = new File(p, name);
		if(!file.exists()) { file.createNewFile();}
		file.deleteOnExit();
		FileChannel fc = FileChannel.open(file.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE);
		this.ctx = fc.map(MapMode.READ_WRITE, pos, size);
		this.ctx.load();
	}
	
	@Override
	public void close()
	{
		closed = true;
		Tentation.closeHandle(this.ctx);
		//Tentation.closeHandle(this.ctx,() -> {this.ctx = null;});
		this.ctx = null;
	}
	
	@Override
	public int size()
	{
		return ctx.capacity();
	}
	
	@Override
	public boolean isClosed()
	{
		return closed;
	}
	
	private void checkClosed()
	{
		if(isClosed())
			throw new IllegalStateException("Pipeline already closed");
	}
	
	@Override
	public boolean isIndexInBound(int index)
	{
		return index < size();
	}
	
	@Override
	public void putByte(int index, byte byt)
	{
		checkClosed();
		this.ctx.put(index, byt);
	}
	
	@Override
	public void putChar(int index, char c)
	{
		checkClosed();
		this.ctx.putChar(index, c);
	}
	
	@Override
	public void putDouble(int index, double d)
	{
		checkClosed();
		this.ctx.putDouble(index, d);
	}
	
	@Override
	public void putFloat(int index, float f)
	{
		checkClosed();
		this.ctx.putFloat(index, f);
	}
	
	@Override
	public void putInt(int index, int i)
	{
		checkClosed();
		this.ctx.putInt(index, i);
	}
	
	@Override
	public void putLong(int index, long l)
	{
		checkClosed();
		this.ctx.putLong(index, l);
	}
	
	@Override
	public void putShort(int index, short s)
	{
		checkClosed();
		this.ctx.putShort(index, s);
	}
	
	@Override
	public byte getByte(int index)
	{
		checkClosed();
		return this.ctx.get(index);
	}
	
	@Override
	public char getChar(int index)
	{
		checkClosed();
		return this.ctx.getChar(index);
	}
	
	@Override
	public double getDouble(int index)
	{
		checkClosed();
		return this.ctx.getDouble(index);
	}
	
	@Override
	public float getFloat(int index)
	{
		checkClosed();
		return this.ctx.getFloat(index);
	}
	
	@Override
	public int getInt(int index)
	{
		checkClosed();
		return this.ctx.getInt(index);
	}
	
	@Override
	public long getLong(int index)
	{
		checkClosed();
		return this.ctx.getLong(index);
	}
	
	@Override
	public short getShort(int index)
	{
		checkClosed();
		return this.ctx.getShort(index);
	}


	private MappedByteBuffer ctx;

	private volatile boolean closed;
	
	static class Tentation {
			public static void closeHandle(Object directBuffer)
			{
				// Please use this carefully.
				// This operation may fail on some JVMs.
				final Reference<Object> weak = new WeakReference<>(directBuffer);
				directBuffer = null;
				AccessController.doPrivileged(new PrivilegedAction<Void>()
				{
					@SuppressWarnings("restriction")
					public Void run()
					{
						Object obj = weak.get();
						if(obj != null) try {
							Method getCleanerMethod = obj.getClass()
									.getMethod("cleaner", new Class[0]);
			                getCleanerMethod.setAccessible(true);
			                sun.misc.Cleaner cleaner =(sun.misc.Cleaner)getCleanerMethod.invoke(obj, new Object[0]);
			                cleaner.clean();
						} catch (Throwable e) {
							obj = null;
							weak.clear();
							System.gc();
						}
						else; // I don't even know what I should do when the reference is recycled
						return null;
					}
			});
		}
	}
}
