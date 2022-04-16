/*
 * Author: Hanyi Gao <hanyig1@student.unimelb.edu.au>
 * Student ID: 1236476
 */

package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class Dictionary {
	
	private final static String DEFAULT_DICT_PATH = "defaultDict.dat";
	private String dictPath;
	private HashMap<String, String> store;
	
	public Dictionary(String dictPath) {
		this.dictPath = dictPath;
		ObjectInputStream reader;
		try {
			reader = new ObjectInputStream(new FileInputStream(dictPath));
			this.store = (HashMap<String, String>) reader.readObject();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("The dictionary file is not found! Looking for default dictionary");
			getDictFile(DEFAULT_DICT_PATH);
		} catch (ClassNotFoundException e) {
			System.out.println("Error: ClassNotFoundException");
			getDictFile(DEFAULT_DICT_PATH);
		} catch(Exception e) {
			System.out.println("Errot");
			e.printStackTrace();
		}
	}
	
	private void getDictFile(String dictPath) {
		ObjectInputStream reader;
		try {
			reader = new ObjectInputStream(new FileInputStream(dictPath));
			this.store = (HashMap<String, String>) reader.readObject();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("The dictionary file is not found!");
			createDictFile(DEFAULT_DICT_PATH);
		} catch (ClassNotFoundException e) {
			System.out.println("Error: ClassNotFoundException");
			createDictFile(DEFAULT_DICT_PATH);
		} catch(Exception e) {
			System.out.println("Errot");
			e.printStackTrace();
		}
	}
	
	private void createDictFile(String dictPath) {
		this.store = new HashMap<String, String>();
		this.dictPath = dictPath;
		ObjectOutputStream writer;
		try {
			writer = new ObjectOutputStream(new FileOutputStream(dictPath));
			writer.writeObject(store);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isWordInDict(String word) {
		return store.containsKey(word);
	}
	
	private void updateDictFile() throws FileNotFoundException, IOException {
		ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(dictPath));
		writer.writeObject(store);
		writer.close();
	}
	
	public synchronized String query(String word) {
		if (!isWordInDict(word))
			return "";
		return store.get(word);
	}
	
	public synchronized boolean add(String word, String meaning) throws IOException {
		if (isWordInDict(word))
			return false;
		store.put(word, meaning);
		updateDictFile();
		return true;
	}
	
	public synchronized boolean remove(String word) throws IOException {
		if (!isWordInDict(word))
			return false;
		store.remove(word);
		updateDictFile();
		return true;
	}
	
	public synchronized boolean update(String word, String meaning) throws IOException {
		if (!isWordInDict(word))
			return false;
		store.put(word, meaning);
		updateDictFile();
		return true;
	}
	
	
}
