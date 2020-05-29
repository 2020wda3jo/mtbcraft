package com.mtbcraft.dto;

import java.util.ArrayList;
import java.util.List;

public class Gpx {
	private double maxLat;
	private double minLat;
	private double maxLon;
	private double minLon;
	private List<Info_GPX> infos;
	public double getMaxLat() {
		return maxLat;
	}
	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}
	public double getMinLat() {
		return minLat;
	}
	public void setMinLat(double minLat) {
		this.minLat = minLat;
	}
	public double getMaxLon() {
		return maxLon;
	}
	public void setMaxLon(double maxLon) {
		this.maxLon = maxLon;
	}
	public double getMinLon() {
		return minLon;
	}
	public void setMinLon(double minLon) {
		this.minLon = minLon;
	}
	public List<Info_GPX> getInfos() {
		return infos;
	}
	public void setInfos(List<Info_GPX> infos) {
		this.infos = infos;
	}
	
	public void setting(String txt) {
		
		txt = txt.substring(txt.indexOf("maxLat=")+("maxLat=\"".length()));
		double maxLat = Double.parseDouble(txt.substring(0, txt.indexOf("\"")));
		txt = txt.substring(txt.indexOf("maxLon=\"")+"maxLon=\"".length());
		double maxLon = Double.parseDouble(txt.substring(0, txt.indexOf("\"")));
		txt = txt.substring(txt.indexOf("minLat=\"")+"minLat=\"".length());
		double minLat = Double.parseDouble(txt.substring(0, txt.indexOf("\"")));
		txt = txt.substring(txt.indexOf("minLon=\"")+"minLon=\"".length());
		double minLon = Double.parseDouble(txt.substring(0, txt.indexOf("\"")));
		
		this.setMaxLat(maxLat);
		this.setMaxLon(maxLon);
		this.setMinLat(minLat);
		this.setMinLon(minLon);
		
		List<Info_GPX> info_list = new ArrayList<Info_GPX>();
		
		while(true) { 
			if(txt.contains("lat=")) {
				Info_GPX info_gpx = new Info_GPX();
				txt = txt.substring(txt.indexOf("lat=\"")+"lat=\"".length());
				double lat = Double.parseDouble(txt.substring(0, txt.indexOf("\"")));
				txt = txt.substring(txt.indexOf("lon=\"")+"lon=\"".length());
				double lon = Double.parseDouble(txt.substring(0, txt.indexOf("\"")));
				txt = txt.substring(txt.indexOf("<ele>")+"<ele>".length());
				double ele = Double.parseDouble(txt.substring(0, txt.indexOf("<")));
				info_gpx.setLat(lat);
				info_gpx.setLon(lon);
				info_gpx.setEle(ele);
				info_list.add(info_gpx);
			}else { break; } 
		}
		this.setInfos(info_list);
	}
	
	
	
	
}
