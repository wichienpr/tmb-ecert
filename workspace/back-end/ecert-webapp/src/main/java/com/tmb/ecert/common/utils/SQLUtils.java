package com.tmb.ecert.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class SQLUtils {

	
	public static List<String> whereinString(List<String> inputs) {
		List<String> output = new ArrayList<>();
		for (String t : inputs) {
			output.add("'" + t + "'");
		}
		return output;
	}
	
	
	public static String whereinTypeStringSQL(List<String> inputs) {
		if(inputs == null || inputs.isEmpty()) {
			return null;
		}
		return StringUtils.join( SQLUtils.whereinString(inputs), "," );
	}
	
	

}
