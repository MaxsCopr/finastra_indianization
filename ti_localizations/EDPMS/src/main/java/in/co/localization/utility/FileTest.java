package in.co.localization.utility;
 
import com.jcraft.jsch.Channel;

import com.jcraft.jsch.ChannelSftp;

import com.jcraft.jsch.JSch;

import com.jcraft.jsch.JSchException;

import com.jcraft.jsch.Session;

import com.jcraft.jsch.SftpException;

import com.misys.tiplus2.enigma.persistent.sql.parser.ParseException;

import java.io.File;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.List;

import java.util.Properties;

import org.apache.log4j.Logger;
 
public class FileTest

{

  private static Logger logger = Logger.getLogger(FileTest.class.getName());

  static ChannelSftp channelSftp;

  static String sftpDirectory;

  static String NewDir;

  public static void main(String[] args)

    throws FileNotFoundException, IOException, ParseException

  {

    try

    {

      JSch jsch = new JSch();

      Session session = jsch.getSession("root", "10.10.20.135");

      session.setPassword("Kotak@123");

      Properties config = new Properties();

      config.put("StrictHostKeyChecking", "no");

      config.put("PreferredAuthentications", 

        "publickey,keyboard-interactive,password");

      session.setConfig(config);

      session.connect();

      Channel channel = session.openChannel("sftp");

      channel.connect();

      channelSftp = (ChannelSftp)channel;

      logger.info("sftp channel opened and connected.");

      sftpDirectory = "/app/IBM/Localization/EDPMS/ROD/";

      NewDir = sftpDirectory;

      listf("D:\\Arun_ETT\\FileTest\\");

    }

    catch (Exception e)

    {

      e.printStackTrace();

    }

    finally

    {

      logger.info("Transfer Process Completed...");

    }

  }

  public static List<File> listf(String directoryName)

    throws JSchException, SftpException

  {

    File directory = new File(directoryName);

    List<File> resultList = new ArrayList();

    File[] fList = directory.listFiles();

    resultList.addAll(Arrays.asList(fList));

    for (File file : fList) {

      if (file.isFile())

      {

        String filename = file.getAbsolutePath();

        channelSftp.put(filename, NewDir, 0);

        logger.info(filename + " transferred to " + sftpDirectory);

      }

      else if (file.isDirectory())

      {

        logger.info(file.getAbsolutePath());

        NewDir = sftpDirectory + file.getName();

        channelSftp.mkdir(NewDir);

        logger.info(NewDir + " Folder created ");

        resultList.addAll(listf(file.getAbsolutePath()));

      }

    }

    logger.info("Files----->" + Arrays.asList(fList));

    return resultList;

  }

}