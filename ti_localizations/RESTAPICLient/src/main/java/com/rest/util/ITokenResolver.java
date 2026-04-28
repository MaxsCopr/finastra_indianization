package com.rest.util;

public abstract interface ITokenResolver
{
  public abstract String resolveToken(String paramString);
}
