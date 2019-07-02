package com.sfy.user.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpClientUtil {

	/**
	 * 初始化日志引擎
	 */
	private final Logger logger = LoggerFactory.getLogger(SftpClientUtil.class);

	/** Sftp */
	ChannelSftp sftp = null;
	/** 主机 */
	private String host = "";
	/** 端口 */
	private int port = 0;
	/** 用户名 */
	private String username = "";
	/** 密码 */
	private String password = "";

	/**
	 * 构造函数
	 * 
	 * @param host
	 *            主机
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * 
	 */
	public SftpClientUtil(String host, int port, String username,
			String password) {

		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	/**
	 * 连接sftp服务器
	 * 
	 * @throws Exception
	 */
	public void connect() throws Exception {

		JSch jsch = new JSch();
		Session sshSession = jsch.getSession(this.username, this.host,
				this.port);
		logger.debug(SftpClientUtil.class + "Session created.");

		sshSession.setPassword(password);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		sshSession.setConfig(sshConfig);
		sshSession.connect(20000);
		logger.debug(SftpClientUtil.class + " Session connected.");

		logger.debug(SftpClientUtil.class + " Opening Channel.");
		Channel channel = sshSession.openChannel("sftp");
		channel.connect();
		this.sftp = (ChannelSftp) channel;
		logger.debug(SftpClientUtil.class + " Connected to " + this.host + ".");
	}

	/**
	 * Disconnect with server
	 * 
	 * @throws Exception
	 */
	public void disconnect() throws Exception {
		if (this.sftp != null) {
			if (this.sftp.isConnected()) {
				this.sftp.disconnect();
			} else if (this.sftp.isClosed()) {
				logger.debug(SftpClientUtil.class + " sftp is closed already");
			}
		}
	}

	/**
	 * 上传单个文件
	 * 
	 * @param directory
	 *            上传的目录
	 * @param uploadFile
	 *            要上传的文件
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void upload(String directory, String uploadFile) throws Exception {		
		String mdia = directory + "/" + StringUtilSFY.getFileTimeYMDH();
		try{ 
		    Vector content = this.sftp.ls(mdia); 
		    if(content == null) { 
		    	this.sftp.mkdir(mdia); 
		    } 
		} catch (SftpException e) { 
			this.sftp.mkdir(mdia); 
		}
		
		this.sftp.cd(mdia);		
		File file = new File(uploadFile);
		this.sftp.put(new FileInputStream(file), file.getName());
	}
	
	/**
	 * 上传单个文件
	 * 
	 * @param directory
	 *            上传的目录
	 * @param uploadFileList
	 *            要上传的文件
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<File> upload(String directory, List<String> uploadFileList) throws Exception {
		List<File> fileList = new ArrayList<File>();
		File file = null;
		for (String uploadFile : uploadFileList) {			
			String mdia = directory + "/" + StringUtilSFY.getFileTimeYMDH();
			try{ 
			    Vector content = this.sftp.ls(mdia); 
			    if(content == null) { 
			    	this.sftp.mkdir(mdia); 
			    } 
			} catch (SftpException e) { 
				this.sftp.mkdir(mdia); 
			}
			
			this.sftp.cd(mdia);		
			file = new File(uploadFile);
			this.sftp.put(new FileInputStream(file), file.getName());
			fileList.add(file);
		}
		return fileList;	
	}
	
	/**
	 * 上传单个文件
	 * 
	 * @param directory
	 *            上传的目录
	 * @param uploadFile
	 *            要上传的文件
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String uploadOne(String directory, String uploadFile) throws Exception {
		String mdia = directory + "/" + StringUtilSFY.getFileTimeYMDH();
		try{ 
		    Vector content = this.sftp.ls(mdia); 
		    if(content == null) { 
		    	this.sftp.mkdir(mdia); 
		    } 
		} catch (SftpException e) { 
			this.sftp.mkdir(mdia); 
		}
		
		try {
			this.sftp.cd(mdia);		
			File file = new File(uploadFile);
			String fileName = UUID.randomUUID().toString() + "." + file.getName().split("\\.")[1];
			this.sftp.put(new FileInputStream(file), fileName);
			return fileName;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return null;
	}

	/**
	 * 上传目录下全部文件
	 * 
	 * @param directory
	 *            上传的目录
	 * 
	 * @throws Exception
	 */
	public void uploadByDirectory(String directory) throws Exception {

		String uploadFile = "";
		List<String> uploadFileList = this.listFiles(directory);
		Iterator<String> it = uploadFileList.iterator();

		while (it.hasNext()) {
			uploadFile = it.next().toString();
			this.upload(directory, uploadFile);
		}
	}

	/**
	 * 下载单个文件
	 * 
	 * @param directory
	 *            下载目录
	 * @param downloadFile
	 *            下载的文件
	 * @param saveDirectory
	 *            存在本地的路径
	 * 
	 * @throws Exception
	 */
	public void download(String directory, String downloadFile,
			String saveDirectory) throws Exception {
		String saveFile = saveDirectory + "//" + downloadFile;

		this.sftp.cd(directory);
		File file = new File(saveFile);
		this.sftp.get(downloadFile, new FileOutputStream(file));
	}

	/**
	 * 下载目录下全部文件
	 * 
	 * @param directory
	 *            下载目录
	 * 
	 * @param saveDirectory
	 *            存在本地的路径
	 * 
	 * @throws Exception
	 */
	public void downloadByDirectory(String directory, String saveDirectory)
			throws Exception {
		String downloadFile = "";
		List<String> downloadFileList = this.listFiles(directory);
		Iterator<String> it = downloadFileList.iterator();

		while (it.hasNext()) {
			downloadFile = it.next().toString();
			if (downloadFile.toString().indexOf(".") < 0) {
				continue;
			}
			this.download(directory, downloadFile, saveDirectory);
		}
	}

	
	/**
	 * 下载单个文件
	 * 
	 * @param directory
	 *            下载目录
	 * @param downloadFile
	 *            下载的文件
	 * @param saveDirectory
	 *            存在本地的路径
	 * @param newFileName
	 *            文件下载后的名称
	 * 
	 * @throws Exception
	 */
	public void downloadAndChangeName(String directory, String downloadFile,
			String saveDirectory,String newFileName) throws Exception {
		String saveFile = saveDirectory + "/" + newFileName;

		this.sftp.cd(directory);
		File file = new File(saveDirectory);
		if (!file .exists() && file.isDirectory()) {
			file.mkdirs();
		}
		this.sftp.get(downloadFile, saveFile);
	}

	
	/**
	 * 删除文件
	 * 
	 * @param directory
	 *            要删除文件所在目录
	 * @param deleteFile
	 *            要删除的文件
	 * 
	 * @throws Exception
	 */
	public void delete(String directory, String deleteFile) throws Exception {
		this.sftp.cd(directory);
		this.sftp.rm(deleteFile);
	}

	/**
	 * 列出目录下的文件
	 * 
	 * @param directory
	 *            要列出的目录
	 * 
	 * @return list 文件名列表
	 * 
	 * @throws Exception
	 */
	
	@SuppressWarnings("rawtypes")
	public List<String> listFiles(String directory) throws Exception {
		Vector fileList;
		List<String> fileNameList = new ArrayList<String>();

		fileList = this.sftp.ls(directory);
		Iterator it = fileList.iterator();

		while (it.hasNext()) {
			String fileName = ((LsEntry) it.next()).getFilename();
			if (".".equals(fileName) || "..".equals(fileName)) {
				continue;
			}
			fileNameList.add(fileName);

		}

		return fileNameList;
	}

	/**
	 * 更改文件名
	 * 
	 * @param directory
	 *            文件所在目录
	 * @param oldFileNm
	 *            原文件名
	 * @param newFileNm
	 *            新文件名
	 * 
	 * @throws Exception
	 */
	public void rename(String directory, String oldFileNm, String newFileNm)
			throws Exception {
		this.sftp.cd(directory);
		this.sftp.rename(oldFileNm, newFileNm);
	}

	public void cd(String directory) throws Exception {
		this.sftp.cd(directory);
	}

	public InputStream get(String directory) throws Exception {
		InputStream streatm = this.sftp.get(directory);
		return streatm;
	}
	
	public void createDir(String dirname){
	    try{
	       
	        System.out.println("在目标服务器上成功建立了文件夹: " + dirname);
	    }catch(Exception ex){
	        System.out.println(ex.getMessage());
	    }
	}
	
	public static void main(String[] args) {
		SftpClientUtil sftp = new SftpClientUtil("192.168.8.233", 22, "root", "prosnav");
		try {
			sftp.connect();
//			sftp.uploadByDirectory("/usr/local/static/html");						
			sftp.upload("/usr/local/static/html", "E:\\html\\123.html");			
			System.out.println("aaaaa");
			sftp.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}