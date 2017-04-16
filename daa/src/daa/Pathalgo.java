package daa;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Map;

public class Pathalgo {
	static Map<String,distribution> map=new Hashtable<String,distribution>();
	public static void main(String[] args) throws Exception {
	    BufferedReader br = new BufferedReader(new FileReader("C:/Users/nicen/workspace/daa/src/input2.txt"));
	    String line = null;
	    int totalVertices = 0,sourceName=0,destinationName=0;
	    String[] inputValues;
	    
	    boolean firstline =true;
	    while ((line = br.readLine()) != null) {
	      inputValues = line.split(",");	      
	      if(firstline){
	    	  totalVertices=Integer.parseInt(inputValues[0].trim());
	    	  sourceName=Integer.parseInt(inputValues[1].trim());
	    	  destinationName=Integer.parseInt(inputValues[2].trim());
    	      firstline=false;
    	  }
    	  else{
    		  double mean=0,variance=0,csquare=0;
    		  int type=Integer.parseInt(inputValues[3].trim());
    		  double alpha=Double.parseDouble(inputValues[4].trim());
    		  double beta=Double.parseDouble(inputValues[5].trim());
    		  if(type==1){
	    			  mean=alpha;
	    			  variance=0;
	    			  csquare=0;	    			  
    		  }
    		  else if(type==2){
    			  mean=(alpha+beta)/2;
    			  variance=Math.pow(beta-alpha, 2)/12;
    			  csquare=Math.pow((beta-alpha)/(alpha+beta), 2)/3;   					      			  
    		  }
    		  else if(type==3){
    			  mean=1/alpha;
    			  variance=1/(Math.pow(alpha, 2));
    			  csquare=1;
    		  }
    		  else if(type==4){
    			  mean=beta+(1/alpha);
    			  variance=1/(alpha*alpha);
    			  csquare=Math.pow(1/(1+(beta*alpha)), 2);    			
    		  }
    		  else if(type==5){
    			  mean=alpha;
    			  variance=beta;
    			  csquare=beta/(alpha*alpha);
    		  }
    		  else if(type==6){
    			  mean=alpha;
    			  variance=alpha*alpha*beta;
    			  csquare=beta;
    		  }
    		  else{
    			  
    			  System.out.println("The type of distribution is not mentioned");
    		  }
    		  int vertex1=Integer.parseInt(inputValues[1].trim());
    		  int vertex2=Integer.parseInt(inputValues[2].trim());
    		  String key1=vertex1+""+vertex2;
    		  map.put(key1, new distribution(vertex1,vertex2,mean,variance,csquare));
    		  String key2=vertex2+""+vertex1;
    		  map.put(key2, new distribution(vertex2,vertex1,mean,variance,csquare));
    		  
    	  }
	      
	    }	    
	    br.close();
	    //print distribution values
	    /*
	    for(Map.Entry<String, distribution> entry:map.entrySet()){
	    	distribution d=entry.getValue();
	    	System.out.println(d.vertex1+" "+d.vertex2+" "+d.mean+" "+d.variance+" "+d.csquare);
	    }
	   */
	    //input to dikjstra
	 
	    computeAll(totalVertices,sourceName,destinationName,Criteria.MEAN_VALUE);
	    computeAll(totalVertices,sourceName,destinationName,Criteria.OPTIMIST);
	    computeAll(totalVertices,sourceName,destinationName,Criteria.PESSIMIST);
	    computeAll(totalVertices,sourceName,destinationName,Criteria.DOUBLY_PESS);
	    computeAll(totalVertices,sourceName,destinationName,Criteria.STABLE);
	    //call another method with own criteria
	    
	    
	  }
	  public static void computeAll(int totalVertices,int sourceName,int destinationName,Criteria criteria){
		  DikjstraAlgorithm.Graph graph=new DikjstraAlgorithm.Graph(totalVertices+1); 
		    for (int i = 1; i < (totalVertices+1); i++) {
	            graph.addVertex(i);
	        }
		    System.out.println("///"+criteria+"///");
		    for(Map.Entry<String, distribution> entry:map.entrySet()){    
		        distribution d=entry.getValue();
		        double weight=0;
		        double sd=Math.sqrt(d.variance);		        
		        if(criteria==Criteria.MEAN_VALUE)
		        {
		        	weight=d.mean;
		        }
		        else if(criteria==Criteria.OPTIMIST){		        	
		        	weight=d.mean-sd;
		        }
		        else if(criteria==Criteria.PESSIMIST){
		        	weight=d.mean+sd;
		        }
		        else if(criteria==Criteria.DOUBLY_PESS){
		        	weight=d.mean+(2*sd);
		        }
		        else if(criteria==Criteria.STABLE){
		        	weight=d.csquare;
		        }
		        //System.out.println(""+d.vertex1+" ," + d.vertex2 +","+ weight);
		        graph.addEdge(d.vertex1, d.vertex2, weight);	        
		    }
		    String path=graph.findShortestPaths(sourceName,destinationName);
		    String edges[]=path.split(";");
		    int hops=edges.length-1;
		    double pathMean=0,pathVariance=0,pathCsquare=0;
		    for(int i=0;i<edges.length;i++){
		    	String Keys[]=edges[i].split(",");
		    	distribution d=map.get(Keys[0]+Keys[1]);
		    	
		    	pathMean=pathMean+d.mean;
		    	pathVariance=pathVariance+d.variance;
		    	pathCsquare=pathCsquare+d.csquare;
		    	
		    }
		    double standardDeviation=Math.sqrt(pathVariance);
		    System.out.println("expected value-standard deviation: "+ (pathMean-standardDeviation));
		    System.out.println("expected value: "+pathMean);
		    System.out.println("expected value+standard deviation: "+ (pathMean+standardDeviation));
		    System.out.println("expected value+2*standard deviation: "+ (pathMean+(2*standardDeviation)));
		    System.out.println("Csquare: "+pathCsquare);
		    System.out.println("hops: "+hops);  
	  }
	  public enum Criteria{
		  MEAN_VALUE,OPTIMIST,PESSIMIST,DOUBLY_PESS,STABLE,OWN
	  }
}