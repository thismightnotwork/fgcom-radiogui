/* 
 * This file is part of the FGCom-mumble distribution (https://github.com/hbeni/fgcom-mumble).
 * Copyright (c) 2020 Benedikt Hallinger
 * 
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation, version 3.
 */
package hbeni.fgcom_mumble;

import java.util.List;
import java.util.Vector;

/**
 * State class, tracking the current identity/radio configuration.
 * SimConnect slaving fields removed; SkyHigh network slaving added.
 */
public class State {
    protected String callsign;
    protected double latitude;
    protected double longitude;
    protected float  height;    // ft AGL
    protected Vector<Radio> radios;
    protected boolean isSkyHighSlave;  // true when fetching from SkyHigh API
    protected String statusmessage = "";

    public State() {
        radios = new Vector<>();
    }

    public void setCallsign(String cs)  { callsign  = cs; }
    public void setLocation(double lat, double lon, float h) {
        setLatitude(lat); setLongitude(lon); setHeight(h);
    }
    public void setLatitude(double lat)  { latitude  = lat; }
    public void setLongitude(double lon) { longitude = lon; }
    public void setHeight(float h)       { height    = h;   }

    public String getCallsign()    { return callsign;  }
    public double getLatitutde()   { return latitude;  }
    public double getLongitude()   { return longitude; }
    public float  getHeight()      { return height;    }
    public List<Radio> getRadios() { return radios;    }

    public boolean isSkyHighSlave()         { return isSkyHighSlave; }
    public void setSkyHighSlaving(boolean b) { isSkyHighSlave = b;   }
}
