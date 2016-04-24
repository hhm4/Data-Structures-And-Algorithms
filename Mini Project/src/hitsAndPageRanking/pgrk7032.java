/* MOHAN HARINARAYANAN cs610 PP 7032 */
package hitsAndPageRanking;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
public class pgrk7032 {
	int noOfVertices;
	int noOfEdges;
	int []outdegree;
	float d;
	float dm;
	AdjacencyList []indegree;
	float []pageRankPrev;
	float []pageRank;
	
	public pgrk7032(int ver,int edg){
		this.noOfVertices=ver;
		this.noOfEdges=edg;
		this.d=0.85f;
		this.dm=(1.0f-d)/(float)this.noOfVertices;
		this.outdegree=new int[this.noOfVertices];
		this.pageRankPrev=new float[this.noOfVertices];
		this.pageRank=new float[this.noOfVertices];
		this.indegree=new AdjacencyList[this.noOfVertices];
		for (int i=0;i<this.noOfVertices;i++){
			this.outdegree[i]=0;
			this.indegree[i]=new AdjacencyList();
		}
	}
	
	public void initializeGraph7032(BufferedReader br) throws IOException{
		for (int i=0;i<this.noOfEdges;i++){
			String line = br.readLine();
			int ver1=Integer.parseInt(line.split(" ")[0]);
			int ver2=Integer.parseInt(line.split(" ")[1]);
			this.indegree[ver2].list.add(ver1);
			this.outdegree[ver1]++;
		}
	}
	
	public void initializePageRank7032(int init){
		
		float initialValue=0.0f;
		if(this.noOfVertices>10){
			init=-1;
		}
		switch(init){
		case 0:
			initialValue=0.0f;
			break;
		case 1:
			initialValue=1.0f;
			break;
		case -1:
			initialValue=1.0f/(float)this.noOfVertices;
			break;
		case -2:
			initialValue=(float) (1.0f/(float)Math.sqrt(this.noOfVertices));
			break;			
		}
		for (int i=0;i<this.noOfVertices;i++){
			this.pageRankPrev[i]=initialValue;
			this.pageRank[i]=-1;
		}
	}
	
	public void getPageRank7032(){
		
		for(int i=0;i<this.noOfVertices;i++){
			float pgrk=0.0f;
			for(int j=0;j<this.indegree[i].list.size();j++){
				int ver=this.indegree[i].list.get(j);
				
				pgrk=pgrk+(this.pageRankPrev[ver]/this.outdegree[ver]);
			}
			this.pageRank[i]=(pgrk*this.d)+this.dm;
		}
		
	}
	
	public void pipePageRank7032() {
		for(int i=0;i<this.noOfVertices;i++){
			this.pageRankPrev[i]=this.pageRank[i];
		}
	}

	public void displayIterationValues7032(int noOfIterations){
		
		DecimalFormat df = new DecimalFormat("#0.000000");
		Boolean display=true;
		if(this.noOfVertices>10){
			noOfIterations=0;
			display=false;
		}
		if(noOfIterations==0){
			int iteration=0;
			Boolean base=true;
			Boolean convergence=true;
			do{
				String output="";
				if(base){
					output="Base";
					output=output+"  : 0 : ";
					for (int v=0;v<this.noOfVertices;v++){
						output=output+"P["+v+"]="+df.format(this.pageRankPrev[v])+" ";
					}
					if (display)
						System.out.println(output+"\n");
				}
				else{
					convergence=true;
					this.getPageRank7032();
					output="Iter";
					output=output+"  : "+iteration+" : ";
					for (int v=0;v<this.noOfVertices;v++){
						output=output+"P["+v+"]="+df.format(this.pageRank[v])+" ";
					}
					if (display)
						System.out.println(output+"\n");
					
				}
				iteration++;
				for(int c=0;c<this.noOfVertices;c++){
					float a=Float.parseFloat(df.format(this.pageRankPrev[c]));
					float b=Float.parseFloat(df.format(this.pageRank[c]));
					
					if(Math.abs(a-b)>0.00001){
						convergence=false;
						break;
					}
				}
				
				if(!base){
					this.pipePageRank7032();
				}
				base=false;
				if(convergence&&display!=true){
					output ="Iter : "+(iteration-1)+" : \n";
					System.out.println(output);
					output="";
					for (int v=0;v<this.noOfVertices;v++){
						output=output+"P["+v+"]="+df.format(this.pageRank[v])+" \n\n";
					}
					System.out.println(output+"\n");
				}
					
			}while(!convergence);
		}
		else {
			for (int i=0;i<=noOfIterations;i++){
				String output="";
				if(i==0){
					output="Base  ";
					output=output+"  : "+i+" : ";
					for (int v=0;v<this.noOfVertices;v++){
						output=output+"P["+v+"]="+df.format(this.pageRankPrev[v])+" ";
					}
					System.out.println(output+"\n");
				}
				else{
					this.getPageRank7032();
					this.pipePageRank7032();
					output="Iter";
					output=output+"  : "+i+" : ";
					
					for (int v=0;v<this.noOfVertices;v++){
						output=output+"P["+v+"]="+df.format(this.pageRank[v])+" ";
					}
					System.out.println(output);
				}
				
			}
		}
	}
	
	public static void main(String []args) throws IOException{
		pgrk7032 graph;
		int ver;
		int edg;
		String line;
		BufferedReader br=null; 
		try{
			br=new BufferedReader(new FileReader(args[2]));
		}
		catch(Exception e){
			System.out.println("File Not Found :"+e);
		}
		line = br.readLine();
		ver=Integer.parseInt(line.split(" ")[0]);
		edg=Integer.parseInt(line.split(" ")[1]);
		graph=new pgrk7032(ver,edg);
		graph.initializeGraph7032(br);
		graph.initializePageRank7032(Integer.parseInt(args[1]));
		graph.displayIterationValues7032(Integer.parseInt(args[0]));
	}
	
	class AdjacencyList{
		ArrayList<Integer> list;
		
		public AdjacencyList(){
			list=new ArrayList<Integer>();
		}
	}
}
