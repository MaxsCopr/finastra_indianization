package in.co.FIRC.vo;

public class AuditVO
{
  private String reference_nubmer;
  private String irmType;
  private String audit_by;
  public String getReference_nubmer()
  {
    return this.reference_nubmer;
  }
  public void setReference_nubmer(String reference_nubmer)
  {
    this.reference_nubmer = reference_nubmer;
  }
  public String getIrmType()
  {
    return this.irmType;
  }
  public void setIrmType(String irmType)
  {
    this.irmType = irmType;
  }
  public String getAudit_by()
  {
    return this.audit_by;
  }
  public void setAudit_by(String audit_by)
  {
    this.audit_by = audit_by;
  }
}