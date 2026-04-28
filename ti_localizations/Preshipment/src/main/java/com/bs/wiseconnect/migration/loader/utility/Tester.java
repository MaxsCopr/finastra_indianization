package com.bs.wiseconnect.migration.loader.utility;
 
import java.io.BufferedReader;

import java.io.FileReader;

import java.io.IOException;

import java.util.ArrayList;

import java.util.List;
 
public class Tester

{

  public static void main(String[] args)

    throws IOException

  {

    List<String> strValues = new ArrayList();

    strValues.add("String 1");

    strValues.add("String 2");

  }

  public static String readFile(String filePath)

    throws IOException

  {

    BufferedReader reader = new BufferedReader(new FileReader(filePath));

    String line = null;

    StringBuilder stringBuilder = new StringBuilder();

    String ls = System.getProperty("line.separator");

    while ((line = reader.readLine()) != null)

    {

      stringBuilder.append(line);

      stringBuilder.append(ls);

    }

    return stringBuilder.toString();

  }

}