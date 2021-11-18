package 스팸메일;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Spam_filtering {
	public static Vector<wordCount> spamList = new  Vector<wordCount>();;
	public static Vector<wordCount> hamList = new  Vector<wordCount>();
	
	public class wordCount{
		String word; //단어
		int count; //횟수
		boolean isExist;
	}
	
	public static void main(String[] args) {
		//training
		Spam_filtering spamFiltering = new Spam_filtering();
		spamList = spamFiltering.training("spamTraining.txt");
		hamList = spamFiltering.training("hamTraining.txt");

		//testing
		spamFiltering.testing();
	}
	
	public Vector<wordCount> training(String fileName){

		Vector<wordCount> list = new Vector<wordCount>();
		Scanner inputStream = null; 
		try {
			inputStream = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("파일을 찾을 수 없습니다.");
		}
		
		while(inputStream.hasNextLine()) { // 이메일 하나씩 옮겨놓는다. 
	
			String [] oneMail = inputStream.nextLine().split("\n\n"); //메일을 하나씩 분리
			String word[] = oneMail[0].split(" "); //한 메일을 단어로 분리
			
			for(wordCount tmp : list) { //for문을 통한 단어 사용여부 초기화 
				tmp.isExist=false;
			}
			boolean isHappen=false;
			
			for(int i=0;i<word.length;i++) { //각각의 단어에 대해 list의 단어들과 비교
				
				word[i]=word[i].toLowerCase().trim(); //모두 소문자화
				for(int j=0;j<list.size();j++) {
					wordCount tmp = list.get(j);
					if(tmp.word.equals(word[i]) && tmp.isExist == false) { //list에 존재하는 단어지만 이 메일에서는 처음 등장했을 경우 -> 횟수를 1 증가
						tmp.count++;
						tmp.isExist=true;
						isHappen=true;
						break;
					}
				}
				
				if(isHappen==false) { // list에 존재하지 않는 단어일 경우 -> 단어를 추가한다.
					wordCount tmp = new wordCount();
					tmp.word = word[i];
					tmp.count = 1;
					tmp.isExist =true;
					list.add(tmp);
				}
			}
		}
//		for(wordCount tmp : list) {
//			System.out.println("'"+ tmp.word+"' -> "+ tmp.count + "/25");
//		}
		
		return list;
	}

	public void testing() {
		boolean isSpam;
		
		for(int i=1;i<=5;i++) {
			//a_simple_bayesian_spam_filter으로 하나씩 보낸다.
			isSpam = a_simple_bayesian_spam_filter("spam"+i);
			//하나에 대한 결과값을 출력한다.
			if(isSpam)System.out.println("spam"+i + " 이메일은 스팸입니다. \n");
			else System.out.println("spam"+i + " 이메일은 스팸이 아닙니다. \n");
		}

		for(int i=1;i<=5;i++) {
			//a_simple_bayesian_spam_filter으로 하나씩 보낸다.
			isSpam = a_simple_bayesian_spam_filter("ham"+i);
			//하나에 대한 결과값을 출력한다.
			if(isSpam)System.out.println("ham"+i + " 이메일은 스팸입니다. \n");
			else System.out.println("ham"+i + " 이메일은 스팸이 아닙니다. \n");
		}
	}
	
	boolean a_simple_bayesian_spam_filter(String filename_for_test_email) {
		
		double spamProduct=1;
		double hamProduct=1;
		Scanner inputStream = null; 
		try {
			inputStream = new Scanner(new File(filename_for_test_email));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("파일을 찾을 수 없습니다.");
		}
		
		while(inputStream.hasNextLine()) { 
			String word []= inputStream.nextLine().split(" "); //메일을 단어로 분리
//			String word[] = oneLine[0].split(" "); //한 메일을 단어로 분리
			
			for(int i=0;i<word.length;i++) {//단어 하나씩 접근 
				//스팸 리스트에서 하나씩 빼오기
				word[i]=word[i].toLowerCase().trim(); //모두 소문자화
				for(int j=0;j<spamList.size();j++) {
					wordCount tmp = spamList.get(j);
					
					if(tmp.word.equals(word[i])) {
						spamProduct= spamProduct * tmp.count;
						break;
					}
				}
				for(int j=0;j<hamList.size();j++) {//일반 리스트에서 하나씩 빼오기
					wordCount tmp = hamList.get(j);
					
					if(tmp.word.equals(word[i])) {
						hamProduct= hamProduct * tmp.count;
						break;
					}
				}
			}
		}
		return (spamProduct/(spamProduct+hamProduct)>0.9);
	}
}


