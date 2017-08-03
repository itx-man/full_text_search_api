package objects;

public class QueryDetails {

	public int totalMatchedDocuments;
	public int returnedDocuments;
	public int startIndex;
	
	public QueryDetails() {
		
	}

	public int getTotalMatchedDocuments() {
		return totalMatchedDocuments;
	}

	public void setTotalMatchedDocuments(int totalMatchedDocuments) {
		this.totalMatchedDocuments = totalMatchedDocuments;
	}

	public int getReturnedDocuments() {
		return returnedDocuments;
	}

	public void setReturnedDocuments(int returnedDocuments) {
		this.returnedDocuments = returnedDocuments;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
}
