package app;

import dataProcessor.ReviewProcessor;
public class InitDataLoad {
	
	public InitDataLoad(String fileName) {
		ReviewProcessor proc = new ReviewProcessor(fileName);
		proc.index();
	}
}
