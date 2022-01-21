package com.ib.euroact.migration;

import com.ib.system.exceptions.InvalidParameterException;

public class Container {
	
	private Integer action;
	private String name;
	private String url;	
	private String dopInfo;
	
	private String nomer;
	private String vid;
	
	private Dosie dosie = null; 
	
	public Container(Integer action) {
		this.action = action;
	}
	
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) throws InvalidParameterException {
		if (this.action == null) {
			this.action = action;
		}else {
			throw new InvalidParameterException("Container action is allready filled " + this.action + " --- " + action );
		}
	}
	public String getName() {
		return name;
	}
	
	
	public void addToName(String name) {
		
//		if (name.contains("чл. 25, параграф 2, буква г) от Регламент (ЕС) № 648/2012")) {
//			System.out.println("опс");
//		}
		
		
		if (this.name == null) {
			this.name = name;
		}else {
			this.name += "\r\n" + name; 
			//throw new InvalidParameterException("Container name is allready filled " + this.name + " --- " + name );
		}
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	
	
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) throws InvalidParameterException {
		this.url = url;
	}
	
	public boolean isReady() {
		if (action != null && name != null && url != null) {
			//name = name.replace("\r", "");
			//name = name.replace("\n", "");
			name = name.trim();
			if (name.startsWith("-")) {
				name = name.substring(1);
				name = name.trim();
			}
			
			if (name.startsWith("и ")) {
				name = name.substring(1);
				name = name.trim();
			}
			
			if (this.name.startsWith("(е допълнена с:")) {
				System.out.println("LELEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
			}
			
			return true;			
		}else {
			return false;
		}
	}

	

	public Dosie getDosie() {
		return dosie;
	}

	public void setDosie(Dosie dosie) {
		this.dosie = dosie;
	}

	public String getDopInfo() {
		return dopInfo;
	}

	public void setDopInfo(String dopInfo) {
		this.dopInfo = dopInfo;
	}

	public String getNomer() {
		return nomer;
	}

	public void setNomer(String nomer) {
		this.nomer = nomer;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}
	

}
