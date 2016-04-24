/* MOHAN HARINARAYANAN cs610 PP 7032 */
package hitsAndPageRanking;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

public class hits7032 {

	int noOfVertices;
	int noOfEdges;
	float [][]adjMatrix;
	float [][]adjMatrixTrans;
	float []authority;
	float []hub;
	float []scaledAuth;
	float []scaledHub;
	float []scaledAuthPrev;
	float []scaledHubPrev;
	
	public hits7032(int ver, int edg){
		this.noOfVertices=ver;
		this.noOfEdges=edg;
		this.adjMatrix=new float[this.noOfVertices][this.noOfVertices];
		this.adjMatrixTrans=new float[this.noOfVertices][this.noOfVertices];
		this.authority=new float[this.noOfVertices];
		this.hub=new float[this.noOfVertices];
		this.scaledAuth=new float[this.noOfVertices];
		this.scaledHub=new float[this.noOfVertices];
		this.scaledAuthPrev=new float[this.noOfVertices];
		this.scaledHubPrev=new float[this.noOfVertices];
	}
	
	public void formAdjacencyMatrix7032(BufferedReader br) throws IOException{
		
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
		
	}
	
	public void formAdjacencyMatrixTranspose7032(){
		
		for (int i=0;i<this.noOfVertices;i++){
			for (int j=0;j<this.noOfVertices;j++){
				this.adjMatrixTrans[j][i]=this.adjMatrix[i][j];
			}
		}
	}
	
	public void initializeHubAndAuthority7032(int init){

		float initialValue=0.0f;
		if(this.noOfVertices>10){
			init=-1;
		}
		switch(init){
		case 0:
			initialValue =0.0f;
			break;
		case 1:
			initialValue =1.0f;
			break;
		case -1:
			initialValue=1.0f/(float)this.noOfVertices;
			break;
		case -2:
			initialValue=(float) (1.0f/(float)Math.sqrt(this.noOfVertices));
			break;
		}
		for(int i=0;i<this.noOfVertices;i++){
			this.authority[i]=initialValue;
			this.hub[i]=initialValue;
			this.scaledAuth[i]=initialValue;
			this.scaledHub[i]=initialValue;
		}
	}
		
	public void findHubValue7032(){
		
		for (int i=0;i<this.noOfVertices;i++){
			float temp=0.0f;
			for(int j=0;j<this.noOfVertices;j++){
				temp=temp+this.adjMatrix[i][j]*this.authority[j];
			}
			this.hub[i]=temp;
		}
	}
	
	public void findAuthorityValue7032(){
		
		for (int i=0;i<this.noOfVertices;i++){
			float temp=0.0f;
			for(int j=0;j<this.noOfVertices;j++){
				temp=temp+this.adjMatrixTrans[i][j]*this.hub[j];
			}
			this.authority[i]=temp;
		}
	}
	
	public void scaleHubValues7032(){
		
		float temp=0.0f;
		for(int i=0;i<this.noOfVertices;i++){
			temp=temp+(this.hub[i]*this.hub[i]);
		}
		temp=(float) Math.sqrt(temp);
		for(int i=0;i<this.noOfVertices;i++){
			this.scaledHub[i]=this.hub[i]/temp;
		}
	}
	
	public void scaleAuthorityValues7032(){
		
		float temp=0.0f;
		for(int i=0;i<this.noOfVertices;i++){
			temp=temp+(this.authority[i]*this.authority[i]);
		}
		temp=(float) Math.sqrt(temp);
		for(int i=0;i<this.noOfVertices;i++){
			this.scaledAuth[i]=this.authority[i]/temp;
		}
	}
	
	public void displayIterationValues7032(int noOfiterations){
		DecimalFormat df = new DecimalFormat("#0.000000");
		Boolean display=true;
		if(this.noOfVertices>10){
			noOfiterations=0;
			display=false;
		}
		if (noOfiterations==0){
			int iteration=0;
			Boolean convergence=true;
			do{
				convergence=true;
				String output="";
				if(iteration==0){
					
					for(int i=0;i<this.noOfVertices;i++){
						this.scaledAuthPrev[i]=-1.0f;
						this.scaledHubPrev[i]=-1.0f;
					}
					output="Base";
				}
				else{
					output="Iter";
					this.findAuthorityValue7032();
					this.findHubValue7032();
					this.scaleAuthorityValues7032();
					this.scaleHubValues7032();
				}
				output=output+" : "+iteration+" : ";
				
				if (display){
					for (int v=0;v<this.noOfVertices;v++){
						output=output+"A/H["+v+"]="+df.format(this.scaledAuth[v])+"/"+df.format(this.scaledHub[v])+" ";
					}	
					System.out.println(output+"\n");
				}
				
				for(int c=0;c<this.noOfVertices;c++){
					float aP=Float.parseFloat(df.format(this.scaledAuthPrev[c]));
					float a=Float.parseFloat(df.format(this.scaledAuth[c]));
					float hP=Float.parseFloat(df.format(this.scaledHubPrev[c]));
					float h=Float.parseFloat(df.format(this.scaledHub[c]));
					
					if((Math.abs(aP-a)>0.00001)||(Math.abs(hP-h)>0.00001)){
						convergence=false;
						break;
					}
				}
				if(convergence&&display!=true){
					System.out.println("Iter  : "+iteration+" : \n");
					
					for (int v=0;v<this.noOfVertices;v++){
						output="";
						output=output+"A/H["+v+"]="+df.format(this.scaledAuth[v])+"/"+df.format(this.scaledHub[v])+" \n";
						System.out.println(output);
					}
				}
				this.pipeAuhHubValues7032();
				iteration++;
			}while(!convergence);
		}
		else{
			for (int i=0;i<=noOfiterations;i++){
				String output="";
				if(i==0){
					output="Base";
				}
				else{
					output="Iter";
					this.findAuthorityValue7032();
					this.findHubValue7032();
					this.scaleAuthorityValues7032();
					this.scaleHubValues7032();
				}
				output=output+"  : "+i+" : ";
				
				
				for (int v=0;v<this.noOfVertices;v++){
					output=output+"A/H["+v+"]="+df.format(this.scaledAuth[v])+"/"+df.format(this.scaledHub[v])+" ";
				}
				System.out.println(output+"\n");
			}
		}
	}
	
	public void pipeAuhHubValues7032() {
		for(int d=0;d<this.noOfVertices;d++){
			this.scaledAuthPrev[d]=this.scaledAuth[d];
			this.scaledHubPrev[d]=this.scaledHub[d];
		}
	}

	public static void main(String []args) throws IOException{
		
		hits7032 graph;
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
		graph=new hits7032(ver,edg);
		graph.formAdjacencyMatrix7032(br);
		graph.formAdjacencyMatrixTranspose7032();
		graph.initializeHubAndAuthority7032(Integer.parseInt(args[1]));
		graph.displayIterationValues7032(Integer.parseInt(args[0]));
	}
}
