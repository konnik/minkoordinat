package se.minkoordinat.api.model;


public class TransformResponse {

	private PunktSwref99Tm sweref99tm = null;
	private PunktRt90 rt90 =  null;
	private PunktWgs84LatLong wgs84 = null;

	public PunktSwref99Tm getSweref99tm() {
		return sweref99tm;
	}

	public PunktRt90 getRt90() {
		return rt90;
	}

	public PunktWgs84LatLong getWgs84() {
		return wgs84;
	}

	public void setSweref99tm(PunktSwref99Tm sweref99tm) {
		this.sweref99tm = sweref99tm;
	}

	public void setRt90(PunktRt90 rt90) {
		this.rt90 = rt90;
	}

	public void setWgs84(PunktWgs84LatLong wgs84) {
		this.wgs84 = wgs84;
	}
	
	
	
	

}
