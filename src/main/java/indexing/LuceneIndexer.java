package main.java.indexing;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;

public class LuceneIndexer {
	
	IndexWriter iw;
	
	public LuceneIndexer() {
		//
	}
	
	public void initIndexWriter(String indexPath) {
		try {
			Directory idxDir = FSDirectory.open(Paths.get(indexPath)); 
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			iw = new IndexWriter(idxDir, iwc);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			iw.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addDocument(Document d) throws IOException {
		iw.addDocument(d);
	}

	public void updateDocument(Document d, Term t) throws IOException {
		iw.updateDocument(t, d);
	}
	
	public void commit() throws IOException {
		iw.commit();
	}

}
