package in.co.localization.utility;
 
public class ComputingLocation

{

  public static void computeDistanceAndBearing(double lat1, double lon1, double lat2, double lon2, double[] results)

  {

    int MAXITERS = 20;

    lat1 *= 0.0174532925199433D;

    lat2 *= 0.0174532925199433D;

    lon1 *= 0.0174532925199433D;

    lon2 *= 0.0174532925199433D;

    double a = 6378137.0D;

    double b = 6356752.3141999999D;

    double f = (a - b) / a;

    double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);

    double L = lon2 - lon1;

    double A = 0.0D;

    double U1 = Math.atan((1.0D - f) * Math.tan(lat1));

    double U2 = Math.atan((1.0D - f) * Math.tan(lat2));

    double cosU1 = Math.cos(U1);

    double cosU2 = Math.cos(U2);

    double sinU1 = Math.sin(U1);

    double sinU2 = Math.sin(U2);

    double cosU1cosU2 = cosU1 * cosU2;

    double sinU1sinU2 = sinU1 * sinU2;

    double sigma = 0.0D;

    double deltaSigma = 0.0D;

    double cosSqAlpha = 0.0D;

    double cos2SM = 0.0D;

    double cosSigma = 0.0D;

    double sinSigma = 0.0D;

    double cosLambda = 0.0D;

    double sinLambda = 0.0D;

    double lambda = L;

    for (int iter = 0; iter < MAXITERS; iter++)

    {

      double lambdaOrig = lambda;

      cosLambda = Math.cos(lambda);

      sinLambda = Math.sin(lambda);

      double t1 = cosU2 * sinLambda;

      double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;

      double sinSqSigma = t1 * t1 + t2 * t2;

      sinSigma = Math.sqrt(sinSqSigma);

      cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda;

      sigma = Math.atan2(sinSigma, cosSigma);

      double sinAlpha = sinSigma == 0.0D ? 0.0D : cosU1cosU2 * sinLambda / 

        sinSigma;

      cosSqAlpha = 1.0D - sinAlpha * sinAlpha;

      cos2SM = cosSqAlpha == 0.0D ? 0.0D : cosSigma - 2.0D * sinU1sinU2 / 

        cosSqAlpha;

      double uSquared = cosSqAlpha * aSqMinusBSqOverBSq;

      A = 1.0D + uSquared / 16384.0D * (

        4096.0D + uSquared * (-768.0D + uSquared * (320.0D - 175.0D * uSquared)));

      double B = uSquared / 1024.0D * (

        256.0D + uSquared * (-128.0D + uSquared * (74.0D - 47.0D * uSquared)));

      double C = f / 16.0D * cosSqAlpha * (4.0D + f * (4.0D - 3.0D * cosSqAlpha));

      double cos2SMSq = cos2SM * cos2SM;

      deltaSigma = B * 

        sinSigma * (

        cos2SM + B / 4.0D * (

        cosSigma * (-1.0D + 2.0D * cos2SMSq) - B / 6.0D * cos2SM * (

        -3.0D + 4.0D * sinSigma * sinSigma) * (

        -3.0D + 4.0D * cos2SMSq)));

      lambda = L + 

        (1.0D - C) * 

        f * 

        sinAlpha * (

        sigma + C * sinSigma * (

        cos2SM + C * cosSigma * (-1.0D + 2.0D * cos2SM * cos2SM)));

      double delta = (lambda - lambdaOrig) / lambda;

      if (Math.abs(delta) < 1.0E-012D) {

        break;

      }

    }

    double distance = b * A * (sigma - deltaSigma);

    results[0] = distance;

    if (results.length > 1)

    {

      double initialBearing = Math.atan2(cosU2 * sinLambda, cosU1 * sinU2 - 

        sinU1 * cosU2 * cosLambda);

      initialBearing *= 57.295779513082323D;

      results[1] = initialBearing;

      if (results.length > 2)

      {

        double finalBearing = Math.atan2(cosU1 * sinLambda, -sinU1 * cosU2 + 

          cosU1 * sinU2 * cosLambda);

        finalBearing *= 57.295779513082323D;

        results[2] = finalBearing;

      }

    }

  }

  public static void computeDestinationAndBearing(double lat1, double lon1, double brng, double dist, double[] results)

  {

    double a = 6378137.0D;double b = 6356752.3141999999D;double f = 0.003352810664747481D;

    double s = dist;

    double alpha1 = toRad(brng);

    double sinAlpha1 = Math.sin(alpha1);

    double cosAlpha1 = Math.cos(alpha1);

    double tanU1 = (1.0D - f) * Math.tan(toRad(lat1));

    double cosU1 = 1.0D / Math.sqrt(1.0D + tanU1 * tanU1);double sinU1 = tanU1 * cosU1;

    double sigma1 = Math.atan2(tanU1, cosAlpha1);

    double sinAlpha = cosU1 * sinAlpha1;

    double cosSqAlpha = 1.0D - sinAlpha * sinAlpha;

    double uSq = cosSqAlpha * (a * a - b * b) / (b * b);

    double A = 1.0D + uSq / 16384.0D * (

      4096.0D + uSq * (-768.0D + uSq * (320.0D - 175.0D * uSq)));

    double B = uSq / 1024.0D * (256.0D + uSq * (-128.0D + uSq * (74.0D - 47.0D * uSq)));

    double sinSigma = 0.0D;double cosSigma = 0.0D;double deltaSigma = 0.0D;double cos2SigmaM = 0.0D;

    double sigma = s / (b * A);double sigmaP = 6.283185307179586D;

    while (Math.abs(sigma - sigmaP) > 1.0E-012D)

    {

      cos2SigmaM = Math.cos(2.0D * sigma1 + sigma);

      sinSigma = Math.sin(sigma);

      cosSigma = Math.cos(sigma);

      deltaSigma = B * 

        sinSigma * (

        cos2SigmaM + B / 

        4.0D * (

        cosSigma * (-1.0D + 2.0D * cos2SigmaM * cos2SigmaM) - B / 6.0D * 

        cos2SigmaM * (-3.0D + 4.0D * sinSigma * sinSigma) * (

        -3.0D + 4.0D * cos2SigmaM * cos2SigmaM)));

      sigmaP = sigma;

      sigma = s / (b * A) + deltaSigma;

    }

    double tmp = sinU1 * sinSigma - cosU1 * cosSigma * cosAlpha1;

    double lat2 = Math.atan2(sinU1 * cosSigma + cosU1 * sinSigma * cosAlpha1, 

      (1.0D - f) * Math.sqrt(sinAlpha * sinAlpha + tmp * tmp));

    double lambda = Math.atan2(sinSigma * sinAlpha1, cosU1 * cosSigma - sinU1 * 

      sinSigma * cosAlpha1);

    double C = f / 16.0D * cosSqAlpha * (4.0D + f * (4.0D - 3.0D * cosSqAlpha));

    double L = lambda - 

      (1.0D - C) * 

      f * 

      sinAlpha * (

      sigma + C * sinSigma * (

      cos2SigmaM + C * cosSigma * (-1.0D + 2.0D * cos2SigmaM * cos2SigmaM)));

    double lon2 = (toRad(lon1) + L + 9.424777960769379D) % 6.283185307179586D - 3.141592653589793D;

 
 
    double revAz = Math.atan2(sinAlpha, -tmp);

    results[0] = toDegrees(lat2);

    results[1] = toDegrees(lon2);

    results[2] = toDegrees(revAz);

  }

  private static double toRad(double angle)

  {

    return angle * 3.141592653589793D / 180.0D;

  }

  private static double toDegrees(double radians)

  {

    return radians * 180.0D / 3.141592653589793D;

  }

}