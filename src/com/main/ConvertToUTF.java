package com.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConvertToUTF {

	public static void main(String[] args) throws IOException {
		
		//this is the location where a user points out the file
		String fileName = "D:\\Java Project\\teratermlog_rename_updated\\File Sample\\table.txt";
		String newFile = "D:\\Java Project\\sample-file\\sample.txt";

		List<String> dataText = getTextData(fileName);
		writeTextData(newFile, dataText);

	}

	private static List<String> getTextData(String fileName) throws IOException {
		List<String> textData = new ArrayList<String>();

		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		FileInputStream fileInputStream = null;

		try {
			String str;
			fileInputStream = new FileInputStream(fileName);
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);

			while ((str = reader.readLine()) != null) {
				textData.add(str);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			inputStreamReader.close();
		}

		return textData;
	}

	private static void writeTextData(String fileName, List<String> textFiles) {
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
				randomAccessFile.setLength(0);
				randomAccessFile.close();
			}

			fw = new FileWriter(file, StandardCharsets.UTF_8);
			writer = new BufferedWriter(fw);

			for (String line : textFiles) {
				writer.append(line);
				writer.newLine();
				
			}
			
			System.out.println("Write SuccessFully");
			
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
