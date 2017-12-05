package com.framework.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * file filter
 * 
 * @author sxn
 * 
 */
public class FileNameFilter implements FilenameFilter {
	private String[] parStrings;

	/**
	 * 
	 * @param params
	 */
	public FileNameFilter(String... params) {
		this.parStrings = params;
	}

	@Override
	public boolean accept(File dir, String filename) {
		ArrayList<Boolean> bary=new ArrayList<Boolean>();
		for (String fileFilter : parStrings) {
			bary.add(filename.endsWith(fileFilter));
		}
		for (int i = 0; i < bary.size(); i++) {
			if(bary.get(i)){
				return true;
			}
		}
		return false;
	}
}
