package org.kucro3.rma.connection;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;

import org.kucro3.rma.*;
import org.kucro3.rma.connection.exception.RMAPipelineBusyException;
import org.kucro3.rma.connection.exception.RMAPipelineClosedException;
import org.kucro3.rma.connection.exception.RMAPipelineNotReadyException;

public class RMAPipelineInlet implements Iterable<RMAPacket>, Iterator<RMAPacket> {
	RMAPipelineInlet(String file,String name)
	{
		this.head = RemoteMemoryAccess.openContext(file,name, RMAPipeline.headSize());
		if(_mv() != RMAPipeline.MAGIC_VALUE)
			throw new RMAPipelineNotReadyException();
		if(_fb_closed())
			throw new RMAPipelineClosedException();
		while(!_fb_ready()); // wait
		if(_fb_connected())
			throw new RMAPipelineBusyException();
		_fb_connect();
		this.queue = new LinkedList<>();
		this.buffer = RemoteMemoryAccess.openContext(file,name, _ln(), RMAPipeline.headSize());
		this.lastTimestamp = 0;
	}
	
	final int _mv()
	{
		return head.getInt(0);
	}
	
	final int _fb()
	{
		return head.getInt(12);
	}
	
	final void _fb_set(int fb)
	{
		head.putInt(12, fb);
	}
	
	final void _fb_or(int fb)
	{
		_fb_set(_fb() | fb);
	}
	
	final boolean _fb_connected()
	{
		return RMAPipeline.connected(_fb());
	}
	
	final boolean _fb_continue()
	{
		return (_fb() & RMAPipeline.FUNCTIONAL_BITS_PIPELINE_CONTINUE) != 0;
	}
	
	final void _fb_connect()
	{
		_fb_or(RMAPipeline.FUNCTIONAL_BITS_PIPELINE_CONNECTED);
	}
	
	final boolean _fb_closed()
	{
		return RMAPipeline.closed(_fb());
	}
	
	final void _fb_close_pipeline()
	{
		_fb_or(RMAPipeline.FUNCTIONAL_BITS_PIPELINE_CLOSED);
	}
	
	final boolean _fb_writing()
	{
		return (_fb() & RMAPipeline.FUNCTIONAL_BITS_PIPELINE_WRITING) != 0;
	}
	
	final void _fb_read_complete()
	{
		_ts_flush();
	}
	
	final long _ts()
	{
		return head.getLong(4);
	}
	
	final void _ts(long ts)
	{
		head.putLong(4, ts);
	}
	
	final void _ts_flush()
	{
		_ts(this.lastTimestamp = RMAPipeline.newTimestamp());
	}
	
	final boolean _ts_wait()
	{
		while(!_ts_updated())
			if(_fb_closed())
				return false;
		return true;
	}
	
	final boolean _ts_updated()
	{
		return _ts() != this.lastTimestamp;
	}
	
	final int _ln()
	{
		return head.getInt(16);
	}
	
	final boolean _fb_ready()
	{
		return (_fb() & RMAPipeline.FUNCTIONAL_BITS_PIPELINE_READY)
				== RMAPipeline.FUNCTIONAL_BITS_PIPELINE_READY;
	}
	
	public void close()
	{
		_fb_close_pipeline();
		head.close();
		buffer.close();
	}
	
	public boolean isClosed()
	{
		return _fb_closed();
	}
	
	@Deprecated
	public int tryRead()
	{
		return read0(false);
	}
	
	public int read()
	{
		if(isClosed())
			throw new RMAPipelineClosedException();
		return read0(true);
	}
	
	public RMAPacket peekPacket()
	{
		return queue.get(0);
	}
	
	public RMAPacket pollPacket()
	{
		RMAPacket packet = queue.get(0);
		queue.remove(0);
		return packet;
	}
	
	public boolean hasPacket()
	{
		return !queue.isEmpty();
	}
	
	@Override
	public boolean hasNext() 
	{
		return !(queue.isEmpty() && isClosed());
	}

	@Override
	public RMAPacket next()
	{
		if(!hasPacket())
			read();
		return pollPacket();
	}

	@Override
	public Iterator<RMAPacket> iterator() 
	{
		return this;
	}
	
	synchronized int read0(boolean wait)
	{
		if(_fb_closed())
			return 0;
		_ts_wait();
		int count = 0;
		RMAPacket p;
		do {
			p = readPacket();
			queue.add(p);
			count++;
			try {
				if((!_fb_continue()) || _fb_closed())
					break;
			} finally {
				_fb_read_complete();
			}
		} while(_ts_wait());
		return count;
	}
	
	private final RMAPacket readPacket()
	{
		int length = _ln();
		if(length < 0)
			if(length == RMAPipeline.BUFFER_SIGNAL_FRAGMENT)
				throw new RMAPipelineDataFragmentException();
			else
				throw new IllegalStateException("Illegal data length");
		byte[] byts = new byte[length];
		for(int i = 0, j = 4; i < byts.length; i++, j++)
			if(j < buffer.size())
				byts[i] = buffer.getByte(j);
			else if(_fb_continue())
			{
				j = 3;
				i--;
				_fb_read_complete();
				if(!_ts_wait())
					throw new RMAPipelineDataFragmentException();
				System.out.println("Continued.");
				continue;
			}
			else
				break;
		return new RMAPacketImplB(byts);
	}
	
	public int getBufferSize()
	{
		return buffer.size() - 4;
	}
	
	private volatile long lastTimestamp;
	
	private final List<RMAPacket> queue;
	
	private final RMAContext head;
	
	private final RMAContext buffer;
}
