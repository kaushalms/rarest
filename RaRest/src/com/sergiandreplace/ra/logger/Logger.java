package com.sergiandreplace.ra.logger;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import android.util.Log;


public class Logger {
	public static enum Switcher {
		on, off
	};

	@Attribute(required = false)
	private Boolean show= false;

	@ElementList(inline = true, required = false)
	private List<Include> includes;

	public boolean hasInclude(String type) {
		boolean found=false;
		for (Include include:includes) {
			if (include.getType().equals(type) && include.getShow()) {
				found=true;
				break;
			}
		}
		return found;
	}

	public String toString() {
		StringBuilder logsBuilder=new StringBuilder();
		for (Include include:includes) {
			logsBuilder.append("        ");
			logsBuilder.append(include.toString());
			logsBuilder.append("\n");
		}
		return String.format("    Debug.start status=%s \n%s\n    Debug.end", getShow().toString(), logsBuilder.toString());
	}

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}
	
	private void logMessage(String type, String message) {
		if (show && hasInclude(type)) {
			Log.d(String.format("RA.%s",type),message);
		}
	}
	
	public void s(String message) {
		logMessage("service",message);
	}
	
	public void p(String message) {
		logMessage("param", message);
	}
	public void h(String message) {
		logMessage("header", message);
	}

}
