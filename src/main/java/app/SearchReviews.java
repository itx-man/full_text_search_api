package app;

import search.QueryParser;
import search.SearchUsingLucene;

import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import objects.JsonReview;
import objects.QueryDetails;
import objects.ResponseReview;

@RestController
public class SearchReviews {

	InitDataLoad idl = new InitDataLoad("bookReviews.json");
	
	@RequestMapping("/search/reviews")
	public ResponseReview search(@RequestParam(value="q", defaultValue="") String q, 
								 @RequestParam(value="r", defaultValue="rStars=1:5") String r, 
								 @RequestParam(value="rows", defaultValue="25") int maxRows,
								 @RequestParam(value="start", defaultValue="0") int start) {
		
		QueryDetails qd = new QueryDetails();
		SearchUsingLucene searcher = new SearchUsingLucene(constants.Constants.indexPath + "reviews");
		QueryParser qp = new QueryParser("reviews");
		
		List<ScoreDoc> result = qp.getResults(qd, q, r, maxRows, start);
		ResponseReview resp = new ResponseReview(qd);
		
		for (int i=0; i<result.size(); i++) {
			Document doc = searcher.getDoc(result.get(i).doc);
			resp.data.add(new JsonReview(doc.get("docID"), doc.get("rID"), doc.get("rName"),
					doc.get("rSummary"), doc.get("rText"), doc.get("rTime"), doc.get("rStars")));
		}
		
		return resp;
	}

}
