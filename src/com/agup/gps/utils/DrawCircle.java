package com.agup.gps.utils;

import java.util.Map;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Polygon;

import android.graphics.Point;

public class DrawCircle {
	
	public DrawCircle(){
		
	}
	
	public void createMercatorCircle(double radius, Point center){
		double lon1 = degToRad(center.x);
		double lat1 = degToRad(center.y);
		double R_KM = 6371; //radius km
		double R_MI = 3963; //radius mi
		//double d = radius/R_KM; //angular distance on earth's surface
		double d = radius/R_MI;
		
		Polygon circle = new Polygon();
		int nodes = 100; //number of nodes in circle
		int step = Math.round(360/nodes);
		int n = 0;
		double[] pointArray;
		
		for(int x = 0; x <= 360; x++){
			int z = Math.round(n+=step);
			double bearing = degToRad(x);
			
			double lat2 = Math.asin(
					Math.sin(lat1) * Math.cos(d) +
					Math.cos(lat1) * Math.sin(d) * Math.cos(bearing)
			);
			
			double lon2 = lon1 + Math.atan2(
					Math.sin(bearing) * Math.sin(d) * Math.cos(lat1), 
					Math.cos(d) - Math.sin(lat1) * Math.sin(lat2)
			);
			
			//pointArray[x] = GeometryEngine.project(x, y, sr)
		}
	}
	
	public static double radToDeg(double radians){
		return radians * 180 / Math.PI;
	}
	
	public static double degToRad(double degrees){
		return degrees * Math.PI / 180;
	}
	
}
