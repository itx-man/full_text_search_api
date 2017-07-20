package main.java.indexing;

public class AmazonReview {

	String reviewerID;
	String reviewerName;
	String reviewText;
	String reviewSummary;
	Long reviewTime;				
	Double rating;
	
	public AmazonReview(String reviewerID, String reviewerName, String reviewText, String reviewSummary, Long reviewTime, Double rating) {
		this.reviewerID = reviewerID;
		this.reviewerName = reviewerName;
		this.reviewText = reviewText;
		this.reviewSummary = reviewSummary;
		this.reviewTime = reviewTime;
		this.rating = rating;
	}
	
}
