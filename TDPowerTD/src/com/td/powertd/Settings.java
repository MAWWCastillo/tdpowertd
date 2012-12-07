package com.td.powertd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public final class Settings {

	FileHandle settingsFile;
	private boolean writable;
	public boolean isWritable() {
		return writable;
	}
	public void setWritable(boolean writable) {
		this.writable = writable;
	}
	private static Settings thisInstance;
	public static Settings getInstance(){
		if(thisInstance==null){
			thisInstance=new Settings();
		}
		return thisInstance;
	}
	private Settings(){
		
		writable = Gdx.files.isExternalStorageAvailable();
		if(Gdx.files.external("TDPowerTD").exists()==false){
			Gdx.files.external("TDPowerTD").mkdirs();
			settingsFile=Gdx.files.external("TDPowerTD/Settings.xml");

		}
	}
}
