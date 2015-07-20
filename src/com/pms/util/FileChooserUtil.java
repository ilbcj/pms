package com.pms.util;

import javax.swing.JFileChooser;


public class FileChooserUtil {
	public Object fileChooser() {
	    JFileChooser fc = new JFileChooser();	
	    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    fc.setDialogTitle("选择文件");		
	    fc.setMultiSelectionEnabled(false);	
	    fc.showSaveDialog(fc);		
	    if (fc.getSelectedFile()==null) {	
	    	return null;	
	    }
	    return fc.getSelectedFile().getPath()+"/";
	}
}
