package app;

import dataProcessor.ReviewProcessor;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import search.SearchUsingLucene;

public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		ReviewProcessor proc = new ReviewProcessor("bookReviews.json");
		long start = System.nanoTime();
		proc.index();
		long stop = System.nanoTime();
		long total = (stop - start) / (long) Math.pow(10, 9);
		System.out.println("Indexed all Documents in " + (total) + " seconds.");
		
		SearchUsingLucene s = new SearchUsingLucene(constants.Constants.reviewIndexPath);
		
		List<List<ScoreDoc>> results = new ArrayList<>();
		
		results.add(s.searchTerm("rText","drama",Integer.MAX_VALUE, 1));
		results.add(s.searchTerm("rText","great",Integer.MAX_VALUE, 1));
		results.add(s.searchTerm("rText","thriller",Integer.MAX_VALUE, 1));
		List<ScoreDoc> a = s.orResults(results);
		
		List<ScoreDoc> r = s.orResults(a, s.searchTerm("rSummary","wow", Integer.MAX_VALUE, 1));
		
		
		for (int i=0; i<r.size() && i<10; i++) {
			Document d = s.getDoc(r.get(i).doc);
			
			System.out.println("Document: " + r.get(i).doc);
			System.out.println("Score: " + r.get(i).score);
			System.out.println("Summary: " + d.get("rSummary"));
			System.out.println("Text: " + d.get("rText"));
			System.out.println("--------------------");
			
		}
		
	}

}