package objects;

public class Result {

	String docID;
	String rID;
	String rName;
	String rSummary;
	String rText;
	String rTime;				
	String rStars;
	
	public Result (String docID, String rID, String rName, String rSummary, String rText, String rTime, String rStars) {
		this.docID = docID;
		this.rID = rID;
		this.rName = rName;
		this.rSummary = rSummary;
		this.rText = rText;
		this.rTime = rTime;
		this.rStars = rStars;
	}

	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}

	public String getrID() {
		return rID;
	}

	public void setrID(String rID) {
		this.rID = rID;
	}

	public String getrName() {
		return rName;
	}

	public void setrName(String rName) {
		this.rName = rName;
	}

	public String getrSummary() {
		return rSummary;
	}

	public void setrSummary(String rSummary) {
		this.rSummary = rSummary;
	}

	public String getrText() {
		return rText;
	}

	public void setrText(String rText) {
		this.rText = rText;
	}

	public String getrTime() {
		return rTime;
	}

	public void setrTime(String rTime) {
		this.rTime = rTime;
	}

	public String getrStars() {
		return rStars;
	}

	public void setrStars(String rStars) {
		this.rStars = rStars;
	}
	
	
}
