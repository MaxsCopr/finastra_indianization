package in.co.prishipment.util;
 
import java.util.HashMap;

import java.util.Map;
 
public class MapTokenResolver

  implements ITokenResolver

{

  protected Map<String, String> tokenMap = new HashMap();

  public MapTokenResolver(Map<String, String> tokenMap)

  {

    this.tokenMap = tokenMap;

  }

  public String resolveToken(String tokenName)

  {

    return (String)this.tokenMap.get(tokenName);

  }

}