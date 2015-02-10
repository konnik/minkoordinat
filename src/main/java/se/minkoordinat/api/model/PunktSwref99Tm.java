package se.minkoordinat.api.model;

import com.vividsolutions.jts.geom.Point;

public class PunktSwref99Tm {

	private Double northing = 123.456;
	private Double easting = 234.567;

	public PunktSwref99Tm(Point p) {
		northing = p.getCoordinate().getOrdinate(0);
		easting = p.getCoordinate().getOrdinate(1);
	}

	public Double getNorthing() {
		return northing;
	}

	public Double getEasting() {
		return easting;
	}

}
