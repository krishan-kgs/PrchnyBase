/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Apr 28, 2011
 *  @author LAPTOP
 */

package com.prchny.base.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class FileTransferUtils {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(FileTransferUtils.class);
  
  /**
   * Uploads the file to the ftp server.
   */
  public static boolean uploadFileToFtpLocation(final File file,
      final String serverName, final String userName, final String password,
      final String workingDir) throws IOException {
  
    return uploadFileToFtpLocation(file, serverName, userName, password,
        workingDir, null);
  }
  
  /**
   * Uploads the file to the ftp server and then moves it to a different folder.
   */
  public static boolean uploadFileToFtpLocation(final File file,
      final String serverName, final String userName, final String password,
      final String workingDir, final String moveToFolder) throws IOException {
  
    FileInputStream fis = null;
    final FTPClient client = new FTPClient();
    boolean uploadedToFtp = false;
    try {
      fis = new FileInputStream(file);
      client.connect(serverName);
      client.login(userName, password);
      client.cwd(workingDir);
      client.enterLocalPassiveMode();
      client.setFileType(FTP.BINARY_FILE_TYPE);
      uploadedToFtp = client.storeFile(file.getName(), fis);
      if (uploadedToFtp) {
        LOG.info("Successfully uploaded file {}", file.getName());
      }
      
      if (moveToFolder != null) {
        client.changeToParentDirectory();
        final String from = workingDir + "/" + file.getName();
        final String to = moveToFolder + "/" + file.getName();
        if (client.rename(from, to)) {
          if (LOG.isInfoEnabled()) {
            LOG.info("Failed to move file { } ", file.getName());
          }
        }
      }
      client.logout();
    } catch (final Exception e) {
      LOG.error("Error upoading file to ftp server {}", e);
    } finally {
      if (fis != null) {
        fis.close();
      }
      client.disconnect();
    }
    return uploadedToFtp;
  }
  
  /***
   * Download file from FTP server
   * 
   * @param serverName
   *          FTP server name
   * @param userName
   *          FTP username
   * @param password
   *          FTP password
   * @param remoteFile
   *          Path of the file on FTP server
   * @param destFile
   *          Path of the file on local machine
   * @return true if success, else false
   * @throws IOException
   */
  public static boolean downloadFileFromFtpLocation(final String serverName,
      final String userName, final String password, final String remoteFile,
      final String destFile) throws IOException {
  
    final FTPClient client = new FTPClient();
    final BufferedOutputStream os =
        new BufferedOutputStream(new FileOutputStream(destFile));
    boolean fileDownloaded = false;
    try {
      client.connect(serverName);
      client.login(userName, password);
      client.enterLocalPassiveMode();
      client.setFileType(FTP.BINARY_FILE_TYPE);
      fileDownloaded = client.retrieveFile(remoteFile, os);
    } catch (final Exception e) {
      LOG.error("Error downloading file from ftp server", e);
      final File file = new File(destFile);
      file.delete();
    } finally {
      if (os != null) {
        os.close();
      }
      client.disconnect();
    }
    return fileDownloaded;
  }
  
  /**
   * Delete files in the directory
   * 
   * @param serverName
   *          FTP server
   * @param userName
   *          User should have delete access
   * @param password
   * @param pathName
   *          Path name of the directory to clean up
   * @param startDate
   *          Files updated after startDate will be deleted. If null, all the
   *          files from the start to endDate will be deleted
   * @param endDate
   *          Files before after endDate will be deleted. If null, all the files
   *          from the startDate till now will be deleted
   * @return True if success, else false
   * @throws IOException
   */
  public static boolean cleanUpDirectory(final String serverName,
      final String userName, final String password, final String pathName,
      final Date startDate, final Date endDate) throws IOException {
  
    final FTPClient client = new FTPClient();
    boolean cleanedUp = false;
    try {
      client.connect(serverName);
      client.login(userName, password);
      client.enterLocalPassiveMode();
      client.setFileType(FTP.BINARY_FILE_TYPE);
      final FTPFile[] files = client.listFiles(pathName);
      boolean allFilesDeleted = true;
      for (final FTPFile ftpFile : files) {
        if (!ftpFile.isFile()) {
          continue;
        }
        final Date updatedDate = ftpFile.getTimestamp().getTime();
        if ((startDate != null) && updatedDate.before(startDate)) {
          continue;
        }
        if ((endDate != null) && updatedDate.after(endDate)) {
          continue;
        }
        final StringBuilder builder = new StringBuilder(pathName);
        builder.append("/").append(ftpFile.getName());
        final String filePath = builder.toString();
        if (!client.deleteFile(filePath)) {
          allFilesDeleted = false;
        }
      }
      cleanedUp = allFilesDeleted;
      client.logout();
    } catch (final SocketException e) {
      LOG.error("Error cleaning up directory {}, {}", pathName, e);
    } catch (final IOException e) {
      LOG.error("Error cleaning up directory {}, {}", pathName, e);
    } finally {
      client.disconnect();
    }
    return cleanedUp;
  }
  
  /**
   * Uploads the file to sftp server.
   * 
   * @param file
   * @param serverName
   * @param userName
   * @param password
   * @param port
   * @param workingDir
   * @return
   * @throws FileNotFoundException
   * @throws SftpException
   * @throws JSchException
   */
  public static boolean uploadFileToSFtpLocation(final File file,
      final String serverName, final String userName, final String password,
      final int port, final String workingDir) throws FileNotFoundException,
      SftpException, JSchException {
  
    final JSch jsch = new JSch();
    Session session = null;
    Channel channel = null;
    ChannelSftp c = null;
    
    boolean uploadedToFtp = false;
    try {
      // Create a session sending through our username and password
      session = jsch.getSession(userName, serverName, port);
      LOG.info("Session created.");
      session.setPassword(password);
      final java.util.Properties config = new java.util.Properties();
      config.put("StrictHostKeyChecking", "no");
      session.setConfig(config);
      session.connect();
      LOG.info("Session connected.");
      channel = session.openChannel("sftp");
      channel.connect();
      c = (ChannelSftp) channel;
      c.cd(workingDir);
      c.put(new FileInputStream(file), file.getName());
      uploadedToFtp = true;
      LOG.info("Successfully uploaded file {}", file.getName());
    } catch (final JSchException e) {
      LOG.error("Error uploading file: {}", e);
    } catch (final SftpException e) {
      LOG.error("Error uploading file: {}", e);
    } finally {
      if (session != null) {
        session.disconnect();
      }
      if (channel != null) {
        channel.disconnect();
      }
      if (c != null) {
        c.quit();
      }
    }
    return uploadedToFtp;
  }
  
  /**
   * download the file from sftp server.
   * 
   * @param file
   * @param serverName
   * @param userName
   * @param password
   * @param port
   * @param workingDir
   * @return
   * @throws FileNotFoundException
   * @throws SftpException
   * @throws JSchException
   */
  public static boolean downloadFileFromSFtpLocation(final String fileName,
      final String destination, final String serverName, final String userName,
      final String password, final int port, final String workingDir)
      throws FileNotFoundException, SftpException, JSchException {
  
    final JSch jsch = new JSch();
    Session session = null;
    Channel channel = null;
    ChannelSftp c = null;
    boolean isDownloaded = false;
    try {
      session = jsch.getSession(userName, serverName, port);
      final java.util.Properties config = new java.util.Properties();
      config.put("StrictHostKeyChecking", "no");
      session.setConfig(config);
      session.setPassword(password);
      session.connect();
      
      channel = session.openChannel("sftp");
      channel.connect();
      c = (ChannelSftp) channel;
      if (workingDir != null) {
        c.cd(workingDir);
      }
      
      c.get(fileName, destination);
      isDownloaded = true;
      c.exit();
      session.disconnect();
    } catch (final JSchException e) {
      LOG.error("Error downloading file: {}", e);
    } catch (final SftpException e) {
      LOG.error("Error downloading file: {}", e);
    } finally {
      if (session != null) {
        session.disconnect();
      }
      if (channel != null) {
        channel.disconnect();
      }
      if (c != null) {
        c.quit();
      }
    }
    return isDownloaded;
  }
}
