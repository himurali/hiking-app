/*
 * Copyright 2015 EPFL. All rights reserved.
 *
 * Created by simon.schuetz on 01 Nov 2015
 * based on HikingApp QuizQuestion class
 */

package ch.epfl.sweng.team7.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Encapsulates the data of a hike/track, as represented in the backend server.
 * Additional annotations will be added here rather than in the TrackPoint class, to simplify
 * serialization and reduce data rate in communication.
 */
public class TrackData {
    public static final long TRACK_ID_UNKNOWN = -1;

    private long mTrackId;   // Database track ID of this track
    private long mOwnerId;   // Database user ID of owner
    private Date mDate;      // A UTC time stamp
    private List<TrackPoint> mTrackPoints;   // Points of the track, in chronological order

    /**
     * Creates a new TrackData instance from the data provided as arguments.
     * @param trackId the database ID (user id) of the track, TRACK_ID_UNKNOWN if unknown
     * @param ownerId the owner ID (user id) of the track
     * @param date the time/date when this hike was done
     * @param trackPoints the list of points on this track, must be >= 1 point
     * @throws IllegalArgumentException
     */
    public TrackData(long trackId, long ownerId, Date date, List<TrackPoint> trackPoints) {

        // Argument checks
        if (trackId < 0 && trackId != TRACK_ID_UNKNOWN) {
            throw new IllegalArgumentException("Track ID must be positive");
        }
        if (ownerId < 0) {
            throw new IllegalArgumentException("Owner ID must be positive");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date not specified");
        }
        if (date.compareTo(new Date()) > 0) {
            throw new IllegalArgumentException("Date is in the future");
        }
        if (trackPoints == null) {
            throw new IllegalArgumentException("TrackPoints not specified");
        }
        if (trackPoints.size() < 1) {
            throw new IllegalArgumentException("Track must contain at least one point");
        }

        mTrackId = trackId;
        mOwnerId = ownerId;
        mDate = date;
        mTrackPoints = trackPoints;
    }
    
    /**
     * Returns the track ID.
     */
    public long getTrackId() {
        return mTrackId;
    }
    
    /**
     * Returns the owner ID.
     */
    public long getOwnerId() {
        return mOwnerId;
    }
    
    /**
     * Returns the date.
     */
    public Date getDate() {
        return (Date) mDate.clone();
    }
    
    /**
     * Returns a list of the question answers.
     */
    public List<TrackPoint> getTrackPoints() {
        return new ArrayList<TrackPoint>(mTrackPoints);
    }

    /**
     * Sets the Track ID. This function will usually be called after a track has been posted
     * and the server has assigned a new track ID.
     * @param trackId The new track ID
     * @throws IllegalArgumentException on negative inputs
     */
    public void setTrackId(long trackId) throws IllegalArgumentException {
        if (trackId < 0 && trackId != TRACK_ID_UNKNOWN) {
            throw new IllegalArgumentException("Track ID must be positive");
        }
        mTrackId = trackId;
    }
    /**
     * @return a JSON object representing this track
     * @throws JSONException
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("track_id", mTrackId);
        jsonObject.put("owner_id", mOwnerId);
        jsonObject.put("date", mDate.getTime());
        jsonObject.put("track_data", parseTrackPointsList(mTrackPoints));
        return jsonObject;
    }

    /**
     * @return a JSON array of the input
     * @throws JSONException
     */
    private JSONArray parseTrackPointsList(List<TrackPoint> trackPoints) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < trackPoints.size(); ++i) {
            jsonArray.put(trackPoints.get(i).toJSON());
        }
        return jsonArray;
    }

    /**
     * Creates a new TrackData object by parsing a JSON object in the format
     * returned by the server.
     * @param jsonObject a {@link JSONObject} encoding.
     * @return a new TrackData object.
     * @throws JSONException in case of malformed JSON.
     */
    public static TrackData parseFromJSON(JSONObject jsonObject) throws JSONException {

        try {
            JSONArray jsonTrackPoints = jsonObject.getJSONArray("track_data");
            List<TrackPoint> trackPoints = new ArrayList<>();
            for (int i = 0; i < jsonTrackPoints.length(); ++i) {
                trackPoints.add(TrackPoint.parseFromJSON(jsonTrackPoints.getJSONArray(i)));
            }

            Date date = new Date(jsonObject.getLong("date"));
            return new TrackData(
                    jsonObject.getLong("track_id"),
                    jsonObject.getLong("owner_id"),
                    date,
                    trackPoints);
        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid track structure: "+e.getMessage());
        } catch (NullPointerException e) {
            throw new JSONException("Invalid track structure");
        }
    }
}
