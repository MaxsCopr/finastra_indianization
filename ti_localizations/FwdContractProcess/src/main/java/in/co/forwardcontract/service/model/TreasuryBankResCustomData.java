package in.co.forwardcontract.service.model;

public class TreasuryBankResCustomData
{
  private String Message;
  private String successorfailure;
  private TreasuryBankResCustomDataDetails StatementTransactionDetail;
  public String getMessage()
  {
    return this.Message;
  }
  public void setMessage(String message)
  {
    this.Message = message;
  }
  public String getSuccessorfailure()
  {
    return this.successorfailure;
  }
  public void setSuccessorfailure(String successorfailure)
  {
    this.successorfailure = successorfailure;
  }
  public TreasuryBankResCustomDataDetails getStatementTransactionDetail()
  {
    return this.StatementTransactionDetail;
  }
  public void setStatementTransactionDetail(TreasuryBankResCustomDataDetails statementTransactionDetail)
  {
    this.StatementTransactionDetail = statementTransactionDetail;
  }
}
