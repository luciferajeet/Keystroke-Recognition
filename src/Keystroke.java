import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Keystroke extends JFrame implements KeyListener{

	private static final long serialVersionUID = 1L;
	private JFrame mainFrame;
	private JPanel textPanel;
	private JPanel inputPanel;
	private JPanel btnPanel;
	private JLabel textArea1;
	private JLabel textArea2;
	private JTextField input;
	private JButton okBtn;
	private JButton canBtn;

	double startTime;
	double endTime;
	double pressTime;
	double releaseTime;
	double holdTime;
	double totalHold=0;
	double time;
	int ctr=0;
	boolean bln=false;

	private static File textFile1;
	private static File textFile2;

	public double test[]=new double[2];
	int space=0;
	int termination=0;
	boolean authorized=false;
	int numOfKeys=0;

	private static final String message = "Type the following and Click Okay button or Press Enter key to submit your typing rhythm."
			+ " Small typos are acceptable.";
	private static final String msg="           Follow your Dreams";

	public Keystroke() {
		createGUI();
	}

	void createGUI(){
			mainFrame = new JFrame("Keystroke Recognition by Ajeet and Anirudh. Version 1.0 ; Accuracy 87%");
			mainFrame.setSize(700,200);
			mainFrame.setLayout(new GridLayout(4,1));

			mainFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent windowEvent) {
					System.exit(0);
				}
			});

			textPanel = new JPanel();
			inputPanel = new JPanel();
			btnPanel = new JPanel();
			mainFrame.add(textPanel);
			mainFrame.add(inputPanel);
			mainFrame.add(btnPanel);

			textArea1 = new JLabel(message);
			textArea2=new JLabel(msg);
			input = new JTextField(20);
			input.addKeyListener(this);
			textPanel.add(textArea1);
			textPanel.add(textArea2);
			inputPanel.add(input);

			okBtn = new JButton("Okay");
			canBtn = new JButton("Cancel");
			okBtn.setActionCommand("Okay");
			canBtn.setActionCommand("Cancel");
			okBtn.addActionListener(new ButtonClickListener());
			canBtn.addActionListener(new ButtonClickListener());
			btnPanel.add(okBtn);
			btnPanel.add(canBtn);

			mainFrame.setVisible(true);
	}

	public void keyTyped(KeyEvent ke) {}

	public void keyPressed(KeyEvent ke) {
		try {
			numOfKeys++;
			pressTime=System.currentTimeMillis();
			if(ctr==0){
				startTime = System.currentTimeMillis();
				ctr++;
				}
			}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void keyReleased(KeyEvent ke) {
		try {
			if(ke.getKeyCode()==KeyEvent.VK_CAPS_LOCK)
				termination++;
			if(ke.getKeyCode()==KeyEvent.VK_BACK_SPACE)
				termination++;
			if(ke.getKeyCode()==KeyEvent.VK_SPACE)
				space++;
			if(ke.getKeyCode()==KeyEvent.VK_ENTER)
				execution("Okay");
			releaseTime=System.currentTimeMillis();
			holdTime=(releaseTime-pressTime);
			totalHold+=holdTime;
			totalHold=totalHold/100;
			totalHold=Double.parseDouble(new DecimalFormat("###.###").format(totalHold));
			}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void results() {
		try {
			StringBuffer speed = new StringBuffer();
			StringBuffer hold = new StringBuffer();

			speed.append(time+"\t");
			hold.append(totalHold+"\t");

			test[0]=time;
			test[1]=totalHold;

			//System.out.println(test[0]+"  "+test[1]+"  "+numOfKeys+"  "+space);

			if(space!=2 || time>5.0 || totalHold>2.0 || numOfKeys<20 || numOfKeys>22)
				termination++;

			//System.out.println(termination);

			textFile1=new File("Speed Test Cases.txt");
			textFile2=new File("Hold Time Test Cases.txt");

			if(!textFile1.exists())
				textFile1.createNewFile();
			if(!textFile2.exists())
				textFile2.createNewFile();

			FileWriter fw1=new FileWriter(textFile1,true);
			fw1.write(speed.toString());
			fw1.close();

			FileWriter fw2=new FileWriter(textFile2,true);
			fw2.write(hold.toString());
			fw2.close();

			if(termination>0)
				Unauthorized();
			else if(termination==0)
				Authorized();
		}

		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void Authorized(){
		DisplayMode displayMode=new DisplayMode(800,600,16,DisplayMode.REFRESH_RATE_UNKNOWN);
		Keystroke b=new Keystroke();
		b.runAuthorized(displayMode);
		authorized=true;
	}

	private void Unauthorized(){
		DisplayMode displayMode=new DisplayMode(800,600,16,DisplayMode.REFRESH_RATE_UNKNOWN);
		Keystroke b=new Keystroke();
		b.runUnauthorized(displayMode);
		authorized=false;
	}

	private Screen screen;
	private Image bgU;
	private Image bgA;

	public void runAuthorized(DisplayMode dm){
		screen=new Screen();
		try{
			screen.setFullScreen(dm,new JFrame());
			loadPicsAuthorized();
			movieLoopAuthorized();
		}finally{
			screen.restoreScreen();
		}
	}

	public void runUnauthorized(DisplayMode dm){
		screen=new Screen();
		try{
			screen.setFullScreen(dm,new JFrame());
			loadPicsUnauthorized();
			movieLoopUnauthorized();
		}finally{
			screen.restoreScreen();
		}
	}

	public void loadPicsUnauthorized(){
		bgU=new ImageIcon("E:\\Unauthorized.jpg").getImage();
		//System.out.println("%%%%%");
	}

	public void loadPicsAuthorized(){
		bgA=new ImageIcon("E:\\Authorized.jpg").getImage();
		//System.out.println("****");
	}

	public void movieLoopAuthorized(){
		long startingTime=System.currentTimeMillis();
		long cumTime=startingTime;

		while(cumTime-startingTime<5000){
			long timePassed=System.currentTimeMillis()-cumTime;
			cumTime+=timePassed;

			Graphics g=screen.getFullScreenWindow().getGraphics();
			drawA(g);
			g.dispose();

			try{
				Thread.sleep(20);
			}catch(Exception ex){}
		}
	}

	public void movieLoopUnauthorized(){
		long startingTime=System.currentTimeMillis();
		long cumTime=startingTime;

		while(cumTime-startingTime<5000){
			long timePassed=System.currentTimeMillis()-cumTime;
			cumTime+=timePassed;

			Graphics g=screen.getFullScreenWindow().getGraphics();
			drawU(g);
			g.dispose();

			try{
				Thread.sleep(20);
			}catch(Exception ex){}
		}
	}

	public void drawU(Graphics g){
			g.drawImage(bgU,0,0,null);
	}

	public void drawA(Graphics g){
		g.drawImage(bgA,0,0,null);
	}

	/*private void giveTestCases(double time, double hold) throws FileNotFoundException {
		Scanner sc4=new Scanner(new File("shuffledSpeed.txt"));
		Scanner sc5=new Scanner(new File("shuffledHold.txt"));
		Scanner sc6=new Scanner(new File("shuffledY.txt"));

		double[] userInput=new double[2];
		userInput[0]=time;
		userInput[1]=hold;

		//Keystroke.tryFunction(sc4,sc5,sc6,userInput);
	}*/

	private class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			try {
				String command = ae.getActionCommand();
				execution(command);
				}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void execution(String command) {
		if(command.equals("Okay")) {
			endTime = System.currentTimeMillis();
			if(ctr>0){
				time=(endTime-startTime)/1000;
				time=Double.parseDouble(new DecimalFormat("###.###").format(time));
			}
			else
				time=0;
			mainFrame.dispose();
			results();
		}
		else if(command.equals("Cancel")) {
			mainFrame.dispose();
		}
	}

	/*private static void tryFunction(Scanner sc1, Scanner sc2, Scanner sc3,double[] userInput) {
		int anomalyNum=50;
		int correctNum=500;
		int m=anomalyNum+correctNum;
		int n=2;
		int XtrainNum=440;
		int XcvNum=60;
		int cv=0;
		double inputLimit=0.0;

		int tp=0;
		int fp=0;
		int fn=0;
		double prec=0;
		double rec=0;
		double F1=0;
		double bestF1=0;
		double bestEpsilon=0;

		double[] x1=new double[m];
		double[] x2=new double[m];
		//double[][] x=new double[m][3];
		double[][] X=new double[m][n];
		double[] y=new double[m];
		double[][] Xtrain=new double[XtrainNum][n];
		double[][] Xcv=new double[XcvNum][n];
		double[] ytrain=new double[XtrainNum];
		double[] ycv=new double[XcvNum];
		double[] mu=new double[n];
		double[] sigma2=new double[n];
		double[] p=new double[n];
		double[] predictions=new double[XcvNum];
		double[] pcv=new double[XcvNum];

		int g=0,r=0,t=0;

		while(sc1.hasNextDouble() && g<550)				//populating x1
			x1[g++]=sc1.nextDouble();

		while(sc2.hasNextDouble() && t<550)				//populating x2
			x2[t++]=sc2.nextDouble();

		while(sc3.hasNextDouble() && r<550)				//populating y
			y[r++]=sc3.nextDouble();

		for(int a=0;a<g;a++){							//combining x1 and x2 x
			X[a][0]=x1[a];
			X[a][1]=x2[a];
		}

		for(int a=0;a<XtrainNum;a++)					//getting Xtrain & ytrain
			for(int b=0;b<n;b++){
				Xtrain[a][b]=X[a][b];
				ytrain[a]=y[a];
			}

		for(int a=XtrainNum;a<(XtrainNum+XcvNum);a++){	//getting Xcv & ycv
			for(int b=0;b<2;b++){
				Xcv[cv][b]=X[a][b];
				ycv[cv]=y[a];
			}
			cv++;
		}

		for(int i=0;i<n;i++){							//initializing mu and sigma2
			mu[i]=0;
			sigma2[i]=0;
		}

		double sumX1=0.0,sumX2=0.0;

		for(int i=0;i<XtrainNum;i++){
			sumX1+=Xtrain[i][0];
			sumX2+=Xtrain[i][1];
		}

		mu[0]=sumX1/m;
		mu[1]=sumX2/m;									//calculating mu

		mu[0]=Double.parseDouble(new DecimalFormat("###.###").format(mu[0]));
		mu[1]=Double.parseDouble(new DecimalFormat("###.###").format(mu[1]));

		for(int i=0;i<n;i++){							//calculating sigma2
			for(int j=0;j<XtrainNum;j++)
				sigma2[i]=(Xtrain[j][i]-mu[i])*(Xtrain[j][i]-mu[i]);
			sigma2[i]=sigma2[i]/m;
		}

		sigma2[0]=Double.parseDouble(new DecimalFormat("###.#######").format(sigma2[0]));
		sigma2[1]=Double.parseDouble(new DecimalFormat("###.#######").format(sigma2[1]));

		for(int i=0;i<2;i++){
			System.out.println(mu[i]+"MU");
			System.out.println(sigma2[i]+"sigma");
		}

		for(int i=0;i<n;i++){
			double sigma = Math.sqrt(sigma2[i]);
			double rtTwoPi=Math.sqrt((2*Math.PI));
			double denominator=sigma*rtTwoPi;
			double numerator=(userInput[i]-mu[i])*(userInput[i]-mu[i]);
			double den=2*sigma2[i];
			double pow=numerator/den;
			double power=-1*pow;
			power=power/1000;
			System.out.println(power+"POWER");
			double secondPart=Math.exp(power);
			p[i]=secondPart/denominator;
			System.out.println(p[i]+"%%%%%%%%");
		}

		inputLimit=p[0]*p[1];

		for(int i=0;i<XcvNum;i++){
			for(int j=0;j<n;j++){
				double sigma = Math.sqrt(sigma2[j]);
				pcv[i]=((Math.exp(-(((Xcv[i][j]-mu[j])*(Xcv[i][j]-mu[j]))/(2*sigma2[j]))))/(2*Math.PI*sigma));
			}
		}

		double max=pcv[0];

		for(int i=0;i<XcvNum;i++){
			if(max<=pcv[i])
				max=pcv[i];
		}

		double min=max;

		for(int i=0;i<XcvNum;i++){
			if(min>=pcv[i])
				min=pcv[i];
		}

		double stepSize=max-min;
		int z=0;

		for(double epsilon=min;epsilon<=max;epsilon+=stepSize){
			z++;
			for(int i=0;i<XcvNum;i++){
				if(pcv[i]<epsilon)
					predictions[i]=0;
				else
					predictions[i]=1;
			}

			for(int i=0;i<XcvNum;i++){
				if(predictions[i]==1 && ycv[i]==0)
					fp++;
				if(predictions[i]==1 && ycv[i]==1)
					tp++;
				if(predictions[i]==0 && ycv[i]==1)
					fn++;
			}

			prec=tp/(tp+fp);
			rec=tp/(tp+fn);
			F1=(2*rec*prec)/(prec+rec);

			if(F1>bestF1){
				bestF1=F1;
				bestEpsilon=epsilon;
			}
		}
		System.out.println("Z is="+z);

		System.out.println("BestEpsilon is="+bestEpsilon);

		if(inputLimit<bestEpsilon)
			System.out.println("You are not AJEET");
		else
			System.out.println("You are AJEET");
	}*/

	public static void main(String[] args) throws FileNotFoundException {
		Keystroke ks = new Keystroke();
	}
}