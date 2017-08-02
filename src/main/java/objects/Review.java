package objects;

public class Review {

	String docID;
	String rID;
	String rName;
	String rSummary;
	String rText;
	Long rTime;				
	Double rStars;
	
	public Review(String docID, String rID, String rName, String rSummary, String rText, Long rTime, Double rStars) {
		this.docID = docID;
		this.rID = rID;
		this.rName = rName;
		this.rSummary = rSummary;
		this.rText = rText;
		this.rTime = rTime;
		this.rStars = rStars;
	}
	
	public String getrDocID() {
		return docID;
	}
	
	public String getrID() {
		return rID;
	}
	
	public String getrName() {
		return rName;
	}
	
	public String getrSummary() {
		return rSummary;
	}
	
	public String getrText() {
		return rText;
	}
	
	public Long getrTime() {
		return rTime;
	}
	
	public Double getrStars() {
		return rStars;
	}
	
}
