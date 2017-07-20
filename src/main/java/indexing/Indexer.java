package main.java.indexing;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {

	Directory dir; 
	Analyzer analyzer;
	IndexWriterConfig iwc;
	IndexWriter writer;
	
	public Indexer() {
		initializeWriter();
	}
	
	public void initializeWriter() {
		try {
			dir = FSDirectory.open(Paths.get(main.java.constants.Constants.indexPath));
		    analyzer = new StandardAnalyzer();
		    iwc = new IndexWriterConfig(analyzer);
		    iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		    writer = new IndexWriter(dir, iwc);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void indexDoc(AmazonReview r) throws IOException {
		
		Document doc = new Document();
		doc.add(new StringField("reviewerID", r.reviewerID, Field.Store.YES));
		doc.add(new StringField("reviewerName", r.reviewerName, Field.Store.YES));
		doc.add(new TextField("reviewSummary", r.reviewSummary, Field.Store.YES));
		doc.add(new TextField("reviewText", r.reviewText, Field.Store.YES));
		doc.add(new LongPoint("reviewTime", r.reviewTime));
		doc.add(new DoublePoint("rating", r.rating));
		
		writer.updateDocument(new Term("review", r.reviewerID+r.reviewTime.toString()), doc);
		
	}
	
	public void closeWriter() {
		try {
			writer.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
