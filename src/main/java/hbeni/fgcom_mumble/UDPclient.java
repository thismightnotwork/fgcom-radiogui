/* 
 * This file is part of the FGCom-mumble distribution (https://github.com/hbeni/fgcom-mumble).
 * Copyright (c) 2020 Benedikt Hallinger
 *
 * GPL-3.0 — see LICENSE
 */
package hbeni.fgcom_mumble;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * UDP client that sends messages to the fgcom-mumble plugin.
 */
public class UDPclient {

    protected boolean active;
    protected State   state;

    InetAddress    address;
    DatagramSocket socket;

    public UDPclient(State s) {
        this.active = false;
        this.state  = s;
    }

    public void openHost() throws UnknownHostException {
        address = InetAddress.getByName(radioGUI.Options.udpHost);
    }

    protected void openSocket() throws UnknownHostException, SocketException {
        if (socket == null) socket = new DatagramSocket();
    }

    public void setActive(boolean act) { this.active = act; }

    public static String escapeUDP(String field) {
        return field.replace(",", "\\,").replace("=", "\\=");
    }
    public static String escapeUDP(double field) { return escapeUDP(Double.toString(field)); }
    public static String escapeUDP(float  field) { return escapeUDP(Float.toString(field));  }

    public String prepare() {
        String msg = "";
        msg += "CALLSIGN=" + escapeUDP(state.getCallsign());
        msg += ",LAT="     + escapeUDP(state.getLatitutde());
        msg += ",LON="     + escapeUDP(state.getLongitude());
        msg += ",HGT="     + escapeUDP(state.getHeight());

        int i = 1;
        for (Radio r : state.getRadios()) {
            msg += ",COM"+i+"_PTT="  + (r.getPTT()    ? "1" : "0");
            msg += ",COM"+i+"_FRQ="  + escapeUDP(r.getFrequency());
            msg += ",COM"+i+"_VOL="  + escapeUDP(r.getVolume());
            msg += ",COM"+i+"_PWR="  + escapeUDP(r.getPower());
            msg += ",COM"+i+"_SQC="  + escapeUDP(r.getSquelch());
            msg += ",COM"+i+"_CWKHZ=" + escapeUDP(r.getChannelWidth());
            msg += ",COM"+i+"_PBT="  + (r.getPwrBtn() ? "1" : "0");
            msg += ",COM"+i+"_RDF="  + (r.getRDF()    ? "1" : "0");
            i++;
        }

        float dbgSig = (radioGUI.Options.debugSignalOverride >= 0)
            ? (float) radioGUI.Options.debugSignalOverride / 100 : -1;
        msg += ",DEBUG_SIGQLY=" + dbgSig;
        msg += ",AUDIO_FX_RADIO="   + (radioGUI.Options.enableAudioEffecs          ? "1" : "0");
        msg += ",AUDIO_HEAR_ALL="   + (radioGUI.Options.allowHearingNonPluginUsers  ? "1" : "0");
        msg += ",ALWAYSMUMBLEPTT=" + (radioGUI.Options.alwaysMumblePTT            ? "1" : "0");
        return msg;
    }

    public SendRes send() {
        SendRes res = new SendRes();
        String msg  = prepare() + '\n';
        if (this.active) {
            try {
                openHost();
                openSocket();
                byte[] buffer = msg.getBytes();
                DatagramPacket pkt = new DatagramPacket(buffer, buffer.length, address, radioGUI.Options.udpPort);
                socket.send(pkt);
                res.res = true;
                res.msg = "SEND_OK: " + msg;
            } catch (UnknownHostException | SocketException ex) {
                res.res = false; res.msg = "SEND_ERR: " + ex.toString();
            } catch (IOException ex) {
                res.res = false; res.msg = "SEND_ERR: " + ex.toString();
            }
        } else {
            res.res = false;
            res.msg = "not connected: " + msg;
        }
        return res;
    }

    public class SendRes {
        public String  msg = "";
        public boolean res = false;
    }
}
