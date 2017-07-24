package main.java.searching;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
public class LuceneSearch {

	public static void main(String[] args) throws IOException, ParseException {
		
		String indexPath = main.java.constants.Constants.indexPath;
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
	    IndexSearcher searcher = new IndexSearcher(reader);
	    Analyzer analyzer = new StandardAnalyzer();
		String queryString = "game of thrones";
		QueryParser qp = new QueryParser("rText", analyzer);
		Query q = qp.parse(queryString);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
		int hitsPerPage = 3;
		boolean raw = false;
		doPagingSearch(in, searcher, q, hitsPerPage, raw, true);
		reader.close();
	}
		
	public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, 
            int hitsPerPage, boolean raw, boolean interactive) throws IOException {

		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;
		
		int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");
		
		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);
		
		while (true) {
			if (end > hits.length) {
				System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
				System.out.println("Collect more (y/n) ?");
				String line = in.readLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}
			
				hits = searcher.search(query, numTotalHits).scoreDocs;	
			}
		
			end = Math.min(hits.length, start + hitsPerPage);
			
			for (int i = start; i < end; i++) {
				if (raw) {                              // output raw format
					System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
					continue;
				}
				
				Document doc = searcher.doc(hits[i].doc);
				System.out.println(doc.get("rSummary"));
				System.out.println(doc.get("rText"));
				System.out.println("---------------------");
			}
		
			if (!interactive || end == 0) {
				break;
			}
		
			if (numTotalHits >= end) {
				boolean quit = false;
				while (true) {
					System.out.print("Press ");
					if (start - hitsPerPage >= 0) {
						System.out.print("(p)revious page, ");  
					}
					if (start + hitsPerPage < numTotalHits) {
						System.out.print("(n)ext page, ");
					}
					System.out.println("(q)uit or enter number to jump to a page.");
		
					String line = in.readLine();
					if (line.length() == 0 || line.charAt(0)=='q') {
						quit = true;
						break;
					}
					if (line.charAt(0) == 'p') {
						start = Math.max(0, start - hitsPerPage);
						break;
					} else if (line.charAt(0) == 'n') {
						if (start + hitsPerPage < numTotalHits) {
							start+=hitsPerPage;
						}
						break;
					} else {
						int page = Integer.parseInt(line);
						if ((page - 1) * hitsPerPage < numTotalHits) {
							start = (page - 1) * hitsPerPage;
							break;
						} else {
							System.out.println("No such page");
						}
					}
				}
				
				if (quit) 
					break;
				
				end = Math.min(numTotalHits, start + hitsPerPage);
			}
		}
	}
}
