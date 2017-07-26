package main.java.searching;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class SearchUsingLucene {

	IndexReader reader; 
	IndexSearcher searcher;
	
	public SearchUsingLucene(String indexPath) {
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	    searcher = new IndexSearcher(reader);
	}
	
	public Document getDoc(int docID) {
		try {
			return searcher.doc(docID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("finally")
	public List<ScoreDoc> searchTerm(String field, String termText, int limit, float boostValue) {
		
		TermQuery tq = new TermQuery(new Term(field, termText));
		TopDocs results = null;
		ScoreDoc[] hits = null;
		try {
			results = searcher.search(tq, limit);
			hits = results.scoreDocs;			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			List<ScoreDoc> resultSet = new ArrayList<ScoreDoc>();
			for (ScoreDoc s: hits)
				resultSet.add(s);
			
			boostScores(resultSet, boostValue);
			
			return resultSet;
		}
	}
	
	public List<ScoreDoc> andResults(List<ScoreDoc> r1, List<ScoreDoc> r2) {
		
		Map<Integer, Float> m = new HashMap<>();
		List<ScoreDoc> resultSet = new ArrayList<>();
		
		if (r1.size() > r2.size()) {
			List<ScoreDoc> t = r1;
			r1 = r2;
			r2 = t;
		}
		
		
		for (int i=0; i<r1.size(); i++) {
			m.put(r1.get(i).doc, r1.get(i).score);
		}
		
		for (int i=0; i<r2.size(); i++) {
			if (m.containsKey(r2.get(i).doc)) {
				ScoreDoc match = r2.get(i);
				match.score += m.get(r2.get(i).doc);
				resultSet.add(match);
			}
		}
		
		Collections.sort(resultSet, Collections.reverseOrder(new Comparator<ScoreDoc>() {
			@Override
			public int compare(ScoreDoc s1, ScoreDoc s2) {
				if (s1.score < s2.score)
					return -1;
				else if (s1.score > s2.score)
					return 1;
				else
					return 0;
			
			}
		}));
		
		return resultSet;
	}
	
	public void boostScores(List<ScoreDoc> hits, float boostValue) {
		for (ScoreDoc doc : hits) {
			doc.score *= boostValue;
		}
	}
	
}
