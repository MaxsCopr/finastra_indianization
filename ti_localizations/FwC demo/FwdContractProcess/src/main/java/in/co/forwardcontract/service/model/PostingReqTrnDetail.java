package in.co.forwardcontract.service.model;


import java.util.List;

public class PostingReqTrnDetail
{
 private List<PostingReqPartTrnRec> PartTrnRec;
 public List<PostingReqPartTrnRec> getPartTrnRec()
 {
   return this.PartTrnRec;
 }
 public void setPartTrnRec(List<PostingReqPartTrnRec> partTrnRec)
 {
   this.PartTrnRec = partTrnRec;
 }
}