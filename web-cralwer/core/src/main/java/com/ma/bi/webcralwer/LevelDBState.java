package com.ma.bi.webcralwer;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBException;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;



public class LevelDBState implements State {
	
	
	private File dataDir;
	private Options options;
	
	private DB db;

	public LevelDBState(File dataDir ,Options options) {
		// TODO Auto-generated constructor stub
		this.dataDir = dataDir;
		this.options = options;
	}

	@Override
	public boolean update(byte[] key, byte state) {
		// TODO Auto-generated method stub
		try {
			db.put(key,	new byte[]{state});
			return true;
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return false;
	}
	
	public Collection<String> foundRecordsByState(byte state) {
		
		Collection<String> urls = new LinkedHashSet<String>();
		try {
			DBIterator iterator = db.iterator();
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
				Map.Entry<byte[] , byte[]> entry = iterator.peekNext();
				String key = Iq80DBFactory.asString(entry.getKey());
				byte value = entry.getValue()[0];
				
				if ( state  == value) {
					// --- add to list ---
					urls.add( key );
				}
			}	


		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return urls;
				
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub
		
		// --- create tmp example ---
		try {
			this.db = Iq80DBFactory.factory.open(dataDir, options);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}		

		
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		try {
			this.db.close();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (this.db != null) {
				this.db = null;
			}
		}				
	}
	
	
	

}
