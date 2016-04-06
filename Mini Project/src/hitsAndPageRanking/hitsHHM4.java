package hitsAndPageRanking;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;

public class hitsHHM4 {

	int noOfVertices;
	int noOfEdges;
	float [][]adjMatrix;
	float [][]adjMatrixTrans;
	float []authority;
	float []hub;
	float []scaledAuth;
	float []scaledHub;
	
	public hitsHHM4(int ver, int edg){
		this.noOfVertices=ver;
		this.noOfEdges=edg;
		this.adjMatrix=new float[this.noOfVertices][this.noOfVertices];
		this.adjMatrixTrans=new float[this.noOfVertices][this.noOfVertices];
		this.authority=new float[this.noOfVertices];
		this.hub=new float[this.noOfVertices];
		this.scaledAuth=new float[this.noOfVertices];
		this.scaledHub=new float[this.noOfVertices];
	}
	
	public void formAdjacencyMatrix(BufferedReader br) throws IOException{
		
		for (int i=0;i<this.noOfVertices;i++){
			for (int j=0;j<this.noOfVertices;j++){
				this.adjMatrix[i][j]=0.0f;
			}
		}
		for (int i=0;i<this.noOfEdges;i++){
			String line=br.readLine();
			int vert1=Integer.parseInt(line.split(" ")[0]);
			int vert2=Integer.parseInt(line.split(" ")[1]);
			this.adjMatrix[vert1][vert2]=1.0f;
		}
//		for (int i=0;i<this.noOfVertices;i++){
//			for (int j=0;j<this.noOfVertices;j++){
//				System.out.print(this.adjMatrix[i][j]+" ");
//			}
//			System.out.print("\n");
//		}
		
	}
	
	public void formAdjacencyMatrixTranspose(){
		
		for (int i=0;i<this.noOfVertices;i++){
			for (int j=0;j<this.noOfVertices;j++){
				this.adjMatrixTrans[j][i]=this.adjMatrix[i][j];
			}
		}
//		for (int i=0;i<this.noOfVertices;i++){
//			for (int j=0;j<this.noOfVertices;j++){
//				System.out.print(this.adjMatrixTrans[i][j]+" ");
//			}
//			System.out.print("\n");
//		}
	}
	
	public void initializeHubAndAuthority(int init){
		
		float initialValue=0.0f;
		if(init==0){
			initialValue =0.0f;
		}
		if(init==1){
			initialValue =1.0f;
		}
		if(init==-1){
			initialValue=1.0f/(float)this.noOfVertices;
		}
		if(init==-2){
			initialValue=(float) (1.0f/(float)Math.sqrt(this.noOfVertices));
		}
		for(int i=0;i<this.noOfVertices;i++){
			this.authority[i]=initialValue;
			this.hub[i]=initialValue;
			this.scaledAuth[i]=initialValue;
			this.scaledHub[i]=initialValue;
		}
	}
	
	public void findHubValue(){
		for (int i=0;i<this.noOfVertices;i++){
			float temp=0.0f;
			for(int j=0;j<this.noOfVertices;j++){
				temp=temp+this.adjMatrix[i][j]*this.authority[j];
			}
			this.hub[i]=temp;
		}
	}
	
	public void findAuthorityValue(){
		
		for (int i=0;i<this.noOfVertices;i++){
			float temp=0.0f;
			for(int j=0;j<this.noOfVertices;j++){
				temp=temp+this.adjMatrixTrans[i][j]*this.hub[j];
			}
			this.authority[i]=temp;
		}
	}
	
	public void scaleHubValues(){
		
		float temp=0.0f;
		for(int i=0;i<this.noOfVertices;i++){
			temp=temp+(this.hub[i]*this.hub[i]);
		}
		temp=(float) Math.sqrt(temp);
		for(int i=0;i<this.noOfVertices;i++){
			this.scaledHub[i]=this.hub[i]/temp;
		}
	}
	
	public void scaleAuthorityValues(){
		
		float temp=0.0f;
		for(int i=0;i<this.noOfVertices;i++){
			temp=temp+(this.authority[i]*this.authority[i]);
		}
		temp=(float) Math.sqrt(temp);
		for(int i=0;i<this.noOfVertices;i++){
			this.scaledAuth[i]=this.authority[i]/temp;
		}
	}
	
	public void displayIterationValues(int noOfiterations){
		DecimalFormat df = new DecimalFormat("#0.00000");
		//df.setRoundingMode(RoundingMode.CEILING);
		if (noOfiterations==0){
			
		}
		else{
			for (int i=0;i<=noOfiterations;i++){
				String output="";
				if(i==0){
					output="Base  ";
				}
				else{
					output="Iterat";
					this.findAuthorityValue();
					this.findHubValue();
					this.scaleAuthorityValues();
					this.scaleHubValues();
				}
				output=output+"  : "+i+" :";
				
				
				for (int v=0;v<this.noOfVertices;v++){
					output=output+"A/H["+v+"]="+df.format(this.scaledAuth[v])+"/"+df.format(this.scaledHub[v])+" ";
				}
				System.out.println(output+"\n");
			}
		}
		
	}
	
	public static void main(String []args) throws IOException{
		
		hitsHHM4 graph;
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
		graph=new hitsHHM4(ver,edg);
		graph.formAdjacencyMatrix(br);
		System.out.println("\n");
		graph.formAdjacencyMatrixTranspose();
		graph.initializeHubAndAuthority(Integer.parseInt(args[1]));
		graph.displayIterationValues(Integer.parseInt(args[0]));
	}
}
