package org.kucro3.rma.connection;

import java.io.Closeable;
import java.util.List;
import java.util.LinkedList;

import org.kucro3.rma.*;
import org.kucro3.rma.connection.exception.RMAPacketNotFinishedException;
import org.kucro3.rma.connection.exception.RMAPipelineClosedException;

public class RMAPipelineOutlet implements Closeable{
	RMAPipelineOutlet(String file,String name, int bufferSize)
	{
		this.ctx = RemoteMemoryAccess.openContext(file,name, RMAPipeline.realSize(bufferSize));
		if(this.ctx.getInt(0) != 0)
			throw new IllegalArgumentException("Illegal space");
		this.bufferSize = bufferSize;
		this.bufferCtx = 
				RemoteMemoryAccess.openContext(file,name, bufferSize + 4, RMAPipeline.headSize());
		this.queue = new LinkedList<>();
		init();
	}
	
	private int _fb()
	{
		return ctx.getInt(12);
	}
	
	private void _fb_set(int fb)
	{
		ctx.putInt(12, fb);
	}
	
	private void _fb_or(int fb)
	{
		ctx.putInt(12, _fb() | fb);
	}
	
	private void _fb_close_pipeline()
	{
		_fb_or(RMAPipeline.FUNCTIONAL_BITS_PIPELINE_CLOSED);
	}
	
	private void _fb_ready()
	{
		_fb_set(RMAPipeline.FUNCTIONAL_BITS_PIPELINE_READY);
	}
	
	private void _fb_writing()
	{
		_fb_or(RMAPipeline.FUNCTIONAL_BITS_PIPELINE_WRITING);
	}
	
	private void _fb_continue()
	{
		_fb_or(RMAPipeline.FUNCTIONAL_BITS_PIPELINE_CONTINUE);
	}
	
	private void _fb_clear_rw_state()
	{
		_fb_set(_fb() & RMAPipeline.MASK_FB_RW_STATE);
	}
	
	private void _mv_init()
	{
		ctx.putInt(0, RMAPipeline.MAGIC_VALUE);
	}
	
	private long _ts()
	{
		return ctx.getLong(4);
	}
	
	private void _ts(long timestamp)
	{
		ctx.putLong(4, timestamp);
	}
	
	private void _ts_zero()
	{
		_ts(0L);
	}
	
	private void _ts_flush()
	{
		_ts(this.lastTimestamp = RMAPipeline.newTimestamp());
	}
	
	private boolean _fb_closed()
	{
		return RMAPipeline.closed(_fb());
	}
	
	private boolean _ts_wait()
	{
		if(_ts() == 0)
			return true;
		while(!_ts_updated())
			if(_fb_closed())
				return false;
		return true;
	}
	
	private boolean _ts_updated()
	{
		return _ts() != this.lastTimestamp;
	}
	
	private int _ln()
	{
		return ctx.getInt(16);
	}

	private void _ln_init(int size)
	{
		ctx.putInt(16, bufferSize);
	}
	
	private void init()
	{
		_mv_init();
		_ts_zero();
		_fb_ready();
		_ln_init(bufferSize);
	}
	
	public final int getBufferSize()
	{
		return bufferSize;
	}
	
	public void close()
	{
		close(true, true);
	}


	private void close(boolean flag, boolean flushFb)
	{
		if(flushFb)
			_fb_close_pipeline();
		if(flag)
			bufferCtx.close();
	}
	
	public boolean isClosed()
	{
		return _fb_closed();
	}
	
	public final RMAContext getBufferContext()
	{
		return bufferCtx;
	}
	
	public boolean isAvailable()
	{
		return !isClosed();
	}
	
	public boolean isConnected()
	{
		return RMAPipeline.connected(_fb());
	}
	
	public void connect()
	{
		while(!isConnected())
			if(isClosed())
				throw new RMAPipelineClosedException();
	}
	
	void updateLocal()
	{
		int bits = _fb();
		if(bits != lastFB)
		{
			lastFB = bits;
			if(RMAPipeline.closed(bits))
				this.close(true, false);
		}
	}
	
	public synchronized void appendPacket(RMAPacket packet)
	{
		queue.add(packet);
	}
	
	public synchronized void write()
	{
		if(_fb_closed())
			throw new RMAPipelineClosedException();
		while(!queue.isEmpty())
		{
			if(!_ts_wait())
				throw new RMAPipelineClosedException();
			RMAPacket p = pollPacket();
			if(!p.finished())
				throw new RMAPacketNotFinishedException();
			byte[] byts = p.getByteArray();
			int length = byts.length;
			_fb_writing();
			bufferCtx.putInt(0, length);
			for(int i = 0, j = 4; i < length; i++, j++)
				if(j < bufferCtx.size())
					bufferCtx.putByte(j, byts[i]);
				else
				{
					j = 3;
					i--;
					_fb_clear_rw_state();
					_fb_continue();
					_ts_flush();
					if(!_ts_wait())
						throw new RMAPipelineClosedException();
					bufferCtx.putInt(0, RMAPipeline.BUFFER_SIGNAL_FRAGMENT);
				}
			_fb_clear_rw_state();
			if(!queue.isEmpty())
				_fb_continue();
			_ts_flush();
		}
	}
	
	private RMAPacket pollPacket()
	{
		RMAPacket p = queue.get(0);
		queue.remove(0);
		return p;
	}
	
	private volatile long lastTimestamp;
	
	private volatile int lastFB;
	
	private final int bufferSize;
	
	protected final RMAContext bufferCtx;
	
	private final RMAContext ctx;

	final List<RMAPacket> queue;
}
