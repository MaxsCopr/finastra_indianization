package com.bs.wiseconnect.migration.loader.utility;
 
public class QueryConstants

{

  public static final String select_branch_query = "SELECT BRANCH FROM SOURCESYSTEMLOOKUP WHERE SOURCESYSTEM = ?";

  public static final String select_product_Type_query = "SELECT SUBPRODUCT FROM PRODUCTCODELOOKUP WHERE t24producttype = ? and branch= ?";

}