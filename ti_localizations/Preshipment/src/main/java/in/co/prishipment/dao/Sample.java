package in.co.prishipment.dao;
 
import java.io.PrintStream;

import java.util.Arrays;

import java.util.Collection;

import java.util.HashSet;
 
public class Sample

{

  public static void main(String[] args)

  {

    Collection<String> listOne = Arrays.asList(new String[] { "milan", "iga", "dingo", "iga", "elpha", "iga", "hafil", "iga", "meat", "iga", "neeta.peeta", "iga" });

    Collection<String> listTwo = Arrays.asList(new String[] { "hafil", "iga", "binga", "mike", "dingo", "dingo", "dingo" });

    Collection<String> similar = new HashSet(listOne);

    Collection<String> different = new HashSet();

    different.addAll(listOne);

    different.addAll(listTwo);

    similar.retainAll(listTwo);

    different.removeAll(similar);

    System.out.printf("One:%s%nTwo:%s%nSimilar:%s%nDifferent:%s%n", new Object[] { listOne, listTwo, similar, different });

  }

}