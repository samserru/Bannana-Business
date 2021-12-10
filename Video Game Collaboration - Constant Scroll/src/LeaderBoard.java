import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LeaderBoard {

	public static String leaderBoardScores;
	public static String[] sLeaderBoard;

	public static String leaderBoardNames;
	public static String[] nLeaderBoard;

	public static ArrayList<Integer> fSBoard,top10;
	public static ArrayList<String> nBoard,fNBoard,finalLeaderBoard;

	public static String updateBoardNames = " ";
	public static String updateBoardScores = " ";

	public static int n=10;

	public LeaderBoard() throws IOException {
		try{
			leaderBoardScores = new String(Files.readAllBytes(Paths.get("textFiles/LeaderBoardScores.txt")));
			leaderBoardNames = new String(Files.readAllBytes(Paths.get("textFiles/LeaderBoardNames.txt")));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getTop10() throws IOException {
		int max = 0;
		int maxIndex=0;
		try{
			leaderBoardScores = new String(Files.readAllBytes(Paths.get("textFiles/LeaderBoardScores.txt")));
			leaderBoardNames = new String(Files.readAllBytes(Paths.get("textFiles/LeaderBoardNames.txt")));
		}catch(IOException e) {e.printStackTrace();}
		
		fSBoard = new ArrayList<Integer>();
		top10 = new ArrayList<Integer>();
		nBoard = new ArrayList<String>();
		fNBoard = new ArrayList<String>();
		finalLeaderBoard = new ArrayList<String>();


		nLeaderBoard = leaderBoardNames.split(" ");
		for(String s: nLeaderBoard) {
			nBoard.add(s);
		}

		sLeaderBoard = leaderBoardScores.split(" ");
		for(String s: sLeaderBoard) {
			fSBoard.add(Integer.parseInt(s));
		}

		if(fSBoard.size() <10)
			n=fSBoard.size();

		for(int x = 0; x<n ;x++) {
			max = 0;
			maxIndex=0;

			for(int i = 0; i < fSBoard.size(); i++) {

				if(fSBoard.get(i)>max) {
					max = fSBoard.get(i);
					maxIndex=i;
				}
			}

			top10.add(fSBoard.get(maxIndex));
			fSBoard.remove(maxIndex);

			fNBoard.add(nBoard.get(maxIndex));
			nBoard.remove(maxIndex);
		}



		updateBoardScores = "";
		FileWriter sWrite = new FileWriter("textFiles/LeaderBoardScores.txt");
		PrintWriter sDelete = new PrintWriter(sWrite, false);
		sDelete.flush();
		sWrite.write("");
		for(int s: top10) {
			updateBoardScores += (s + " ");
		}
		sWrite.write(updateBoardScores);
		sDelete.close();
		sWrite.close();


		updateBoardNames = "";
		FileWriter nWrite = new FileWriter("textFiles/LeaderBoardNames.txt");
		PrintWriter nDelete = new PrintWriter(sWrite, false);
		nWrite.write("");
		nDelete.flush();
		for(String s: fNBoard) {
			updateBoardNames += (s+ " ");
		}
		nWrite.write(updateBoardNames);
		nDelete.close();

		nWrite.close();
		
		
		for(int i =0;i<top10.size();i++) {
			finalLeaderBoard.add((i+1)+". " + fNBoard.get(i) + " Score: "+ top10.get(i));
		}
		
		
		return finalLeaderBoard;

	}
	
	
	public static void addScore(String name, String score) throws IOException {
		try {
			FileWriter sWrite = new FileWriter("textFiles/LeaderBoardScores.txt");
			sWrite.write(leaderBoardScores+ score+ " ");

			FileWriter nWrite = new FileWriter("textFiles/LeaderBoardNames.txt");
			nWrite.write(leaderBoardNames+name + " ");

			sWrite.close();
			nWrite.close();
		}catch(Exception e) {System.out.println(e);}


		
	}

}
