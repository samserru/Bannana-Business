import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class LeaderBoard {

	public static String leaderBoardScores,leaderBoardNames;

	public static String[] nLeaderBoard;

	public static ArrayList<Integer> fSBoard,top10;
	public static ArrayList<String> nBoard,fNBoard,finalLeaderBoard;
	public static HashMap<String, String> leaderBoard;

	public static String updateBoardNames = " ";
	public static String updateBoardScores = " ";

	public static int n;
	public static int max;
	public static int maxIndex;

	public LeaderBoard() throws IOException {
		max = 0;
		maxIndex=0;
		n=10;

		leaderBoardScores = new String(Files.readAllBytes(Paths.get("textFiles/LeaderBoardScores.txt")));
		leaderBoardNames = new String(Files.readAllBytes(Paths.get("textFiles/LeaderBoardNames.txt")));


		fSBoard = new ArrayList<Integer>();
		top10 = new ArrayList<Integer>();
		nBoard = new ArrayList<String>();
		fNBoard = new ArrayList<String>();
		finalLeaderBoard = new ArrayList<String>();


		for(String s: leaderBoardNames.split("@@")) {
			nBoard.add(s);
		}

		for(String s: leaderBoardScores.split("@@")) {
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

	}

	public void updateLeaderBoard() throws IOException {
		leaderBoardScores = new String(Files.readAllBytes(Paths.get("textFiles/LeaderBoardScores.txt")));
		leaderBoardNames = new String(Files.readAllBytes(Paths.get("textFiles/LeaderBoardNames.txt")));


		fSBoard = new ArrayList<Integer>();
		top10 = new ArrayList<Integer>();
		nBoard = new ArrayList<String>();
		fNBoard = new ArrayList<String>();
		finalLeaderBoard = new ArrayList<String>();
		
		
		for(String s: leaderBoardNames.split("@@")) {
			nBoard.add(s);
		}

		
		for(String s: leaderBoardScores.split("@@")) {
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
			updateBoardScores += (s + "@@");
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
			updateBoardNames += (s+ "@@");
		}
		nWrite.write(updateBoardNames);
		nDelete.close();

		nWrite.close();
	}

	public ArrayList<String> getTop10() throws IOException {
		for(int i =0;i<top10.size();i++) {
			finalLeaderBoard.add((i+1)+". " + fNBoard.get(i) + " | "+ top10.get(i));
		}

		return finalLeaderBoard;

	}



	public void addScore(String name, int score) throws IOException {

		FileWriter sWrite = new FileWriter("textFiles/LeaderBoardScores.txt");
		sWrite.write(leaderBoardScores+ score+ "@@");

		FileWriter nWrite = new FileWriter("textFiles/LeaderBoardNames.txt");
		nWrite.write(leaderBoardNames+name + "@@");

		sWrite.close();
		nWrite.close();

	}

	public boolean checkScore(int score) {
		for(int s : top10) {
			if(s < score)
				return true;
		}
		return false;
	}

}
