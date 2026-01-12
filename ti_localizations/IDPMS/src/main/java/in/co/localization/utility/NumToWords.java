package in.co.localization.utility;
 
class NumToWords
{
  String[] st4 = { "Twenty", "Thirty", "Fourty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninty" };
  String[] st3 = { "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Ninteen" };
  String[] st2 = { "Hundred", "Thousand", "Lac", "Crore" };
  String[] st1 = { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine" };
  String string;
  public String convert(int number)
  {
    int n = 1;
    this.string = "";
    while (number != 0)
    {
      switch (n)
      {     
      case 1: 
        int word = number % 100;
        pass(word);
        if ((number > 100) && (number % 100 != 0)) {
          show("and ");
        }
        number /= 100;
        break;
      case 2: 
        int word = number % 10;
        if (word != 0)
        {
          show(" ");
          show(this.st2[0]);
          show(" ");
          pass(word);
        }
        number /= 10;
        break;
      case 3: 
        int word = number % 100;
        if (word != 0)
        {
          show(" ");
          show(this.st2[1]);
          show(" ");
          pass(word);
        }
        number /= 100;
        break;
      case 4: 
        int word = number % 100;
        if (word != 0)
        {
          show(" ");
          show(this.st2[2]);
          show(" ");
          pass(word);
        }
        number /= 100;
        break;
      case 5: 
        int word = number % 100;
        if (word != 0)
        {
          show(" ");
          show(this.st2[3]);
          show(" ");
          pass(word);
        }
        number /= 100;
      }
      n++;
    }
    return this.string;
  }
  public void pass(int number)
  {
    if (number < 10) {
      show(this.st1[number]);
    }
    if ((number > 9) && (number < 20)) {
      show(this.st3[(number - 10)]);
    }
    if (number > 19)
    {
      int word = number % 10;
      if (word == 0)
      {
        int q = number / 10;
        show(this.st4[(q - 2)]);
      }
      else
      {
        int q = number / 10;
        show(this.st1[word]);
        show(" ");
        show(this.st4[(q - 2)]);
      }
    }
  }
  public void show(String s)
  {
    String st = this.string;
    this.string = s;
    this.string += st;
  }
}