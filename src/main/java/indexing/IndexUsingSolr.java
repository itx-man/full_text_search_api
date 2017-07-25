package main.java.indexing;

import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.PropertyConfigurator;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

public class IndexUsingSolr {
		
	SolrClient solrClient; 
	String host;
	String port;
	String core;
	
	public IndexUsingSolr(String host, String port, String core) {
		this.host = host;
		this.port = port;
		this.core = core;
		
		String corePath = "http://" + host + ":" + port + "/solr/" + core;
		solrClient = new HttpSolrClient.Builder(corePath).build();
		
		String log4jConfPath = main.java.constants.Constants.log4jConfPath;
		PropertyConfigurator.configure(log4jConfPath);
	}
	
	public void addDocument(SolrInputDocument doc) throws SolrServerException, IOException {
		solrClient.add(doc);
	}
	
	public void addDocument(Collection<SolrInputDocument> c) throws SolrServerException, IOException {
		solrClient.add(c);
	}
	
	public void commit() throws SolrServerException, IOException {
		solrClient.commit();
	}
	
	public void close() throws IOException {
		solrClient.close();
	}

}
