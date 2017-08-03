package objects;

import java.util.ArrayList;
import java.util.List;

public class ResponseReview {
	
	public QueryDetails responseSummary;
	public List<JsonReview> data;
	
	public ResponseReview() {
		responseSummary = new QueryDetails();
		data = new ArrayList<>();
	}
	
	public ResponseReview(QueryDetails qd) {
		responseSummary = qd;
		data = new ArrayList<>();
	}
	
	public QueryDetails getResponseSummary() {
		return responseSummary;
	}
	
	public List<JsonReview> getData() {
		return data;
	}
	
	public void setResponseSummary(QueryDetails responseSummary) {
		this.responseSummary = responseSummary;
	}
	
	public void setData(List<JsonReview> data) {
		this.data = data;
	}
	
}
