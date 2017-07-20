package main.java.indexing;

import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ReviewProcessor {
	
	String fileName;
	
	public ReviewProcessor(String fileName) {
		this.fileName = fileName;
	}
	
	public void readAndIndex() {
		JSONParser parser = new JSONParser();
		
		try {
			JSONArray jsonFile = (JSONArray) parser.parse(new FileReader(main.java.constants.Constants.filePath + fileName));
			
			for (Object o : jsonFile) {
				JSONObject review = (JSONObject) o;
				
				String reviewerID = (String) review.get("reviewerID");
				String reviewerName = (String) review.get("reviewerName");
				String reviewText = (String) review.get("reviewText");
				String reviewSummary = (String) review.get("summary");
				Long reviewTime = (Long) review.get("unixReviewTime");				
				Double rating = (Double) review.get("overall");
				
				//JSONArray h = (JSONArray) review.get("helpful");
				//Long helpful = (Long) h.get(0);
				//Long votes = (Long) h.get(1);

				AmazonReview azReview = new AmazonReview(reviewerID, reviewerName, reviewText, reviewSummary, reviewTime, rating);
				
 			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
}
