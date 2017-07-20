package main.java.indexing;

public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JsonProcessor jp = new JsonProcessor("bookReviews.json");
		jp.index();
		System.out.println("Indexed all Documents");
	}

}
