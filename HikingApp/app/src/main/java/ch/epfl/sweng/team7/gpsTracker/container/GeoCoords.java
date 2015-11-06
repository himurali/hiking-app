package ch.epfl.sweng.team7.gpsTracker.container;

import com.google.android.gms.maps.model.LatLng;

/**
 * Class that represents Geographic coordinates with latitude, longitude and altitude.
 * It is meant to be used solely as a container.
 */
public class GeoCoords {

    private double latitude;
    private double longitude;
    private double altitude;

    /**
     * Class's constructor with separated latitude, longitude and altitude arguments
     */
    public GeoCoords(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    /**
     * Class' constructor with LatLng and altitude as arguments.
     * To be used directly with GoogleMaps API values.
     */
    public GeoCoords(LatLng latLng, double altitude) {
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
        this.altitude = altitude;
    }

    /**
     * Method called to get a copy GeoCoords as LatLng
     * @return LatLng object
     */
    public LatLng toLatLng() {
        return new LatLng(this.latitude, this.longitude);
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getAltitude() {
        return this.altitude;
    }
}
