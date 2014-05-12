package com.dogeops.cantiliver.utils.apache;

import java.util.Hashtable;
/* 
 * Eventually I would like to be able to have a user input
 * their log formatting line, rather them input the regular 
 * expression, but we'll get there in time....
 */

public class ApacheLogDictionary {
	private Hashtable formatStringLookup = new Hashtable();
	
	public ApacheLogDictionary() {
		
		formatStringLookup.put("%%", "The percent sign");
		// "Remote IP-address"
		formatStringLookup.put("%a", "(.*)");
		// "Local IP-address"
		formatStringLookup.put("%A", "(.*)");
		formatStringLookup.put("%B","Size of response in bytes, excluding HTTP headers.");
		formatStringLookup.put("%b","Size of response in bytes, excluding HTTP headers. In CLF format, i.e. a '-' rather than a 0 when no bytes are sent.");
		formatStringLookup.put("%D","The time taken to serve the request, in microseconds.");
		formatStringLookup.put("%f","Filename");
		// "Remote host"
		formatStringLookup.put("%h","(.*)");
		formatStringLookup.put("%H","The request protocol");
		formatStringLookup.put("%k","Number of keepalive requests handled on this connection. ");
		formatStringLookup.put("%l","(.*)");
		formatStringLookup.put("%m","The request method");
		formatStringLookup.put("%p","The canonical port of the server serving the request");
		formatStringLookup.put("%P","The process ID of the child that serviced the request.");
		formatStringLookup.put("%q","The query string (prepended with a ? if a query string exists, otherwise an empty string)");
		formatStringLookup.put("%r","First line of request");
		formatStringLookup.put("%R","The handler generating the response (if any).");
		// HTTP Status
		formatStringLookup.put("%s", "(\\d*)");
		formatStringLookup.put("%>s","(\\d*)");
		// "Time the request was received (standard english format)"
		formatStringLookup.put("%t","(\\[.*\\])");
		formatStringLookup.put("%T","The time taken to serve the request, in seconds.");
		formatStringLookup.put("%u","(.*)");
		formatStringLookup.put("%U","The URL path requested, not including any query string.");
		formatStringLookup.put("%v","The canonical ServerName of the server serving the request.");
		formatStringLookup.put("%V","The server name according to the UseCanonicalName setting.");
		formatStringLookup.put("%X","Connection status when response is completed:");
		formatStringLookup.put("%I","Bytes received, including request and headers, cannot be zero. You need to enable mod_logio to use this.");
		formatStringLookup.put("%O","Bytes sent, including headers, cannot be zero. You need to enable mod_logio to use this.");		
		formatStringLookup.put("%{Foobar}C","The contents of cookie Foobar in the request sent to the server. Only version 0 cookies are fully supported.");
		formatStringLookup.put("%{FOOBAR}e","The contents of the environment variable FOOBAR");
		formatStringLookup.put("%{Foobar}i","The contents of Foobar: header line(s) in the request sent to the server. Changes made by other modules (e.g. mod_headers) affect this. If you're interested in what the request header was prior to when most modules would have modified it, use mod_setenvif to copy the header into an internal environment variable and log that value with the %{VARNAME}e described above.");
		formatStringLookup.put("%{Foobar}n","The contents of note Foobar from another module.");
		formatStringLookup.put("%{Foobar}o","The contents of Foobar: header line(s) in the reply.");
		formatStringLookup.put("%{format}p","The canonical port of the server serving the request or the server's actual port or the client's actual port. Valid formats are canonical, local, or remote.");
		formatStringLookup.put("%{format}P","The process ID or thread id of the child that serviced the request. Valid formats are pid, tid, and hextid. hextid requires APR 1.2.0 or higher.");
		formatStringLookup.put("%{format}t","The time, in the form given by format, which should be in strftime(3) format. (potentially localized)");
		
	}
	
	public String lookupFormatItem(String input) {
		return formatStringLookup.get(input).toString();
	}
}
