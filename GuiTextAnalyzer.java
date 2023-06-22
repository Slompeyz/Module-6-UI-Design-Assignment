/*
* Programmer: Richard Caraballo
* Description:  This program pulls the poem "The Raven" from a URL, keeps track of the number of times each
* 				word appears in the poem, and displays a GUI that lets users choose to see the top 5, 10, 15, or 20
* 				words that appear.
* Just in case: This program was made with jdk-13.0.1 as the JRE, javafx-sdk-11.0.2, and e(fx)clipse 3.8.0
* 				All for Windows
*/

package application;

//required for text analyzer
import java.net.*;
import java.io.*;
import java.util.*;

//required for GUI
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class GuiTextAnalyzer extends Application {
	
	//used in the main method to sort the hashmap of words and their frequency
	//made static to allow its values to be used in the button events
	static LinkedHashMap<String, Integer> sortedWordMap = new LinkedHashMap<>();

	public static void main(String[] args) throws Exception{
			
		URL url = new URL("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
		//must be encoded as UTF-8 to allow open and closed quotation marks from HTML to be read properly by Eclipse
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			
		String text = reader.readLine();
		boolean poemText = false;
			
		//stores each individual word in the poem
		String[] wordArray;
		//hashmap and variables for counting words
		HashMap<String,Integer> wordCountMap = new HashMap<String,Integer>();
		boolean match = false;
		String matchKey = "";
			
		//while the end of the text from the URL has not been reached, the "text" variable is set to each line
		//on the page and then removes that line's tags and punctuation
		while(text != null) {
				
			//removes html tags and replaces mdashes with spaces
			text = text.replaceAll("<.*?>", "");
			text = text.replace("&mdash;", " ");
				
			//removes apostrophes when they're not being used as contractions
			//to get rid of single-quotes
			text = text.replace("‘", "");
			text = text.replaceAll("’(?![[a-z][A-Z]])", "");
				
			//sets poemText to true or false when the beginning and end of the poem are reached
			//this lets us keep track of just the poem's text
			if(text.equals("The Raven")) {
				poemText = true;
			}
			if(text.equals("*** END OF THE PROJECT GUTENBERG EBOOK THE RAVEN ***")) {
				poemText = false;
			}
				
			//if the "text" variable is currently reading part of the poem, the line is split into individual words
			//which are stored in wordCountMap as keys, and their values are set to how many times they've appeared
			if(poemText) {
					
				//splits each line of text into individual words which are stored in wordArray
				wordArray = text.split("[ “”;!?.,-:]+");
					
				//loops through the array of every word in the poem
				for(int i = 0; i < wordArray.length; i++) {
						
					//capitalizes the first letter of every word in wordArray
					if(!wordArray[i].equals("")) {
						wordArray[i] = (wordArray[i].substring(0,1)).toUpperCase() + wordArray[i].substring(1);
					}
						
					//compares the current word in wordArray to every value in the hashmap (which stores one of each word)
					//and notes if there's a match (this will not run for the first word in wordArray)
					for(String word : wordCountMap.keySet()) {
						if(word.equals(wordArray[i])) {
							match = true;
							matchKey = word;
						}
					}
					//if the current word in wordArray matches a word in the hashmap, the counter for
					//the word in the hashmap increases
					//otherwise, the word has not been seen yet and is added to the hashmap
					if(match) {
						wordCountMap.put(matchKey, wordCountMap.get(matchKey) + 1);
						match = false;
					}else {
						wordCountMap.put(wordArray[i], 1);
					}
						
						
						
				}
					
			}
				
			//iterator moves to the next line of the text
			text = reader.readLine();
		}
		reader.close();
			
		//removes the blank lines from the hashmap
		wordCountMap.remove("");

		//sorts hashmap by adding its word count values to an arraylist sorted by Collections
		//the order is reversed so the highest word counts appear first
		ArrayList<Integer> sortList = new ArrayList<>();
			
		for(Map.Entry<String,Integer> word : wordCountMap.entrySet()) {
			sortList.add(word.getValue());
		}
			
		Collections.sort(sortList);
		Collections.reverse(sortList);
			
		//the sorted arraylist is used to fill a LinkedHashMap (sortedWordMap) in order of highest to lowest word count
		//with the matching words being pulled from the hashmap
		for(int listValue: sortList) {
			for(Map.Entry<String,Integer> word : wordCountMap.entrySet()) {
				if(word.getValue() == listValue) {
					sortedWordMap.put(word.getKey(), listValue);
				}
			}
		}	
		
		//starts GUI
		launch(args);
	}
	
	//component initialization for the TilePane GUI and button events
	public void start(Stage primaryStage) throws Exception{
		try {
			
			TilePane pane = new TilePane();
			pane.setTileAlignment(Pos.CENTER);
			
			//creating buttons and labels to add to the GUI
			Label leftLabel = new Label("How many words from the");
			Label rightLabel = new Label(" Raven do you want to see?");
			leftLabel.setFont(Font.font("ariel",FontWeight.BOLD,FontPosture.REGULAR,15));
			rightLabel.setFont(Font.font("ariel",FontWeight.BOLD,FontPosture.REGULAR,15));
			
			Button[] buttonArray = new Button[] {
				new Button(" Top 5 Words "),
				new Button("Top 10 Words"),
				new Button("Top 15 Words"),
				new Button("Top 20 Words")
			};
			
			Label[] wordLabelArray = new Label[] {
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label(),
					new Label()
			};
			
			pane.getChildren().add(leftLabel);
			pane.getChildren().add(rightLabel);
			pane.getChildren().addAll(buttonArray);
			pane.getChildren().addAll(wordLabelArray);
			
			//adding the layout with all of its components into the scene
			Scene scene = new Scene(pane,410,360);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Top X Words");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			//handling button events for:
				//top 5 words
				buttonArray[0].setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						labelChange(5, wordLabelArray);
					}
				});
				//top 10 words
				buttonArray[1].setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						labelChange(10, wordLabelArray);
					}
				});
				//top 15 words
				buttonArray[2].setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						labelChange(15, wordLabelArray);
					}
				});
				//top 20 words
				buttonArray[3].setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						labelChange(20, wordLabelArray);
					}
				});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	//changes labels to the first x entries from the sorted linkedhashmap (sortedWordMap)
	public void labelChange(int numEntries, Label[] labelArray) {
		
		//clears the previous labels in case user has already hit a button
		for(int i = 0; i < 20; i++) {
			labelArray[i].setText("");
		}
		
		//uses an iterator to cycle through the linkedhashmap of words and sets label text to the
		//most frequent words in order
		Iterator<Map.Entry<String,Integer>> mapIterator = sortedWordMap.entrySet().iterator();
		
		for(int i = 0; i < numEntries; i++) {
			Map.Entry<String,Integer> word = mapIterator.next();
			labelArray[i].setText(word.getKey() + ": " + word.getValue());
			
		}
	}
	
}
