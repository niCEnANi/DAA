package daa;

public class distribution {
	double mean,variance,csquare;
	int vertex1,vertex2;
	public distribution(int vertex1,int vertex2,double mean,double variance,double csquare){
		this.vertex1=vertex1;
		this.vertex2=vertex2;
		this.mean=mean;
		this.variance=variance;
		this.csquare=csquare;
	}
}
