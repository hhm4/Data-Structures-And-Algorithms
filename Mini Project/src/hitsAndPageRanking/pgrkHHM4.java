package hitsAndPageRanking;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class pgrkHHM4 {
	int noOfVertices;
	int noOfEdges;
	int []outdegree;
	float d;
	float dm;
	AdjacencyList []indegree;
	float []pageRankPrev;
	float []pageRank;
	
	public pgrkHHM4(int ver,int edg){
		this.noOfVertices=ver;
		this.noOfEdges=edg;
		this.d=0.85f;
		this.dm=(1.0f-d)/(float)this.noOfVertices;
		this.outdegree=new int[this.noOfVertices];
		this.pageRankPrev=new float[this.noOfVertices];
		this.pageRank=new float[this.noOfVertices];
		this.indegree=new AdjacencyList[this.noOfVertices];
		//this.indegree=new AdjacencyList[this.noOfVertices];
		for (int i=0;i<this.noOfVertices;i++){
			this.outdegree[i]=0;
			this.indegree[i]=new AdjacencyList();
		}
	}
	
	public void initializeGraph(BufferedReader br) throws IOException{
		for (int i=0;i<this.noOfEdges;i++){
			String line = br.readLine();
			int ver1=Integer.parseInt(line.split(" ")[0]);
			int ver2=Integer.parseInt(line.split(" ")[1]);
			this.indegree[ver2].list.add(ver1);
			this.outdegree[ver1]++;
		}
	}
	
	public void initializePageRank(int init){
		float initialValue=0.0f;
		if(init ==0){
			initialValue=0.0f;
		}
		if(init==1){
			initialValue=1.0f;
		}
		if(init==-1){
			initialValue=1.0f/(float)this.noOfVertices;
		}
		if(init==-2){
			initialValue=(float) (1.0f/(float)Math.sqrt(this.noOfVertices));
		}
		for (int i=0;i<this.noOfVertices;i++){
			this.pageRankPrev[i]=initialValue;
			this.pageRank[i]=-1;
		}
	}
	
	public void getPageRank(){
		
		
		for(int i=0;i<this.noOfVertices;i++){
			float pgrk=0.0f;
			for(int j=0;j<this.indegree[i].list.size();j++){
				int ver=this.indegree[i].list.get(j);
				
				pgrk=pgrk+(this.pageRankPrev[ver]/this.outdegree[ver]);
			}
			
			this.pageRank[i]=(pgrk*this.d)+this.dm;
		}
		
	}
	
	private void pipePageRank() {
		for(int i=0;i<this.noOfVertices;i++){
			this.pageRankPrev[i]=this.pageRank[i];
		}
	}

	public void displayIterationValues(int noOfIterations){
		DecimalFormat df = new DecimalFormat("#0.000000");
		
		if(noOfIterations==0){
			int iteration=0;
			Boolean base=true;
			Boolean convergence=true;
			do{
				String output="";
				if(base){
					output="Base  ";
					output=output+"  : 0 :";
					for (int v=0;v<this.noOfVertices;v++){
						output=output+"P["+v+"]="+df.format(this.pageRankPrev[v])+" ";
					}
					System.out.println(output+"\n");
					
					
				}
				else{
					convergence=true;
					this.getPageRank();
					output="Iterat";
					output=output+"  : "+iteration+" :";
					
					
					for (int v=0;v<this.noOfVertices;v++){
						output=output+"P["+v+"]="+df.format(this.pageRank[v])+" ";
					}
					System.out.println(output+"\n");
					
				}
				iteration++;
				for(int c=0;c<this.noOfVertices;c++){
					float a=Float.parseFloat(df.format(this.pageRankPrev[c]));
					float b=Float.parseFloat(df.format(this.pageRank[c]));
					if(a!=b){
						convergence=false;
						break;
					}
				}
				
				if(!base){
					this.pipePageRank();
				}
				base=false;
			}while(!convergence);
		}
		else {
			for (int i=0;i<=noOfIterations;i++){
				String output="";
				if(i==0){
					output="Base  ";
					output=output+"  : "+i+" :";
					for (int v=0;v<this.noOfVertices;v++){
						output=output+"P["+v+"]="+df.format(this.pageRankPrev[v])+" ";
					}
					System.out.println(output+"\n");
				}
				else{
					this.getPageRank();
					this.pipePageRank();
					output="Iterat";
					output=output+"  : "+i+" :";
					
					for (int v=0;v<this.noOfVertices;v++){
						output=output+"P["+v+"]="+df.format(this.pageRank[v])+" ";
					}
					System.out.println(output+"\n");
				}
				
			}
		}
	}
	
	public static void main(String []args) throws IOException{
		pgrkHHM4 graph;
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
		graph=new pgrkHHM4(ver,edg);
		graph.initializeGraph(br);
		graph.initializePageRank(Integer.parseInt(args[1]));
		graph.displayIterationValues(Integer.parseInt(args[0]));
	}
	
	class AdjacencyList{
		ArrayList<Integer> list;
		
		public AdjacencyList(){
			list=new ArrayList<Integer>();
		}
	}
}
