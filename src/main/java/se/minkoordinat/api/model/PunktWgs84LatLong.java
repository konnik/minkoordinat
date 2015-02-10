package se.minkoordinat.api.model;

import com.vividsolutions.jts.geom.Point;

public class PunktWgs84LatLong {
	
	private Double longValue = 345.678;
	private Double latValue = 456.789;
	
	public PunktWgs84LatLong(Point p) {
		
		latValue = p.getCoordinate().getOrdinate(0);
		longValue = p.getCoordinate().getOrdinate(1);
	}
	
	public Double getLong() {
		return longValue;
	}
	public Double getLat() {
		return latValue;
	}
	
	
	
	
	
}
