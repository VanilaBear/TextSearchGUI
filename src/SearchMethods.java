import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


class SearchMethods {
	
	public static ArrayList<String> searchGoodFiles(String  directory, ArrayList<String> paths,
	                                                String word,  String extension){
		File dir = new File(directory);
		
		for (File i : dir.listFiles()) {
			try {
				if (i.isDirectory()) {
					searchGoodFiles(i.getPath(), paths, word, extension);
				}
				else if (isInFile(i.getPath(), word, extension)){
					paths.add(i.getPath());
				}
			}
			catch (Exception e){e.printStackTrace();}
		}
		return paths;
		
	}
	
	public static void selectText(File file, ArrayList<ArrayList<Integer>> array,
	                              String word, JTextPane textPane) throws IOException {
		
		InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
		BufferedReader fileReader = new BufferedReader(reader);
		
		int strCounter = -1;
		String line;
		
		textPane.setText("");
		
		StyledDocument doc = textPane.getStyledDocument();
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.BLACK);
		StyleConstants.setBackground(keyWord, Color.YELLOW);
		StyleConstants.setBold(keyWord, true);
		
		int j = 0;
		
		while ((line = fileReader.readLine()) != null) {
			strCounter += 1;
			if (j < array.size()){
				if (strCounter == array.get(j).get(0)){
					int i = 0;
					int k = 2;
					while (i < line.length()){
						if (k < array.get(j).size()) {
							if (i == array.get(j).get(k)) {
								try {
									doc.insertString(doc.getLength(),
											String.valueOf(line.substring(i, i + word.length())), keyWord);
								} catch (BadLocationException ble) {ble.printStackTrace();}
								k += 1;
								i += word.length();
							}
							else {
								try {
									doc.insertString(doc.getLength(), String.valueOf(line.charAt(i)), null);
								} catch (BadLocationException ble){ble.printStackTrace();}
								i +=1;
							}
						}
						else {
							try {
								doc.insertString(doc.getLength(), String.valueOf(line.charAt(i)), null);
							} catch (BadLocationException ble){ble.printStackTrace();}
							i +=1;
						}
					}
					try {
						doc.insertString(doc.getLength(), "\n", null);
					} catch (BadLocationException ble){ble.printStackTrace();}
					j+=1;
				}
				else {
					try {
						doc.insertString(doc.getLength(), line+"\n", null);
					} catch (BadLocationException ble){ble.printStackTrace();}
				}
			}
			else {
				try {
					doc.insertString(doc.getLength(), line+"\n", null);
				} catch (BadLocationException ble){ble.printStackTrace();}
			}
		}
	}
	
	public static ArrayList<ArrayList<Integer>> finder(String filePath, String word) {
		
		ArrayList<ArrayList<Integer>> indList = new ArrayList<>();
		
		try {
			
			int str = 0;
			int pos = 0;
			FileReader fileIn = new FileReader(filePath);
			BufferedReader reader = new BufferedReader(fileIn);
			String line;
			
			while ((line = reader.readLine()) != null) {
				if ((line.contains(word))) {
					ArrayList<Integer> indexesS = new ArrayList<>();
					indexesS.add(str);
					indexesS.add(pos);
					int index = line.indexOf(word);
					while (index >= 0) {
						indexesS.add(index);
						index = line.indexOf(word, index + word.length());
					}
					indList.add(indexesS);
					pos += line.length();
				}
				else {
					pos += line.length();
				}
				str += 1;
			}
			return indList;
			
			
		} catch (IOException e) {e.printStackTrace();}
		return indList;
		
	}
	
	public static boolean isInFile(String path, String word, String ext){
		
		String[] ar = path.split("/");
		boolean isNeeded = false;
		
		if (ar[ar.length-1].endsWith(ext)) {
			try {
				
				FileReader fileIn = new FileReader(path);             //file
				BufferedReader reader = new BufferedReader(fileIn);
				String line;
				
				while ((line = reader.readLine()) != null) {
					if ((line.contains(word))) {
						isNeeded = true;
						break;
					}
				}
			} catch (IOException e) {e.printStackTrace();}
		}
		
		return isNeeded;
	}
	
}