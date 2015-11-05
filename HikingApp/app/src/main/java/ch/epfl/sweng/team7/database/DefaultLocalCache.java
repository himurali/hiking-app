/*
 * Copyright 2015 EPFL. All rights reserved.
 *
 * Created by simon.schuetz on 04 Nov 2015
 */
package ch.epfl.sweng.team7.database;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


class DefaultLocalCache implements LocalCache {
    private final int HIKES_CACHE_MAX_SIZE = 100;//TODO this should be higher
    private final HashMap<Long,HikeData> mHikesCache = new FixedSizeHashMap<>(HIKES_CACHE_MAX_SIZE);

    public DefaultLocalCache()  {
    }

    public boolean hasHike(long hikeId) {
        return mHikesCache.containsKey(hikeId);
    }

    public HikeData getHike(long hikeId) {
        return mHikesCache.get(hikeId);
    }

    public void putHike(HikeData hikeData) {
        if(hikeData != null) {
            mHikesCache.put(hikeData.getHikeId(), hikeData);
        }
    }

    public int cachedHikesCount() {
        return mHikesCache.size();
    }


    private class FixedSizeHashMap<K,V> extends LinkedHashMap<K,V> {
        private final int MAX_ENTRIES;

        FixedSizeHashMap(int maxEntries) {
            super(16, 0.75f, true);
            MAX_ENTRIES = maxEntries;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > MAX_ENTRIES;
        }
    }
}
