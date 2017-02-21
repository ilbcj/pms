package com.pms.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.pms.util.sftp.SFTPChannel;
import com.pms.util.sftp.SFTPConstants;

public class SFtpUtil {
	private static final Log logger = LogFactory.getLog(SFtpUtil.class.getName());
	
	public static void main(String[] args) throws Exception {
		
		
		
//		// =======上传测试============
		String localFile = "c:\\5416-010000-User-All-1445941888-00233.zip";
		
	    String url = "192.168.1.213";
		url = "15.6.42.223";
		String port = "22";
		String username ="jdzy";
		String password ="jdzy";
		String path ="/send/";
		path="/home/filetransfer/pms/send/test";
		String filename ="5416-010000-User-All-1445941888-00233.zip";
		String remote = path + "/" + filename;
		
		uploadFile(url, port, username, password, localFile, remote);
//		// =======上传测试============
		
		// =======下载测试============
//		String localPath = "D:\\";
//		String url = "192.168.120.133";
//		int port = 21;
//		String username ="ftpuser";
//		String password ="ftpuser";
//		String remotePath ="/data/admftp/kw";
//		String fileName = "test.txt";
//		downFile(url, port, username, password, remotePath, fileName, localPath);
		// =======下载测试============
		
	}
	
	/**
	* Description: 向FTP服务器上传文件
	* @param url FTP服务器 hostname
	* @param port FTP服务器端口 默认端 21
	* @param username FTP登录账号
	* @param password FTP登录密码
	* @param path FTP服务器保存目录
	* @param filename 上传到FTP服务器上的文件名
	* @param input 输入流
	* @return 成功返回true，否则返回false
	*/ 
	public static boolean uploadFile(String url,String port,String username, String password, String localFile, String remoteFile) { 
	    boolean success = false;
	    SFTPChannel channel = null;
	    ChannelSftp chSftp = null;
	    try { 
	    	Map<String, String> sftpDetails = new HashMap<String, String>();
	        // 设置主机ip，端口，用户名，密码
	        sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, url);
	        sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, username);
	        sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, password);
	        sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, port);
	        
	        channel = new SFTPChannel();
	        chSftp = channel.getChannel(sftpDetails, 60000);
	        
	        chSftp.put(localFile, remoteFile, ChannelSftp.OVERWRITE); // 代码段2
	        
	        success = true; 
	    }catch (JSchException e) {
	    	logger.info("upload file via sftp failed. [message: " + e.getMessage() + "]");
	    	success = false;
		} catch (SftpException e) {
			logger.info("upload file via sftp failed. [message: " + e.getMessage() + "]");
			success = false;
		} finally {
	    	if(chSftp != null) {
	    		chSftp.quit();
	    	}
	    	if(channel != null) {
	    		try {
					channel.closeChannel();
				} catch (Exception e) {
					logger.info("close sftp channel failed. [message: " + e.getMessage() + "]");
				}
	    	}
	    } 
	    return success;  
}
	
	/**
	* Description: 从FTP服务器下载文件
	* @param url FTP服务器hostname
	* @param port FTP服务器端口
	* @param username FTP登录账号
	* @param password FTP登录密码
	* @param remotePath FTP服务器上的相对路径
	* @param fileName 要下载的文件名
	* @param localPath 下载后保存到本地的路径
	* @return
	*/ 
//	public static boolean downFile(String url, int port,String username, String password, String remotePath,String fileName,String localPath) { 
//	    boolean success = false; 
//	    FTPClient ftp = new FTPClient();
//	    try { 
//	        int reply; 
//	        ftp.connect(url, port); 
//	        //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器 
//	        ftp.login(username, password);//登录 
//	        reply = ftp.getReplyCode(); 
//	        if (!FTPReply.isPositiveCompletion(reply)) { 
//	            ftp.disconnect(); 
//	            return success; 
//	        } 
//	        ftp.changeWorkingDirectory(remotePath);//转移到FTP服务器目录 
//	        FTPFile[] fs = ftp.listFiles(); 
//	        for(FTPFile ff:fs){
//	        	String remotFileName = new String(ff.getName().getBytes("iso-8859-1"),"utf-8");
//	            if(remotFileName.equals(fileName)){ 
//	                File localFile = new File(localPath+"/"+remotFileName); 
//	                OutputStream is = new FileOutputStream(localFile);  
//	                ftp.retrieveFile(ff.getName(), is); 
//	                is.close(); 
//	            } 
//	        } 
//	         
//	        ftp.logout(); 
//	        success = true; 
//	    } catch (IOException e) { 
//	        e.printStackTrace(); 
//	    } finally { 
//	        if (ftp.isConnected()) { 
//	            try { 
//	                ftp.disconnect(); 
//	            } catch (IOException ioe) { 
//	            } 
//	        } 
//	    } 
//	    return success; 
//	}
}
