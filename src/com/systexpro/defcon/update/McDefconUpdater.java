package com.systexpro.defcon.update;

import java.io.*;
import java.net.URL;

import com.systexpro.defcon.McDefcon;

public class McDefconUpdater {

	public final String WEBSITE = "http://underrealm.net/downloads/";

	public final String VERSION_FILE = "VERSION";

	public final String JAR_FILE = "McDefcon.jar";

	public String VERSION_OLD;

	public final int VERSION_ZERO = 0;

	public McDefcon plugin;

	public McDefconUpdater(McDefcon d) {
		plugin = d;
	}

	public void getCurrentVersion() {
		try {
			URL url = new URL(WEBSITE + VERSION_FILE);
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(url.openStream()));
			VERSION_OLD = inputStream.readLine();
			inputStream.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadJarFile() {
		try {

            plugin.send("Downloading File: McDefcon.jar");

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
