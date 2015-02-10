package se.minkoordinat.api.web;

import java.util.HashMap;
import java.util.Map;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.minkoordinat.api.model.PunktRt90;
import se.minkoordinat.api.model.PunktSwref99Tm;
import se.minkoordinat.api.model.PunktWgs84LatLong;
import se.minkoordinat.api.model.TransformResponse;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

@RestController
public class TransformeraControllerV1 {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final int EPSG_WGS84 = 4326;

	private static final int EPSG_RT90 = 3021;

	private static final int EPSG_SWEREF99TM = 3006;
	// WGS84: EPSG:4326
	// RT90: EPSG:4124
	// SWEREF99TM: EPSG:3006
	
	// axel ordning
	// WGS84: northing (lat), easting (long) 
	// RT90: ?
    // SWEREF99TM: northing, easting
	

	Map<String, CoordinateReferenceSystem> crsMap = new HashMap<>();
	Map<String, Integer> sridMap = new HashMap<>();

	public TransformeraControllerV1() {
		try {
			crsMap.put("sweref99tm", CRS.decode("EPSG:" + EPSG_SWEREF99TM));
			crsMap.put("rt90", CRS.decode("EPSG:" + EPSG_RT90));
			crsMap.put("wgs84", CRS.decode("EPSG:" + EPSG_WGS84));

			sridMap.put("sweref99tm", EPSG_SWEREF99TM);
			sridMap.put("rt90", EPSG_RT90);
			sridMap.put("wgs84", EPSG_WGS84);

		} catch (FactoryException e) {
			throw new RuntimeException("Kan inte skapa koordinatsystem.", e);
		}
	}

	@RequestMapping("/api/v1/transformera/{system}/{northing}/{easting:.+}")
	public TransformResponse transformera(@PathVariable String system,
			@PathVariable Double northing, @PathVariable Double easting) {

		logger.info("Transformera: {}, {}, {}", system, northing, easting);
		
		int srid = getSridFromSystem(system);
		Point p = buildPoint(getCoordinateReferenseSystemFromSystem(system), northing, easting, srid);
		
		CoordinateReferenceSystem sourceCrs = getCoordinateReferenseSystemFromSystem(system);

		TransformResponse response = new TransformResponse();

		response.setRt90(new PunktRt90(transformera(p, sourceCrs, getCoordinateReferenseSystemFromSystem("rt90"))));
		response.setSweref99tm(new PunktSwref99Tm(transformera(p, sourceCrs, getCoordinateReferenseSystemFromSystem("sweref99tm"))));
		response.setWgs84(new PunktWgs84LatLong(transformera(p, sourceCrs, getCoordinateReferenseSystemFromSystem("wgs84"))));
		
		return response;
	}

	private Point transformera(Point p, CoordinateReferenceSystem sourceCrs, CoordinateReferenceSystem targetCrs) {
		try {
			MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs);
			return (Point) JTS.transform(p, transform);
		} catch (Exception e) {
			throw new RuntimeException("Kunde inte transformera punkt.", e);
		}
	}

	private CoordinateReferenceSystem getCoordinateReferenseSystemFromSystem(String system) {
		if (crsMap.containsKey(system)) {
			return crsMap.get(system);
		} else {
			throw new IllegalArgumentException(String.format(
					"Felaktigt koordinatsystem: %s", system));
		}
	}

	private int getSridFromSystem(String system) {
		if (sridMap.containsKey(system)) {
			return sridMap.get(system);
		} else {
			throw new IllegalArgumentException(String.format(
					"Felaktigt koordinatsystem: %s", system));
		}
	}

	private Point buildPoint(CoordinateReferenceSystem crs, double northing, double easting, int srid) {

		Coordinate[] coordinates = new Coordinate[1];
		coordinates[0] = new Coordinate(northing, easting);

		GeometryFactory factory = new GeometryFactory(new PrecisionModel(),
				srid);
		
		CoordinateSequence coordinateSequence = factory
				.getCoordinateSequenceFactory().create(coordinates);
		return factory.createPoint(coordinateSequence);

	}

}
