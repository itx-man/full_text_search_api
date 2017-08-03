package search;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;

import objects.QueryDetails;

public class QueryParser {
	SearchUsingLucene searcher;
	
	public QueryParser(String indexName) {
		//TODO Add code to handle index names that do not exist in the file system
		searcher = new SearchUsingLucene(constants.Constants.indexPath + indexName);
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
	
	public List<ScoreDoc> getResults(QueryDetails qd, String q, String r, int resultSize, int start) {
		List<ScoreDoc> result = searcher.andResults(this.processTermFilters(q, Integer.MAX_VALUE), 
				this.processRangeFilters(r, Integer.MAX_VALUE), Integer.MAX_VALUE);
		
		qd.setTotalMatchedDocuments(result.size());
		qd.setStartIndex(start);
		if (result.size() < start) {
			qd.setReturnedDocuments(0);
			return new ArrayList<ScoreDoc>();
		}
		else {
			qd.setReturnedDocuments(Math.min(result.size() - start, resultSize));
			return result.subList(start, Math.min(result.size(), start + resultSize));
		}
			
	}
	
	public List<ScoreDoc> processTermFilters(String q, int resultSize) {
		String[] tokens = q.split("=");
		List<Query> queries = new ArrayList<>();
		List<Integer> boostValues = new ArrayList<>();
		
		if (tokens.length <= 1 || tokens.length%2 == 1)
			return new ArrayList<ScoreDoc>();
		
		for (int i=0; i< tokens.length; i++) {
			String field = tokens[i++];
	
			String[] v = tokens[i].split(" ");
			for (String vv : v) {
				StringBuilder termText = new StringBuilder("");
				String[] t = vv.split("%B");
				if (t.length > 1) {
					boostValues.add(Integer.valueOf(t[t.length-1]));
					for (int j=0; j<t.length-1; j++) {
						termText.append(t[j]);
					}
				}
				else {
					boostValues.add(1);
					for (int j=0; j<t.length; j++) {
						termText.append(t[j]);
					}
				}
				Query quer = this.buildTermQuery(field, termText.toString());
				queries.add(quer);
			}
		}
		
		return searcher.orQueries(queries, boostValues, resultSize);
	}
	
	
	public List<ScoreDoc> processRangeFilters(String r, int resultSize) {
		String[] tokens = r.split("=");
		List<Query> queries = new ArrayList<>();
		List<Integer> boostValues = new ArrayList<>();
		
		if (tokens.length <= 1 || tokens.length%2 == 1)
			return new ArrayList<ScoreDoc>();
		
		for (int i=0; i< tokens.length; i++) {
			String field = tokens[i++];
			String value[] = tokens[i].split(" ");
			
			for (String t: value) {
				int boost = 1;
				String[] v = t.split("%B");
				
				if (v.length > 1) {
					boost = Integer.valueOf(v[v.length-1]);
				}
				
				String[] range = v[0].split(":");
				try {
					long llow = Long.parseLong(range[0]);
					long lhigh = Long.parseLong(range[1]);
					queries.add(this.buildLongRangeQuery(field, llow, lhigh));
					boostValues.add(boost);
				}
				catch (Exception e) {
					//Do Nothing
				}
				
				try {
					Double dlow = Double.parseDouble(range[0]);
					Double dhigh = Double.parseDouble(range[1]);
					queries.add(this.buildDoubleRangeQuery(field, dlow, dhigh));
					boostValues.add(boost);
				}
				catch (Exception e) {
					//Do Nothing
				}
			}
		}
		
		return searcher.orQueries(queries, boostValues, resultSize);
	}
	
	
	public List<ScoreDoc> runRangeSearchQuery(String q, int limit) {

		SearchUsingLucene searcher = new SearchUsingLucene(constants.Constants.reviewIndexPath);
		String[] tokens = q.split("=");
		List<Query> queries = new ArrayList<>();
		List<List<ScoreDoc>> partial = new ArrayList<>();

		if (tokens.length <= 1)
			return null;

		for (int i=0; i< tokens.length; i++) {

			String field = tokens[i++];
			String[] value = tokens[i].split(":");

			Query query1 = searcher.buildDoubleRangeQuery(field, Double.parseDouble(value[0]), Double.parseDouble(value[1]));
			queries.add(query1);

			Query query2 = searcher.buildLongRangeQuery(field, Long.parseLong(value[0]), Long.parseLong(value[1]));
			queries.add(query2);

			partial.add(searcher.orQueries(queries));
			queries.clear();
		}

		return searcher.andResults(partial, limit);
	}

}
