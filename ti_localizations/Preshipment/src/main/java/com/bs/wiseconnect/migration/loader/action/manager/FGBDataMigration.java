package com.bs.wiseconnect.migration.loader.action.manager;
 
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import com.bs.wiseconnect.migration.loader.utility.DBPropertiesLoader;
import com.bs.wiseconnect.migration.loader.utility.QueryBuilder;
 
public class FGBDataMigration

{

  private static final Logger logger = LoggerFactory.getLogger(FGBDataMigration.class);

  public void migrate(String[] args)

    throws Exception

  {

    DBPropertiesLoader.initialize("sourcedb.properties");

    logger.info(DBPropertiesLoader.EJBCLIENT_URL);

    logger.debug("Initializing Database Properties : " + DBPropertiesLoader.JDBC_URL);

 
    String lowRange = null;

    String highRange = null;

    String userName = null;

    String userTeam = null;

    String product = null;

    String financeFlag = null;

    String invoiceno = null;

    String batchid = null;

    if (args.length == 2)

    {

      lowRange = args[1];

    }

    else if (args.length == 3)

    {

      lowRange = args[1];

      highRange = args[2];

    }

    else if (args.length == 7)

    {

      lowRange = args[1];

      highRange = args[2];

      userName = args[3];

      userTeam = args[4];

      product = args[5];

      financeFlag = args[6];

    }

    else if (args.length == 8)

    {

      lowRange = args[1];

      highRange = args[2];

      userName = args[3];

      userTeam = args[4];

      product = args[5];

      invoiceno = args[6];

      batchid = args[7];

    }

    logger.debug("Low Range ->" + lowRange);

    logger.debug("High Range ->" + highRange);

    if ((args[0] != null) && (!args[0].equals("")))

    {

      if (args[0].equals("SUPPLYINV"))

      {

        QueryBuilder.doTFinvdtoQuery(DBPropertiesLoader.INV_DB_TABLE, "INVDTO.properties", lowRange, highRange, userName, userTeam, product, financeFlag);

      }

      else if ((args[0].equals("BUYFIN")) && (DBPropertiesLoader.BUYERFIN_DB_TABLE != null))

      {

        logger.info("Starting migration process for Buyer Finance........");

        QueryBuilder.doTFBuyerFinQuery(DBPropertiesLoader.BUYERFIN_DB_TABLE, "BUYFIN.properties", lowRange, highRange, userName, userTeam, product, invoiceno, batchid);

      }

    }

    else {

      logger.info("Pass the Valid Data Name as parameter to run");

    }

  }

}