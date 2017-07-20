package main.java.indexing;

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
	
}
