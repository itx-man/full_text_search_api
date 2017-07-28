package objects;

public class Review {

	public String docID;
	public String rID;
	public String rName;
	public String rSummary;
	public String rText;
	public Long rTime;				
	public Double rStars;
	
	public Review(String docID, String rID, String rName, String rSummary, String rText, Long rTime, Double rStars) {
		this.docID = docID;
		this.rID = rID;
		this.rName = rName;
		this.rSummary = rSummary;
		this.rText = rText;
		this.rTime = rTime;
		this.rStars = rStars;
	}
	
}
