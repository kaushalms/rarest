package com.sergiandreplace.ra.service;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

public class Debug {
	public static enum Switcher {
		on, off
	};

	@Attribute(required = false)
	private Switcher status = Switcher.off;

	@ElementList(inline = true, required = false)
	private List<Log> logs;

	public Switcher getStatus() {
		return status;
	}

	public void setStatus(Switcher status) {
		this.status = status;
	}

	public String toString() {
		StringBuilder logsBuilder=new StringBuilder();
		for (Log log:logs) {
			logsBuilder.append("        ");
			logsBuilder.append(log.toString());
			logsBuilder.append("\n");
		}
		return String.format("    Debug.start status=%s \n%s\n    Debug.end", status.toString(), logsBuilder.toString());
	}

}
