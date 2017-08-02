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
import objects.Result;

@RestController
public class SearchReviews {
	
	@RequestMapping("/search/reviews")
	public List<Result> search(@RequestParam(value="q", defaultValue="") String q, @RequestParam(value="r", defaultValue="rStars=1:5") String r) {
		
		SearchUsingLucene searcher = new SearchUsingLucene(constants.Constants.indexPath + "reviews");
		QueryParser qp = new QueryParser("reviews");
		System.out.println("q=" + q);
		System.out.println("r=" + r);
		List<ScoreDoc> list = qp.getResults(q, r, 25);
		
		List<Result> l = new ArrayList<>();
		
		for (int i=0; i<list.size(); i++) {
			Document doc = searcher.getDoc(list.get(i).doc);
			l.add(new Result(doc.get("docID"), doc.get("rID"), doc.get("rName"),
					doc.get("rSummary"), doc.get("rText"), doc.get("rTime"), doc.get("rStars")));
		}
			
		return l;
			
	}

}
