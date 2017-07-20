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
	
	public LuceneIndexer() {
		//
	}
	
	@SuppressWarnings("finally")
	public IndexWriter getIndexWriter(String indexPath) {
		try {
			Directory idxDir = FSDirectory.open(Paths.get(indexPath)); 
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			IndexWriter iw = new IndexWriter(idxDir, iwc);
			return iw;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			return null;
		}
	}
	
	public void close(IndexWriter iw) {
		try {
			iw.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addDocument(IndexWriter iw, Document d) throws IOException {
		iw.addDocument(d);
	}

	public void upsertDocument(IndexWriter iw, Document d, Term t) throws IOException {
		iw.updateDocument(t, d);
	}
	
	public void commit(IndexWriter iw) throws IOException {
		iw.commit();
	}

}
