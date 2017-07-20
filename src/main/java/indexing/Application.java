package main.java.indexing;

public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ReviewProcessor json = new ReviewProcessor("rev.json");
		json.readAndIndex();
		System.out.println("Finished Indexing reviews");
	}

}
