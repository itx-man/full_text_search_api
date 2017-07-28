package search;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
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

	//---------- Build Queries --------------
	
	public Query buildTermQuery(String field, String termText) {
		Query tq = new TermQuery(new Term(field, termText));
		return tq;
	}
	
	public Query buildPhraseQuery(List<String> field, List<List<String>> terms) {
		PhraseQuery.Builder builder = new PhraseQuery.Builder();
		for (int i=0;i<field.size();i++) {
			for (int j=0;j<terms.get(i).size();j++) {
				builder.add(new Term(field.get(i), terms.get(i).get(j)));
			}
		}
		Query query = builder.build();
		return query;
	}
	
	public Query buildTermRangeQuery(String field, String lower, String upper, boolean includeLower, boolean includeUpper) {
		Query query = TermRangeQuery.newStringRange(field, lower, upper, includeLower, includeUpper);
		return query;
	}
	
	public Query buildDoubleRangeQuery(String field, Double lower, Double upper) {
		Query query = DoublePoint.newRangeQuery(field, lower, upper);
		return query;
	}
	
	public Query buildLongRangeQuery(String field, Long lower, Long upper) {
		Query query = LongPoint.newRangeQuery(field, lower, upper);
		return query;
	}
	
	public Query buildBooleanQuery(List<Query> queries, List<BooleanClause.Occur> clauses) {
		
		if (queries.size() != clauses.size())
			return null;
		
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		
		for (int i=0; i<queries.size(); i++) {
			builder.add(queries.get(i), clauses.get(i));
		}
		
		BooleanQuery query = builder.build();
		return query;
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
	
	
	//------------ Merge ResultSets Manually --------------
	
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
	
	public List<ScoreDoc> andResults(List<List<ScoreDoc>> r) {
		
		if (r.size() == 0)
			return new ArrayList<ScoreDoc>();
		else if (r.size() == 1) 
			return r.get(0);
		
		List<ScoreDoc> resultSet = r.get(0);
				
		for (int j=1; j<r.size(); j++) {
			resultSet = andResults(resultSet, r.get(j));
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
	
	public List<ScoreDoc> orResults(List<ScoreDoc> r1, List<ScoreDoc> r2) {
		
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
			m.put(r2.get(i).doc, m.getOrDefault(r2.get(i).doc, (float) 0) + r2.get(i).score);
		}
		
		for (Map.Entry<Integer, Float> e  : m.entrySet()) {
			ScoreDoc s = new ScoreDoc(e.getKey(), e.getValue());
			resultSet.add(s);
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

	public List<ScoreDoc> orResults(List<List<ScoreDoc>> r) {
		
		if (r.size() == 0)
			return new ArrayList<ScoreDoc>();
		else if (r.size() == 1) 
			return r.get(0);
		
		List<ScoreDoc> resultSet = r.get(0);
				
		for (int j=1; j<r.size(); j++) {
			resultSet = orResults(resultSet, r.get(j));
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

	//-------- Boost Document Scores -------------
	
	public void boostScores(List<ScoreDoc> hits, float boostValue) {
		for (ScoreDoc doc : hits) {
			doc.score *= boostValue;
		}
	}
	
	//-------- Run Query & Return the results ---------

	public List<ScoreDoc> runQuery(Query query) {
		return runQuery(query, Integer.MAX_VALUE, 1);
	}

	public List<ScoreDoc> runQuery(Query query, int limitDocuments) {
		return runQuery(query, limitDocuments, 1);
	}
	
	@SuppressWarnings("finally")
	public List<ScoreDoc> runQuery(Query query, int limitDocuments, int boostValue) {
		TopDocs results = null;
		ScoreDoc[] hits = null;
		List<ScoreDoc> resultSet = null;
		try {
			results = searcher.search(query, limitDocuments);
			hits = results.scoreDocs;
			resultSet = new ArrayList<ScoreDoc>(Arrays.asList(hits));
			
			if (boostValue != 1)
				boostScores(resultSet, boostValue);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return resultSet;
		}
	}
	
	//--------- Print the documents in a ScoreDoc Array ---------
	
	public void printDocuments(List<ScoreDoc> hits, List<String> fields) {
		for (ScoreDoc d : hits) {
			for (String f : fields) {
				Document doc = this.getDoc(d.doc);
				System.out.println(f + ": " + doc.get(f));
			}
			System.out.println("--------------------------");
		}
	}
	
}
