/* 
 * This file is part of the FGCom-mumble distribution (https://github.com/hbeni/fgcom-mumble).
 * Copyright (c) 2020 Benedikt Hallinger
 *
 * GPL-3.0 — see LICENSE
 */
package hbeni.fgcom_mumble;

/**
 * Represents a single radio.
 */
public class Radio {

    protected String  frequency   = "121.500";
    protected float   volume      = 1.0f;
    protected float   power       = 10.0f;
    protected float   squelch     = 0.1f;
    protected float   channelWidth = 8.33f;
    protected boolean ptt         = false;
    protected boolean pwrBtn      = true;
    protected boolean rdf         = false;

    public Radio() {}
    public Radio(String freq) { this.frequency = freq; }

    public String  getFrequency()    { return frequency;   }
    public float   getVolume()       { return volume;      }
    public float   getPower()        { return power;       }
    public float   getSquelch()      { return squelch;     }
    public float   getChannelWidth() { return channelWidth;}
    public boolean getPTT()          { return ptt;         }
    public boolean getPwrBtn()       { return pwrBtn;      }
    public boolean getRDF()          { return rdf;         }

    public void setFrequency(String f)    { frequency    = f; }
    public void setVolume(float v)        { volume       = v; }
    public void setPower(float p)         { power        = p; }
    public void setSquelch(float s)       { squelch      = s; }
    public void setChannelWidth(float cw) { channelWidth = cw;}
    public void setPTT(boolean p)         { ptt          = p; }
    public void setPwrBtn(boolean b)      { pwrBtn       = b; }
    public void setRDF(boolean r)         { rdf          = r; }
}
