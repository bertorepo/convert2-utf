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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadFile {

	public static void main(String[] args) throws Exception {

		String sourcedirectoryName = "";
		String targetdirectoryName = "";
		String dataTextFiles = "";

		try {
			List<String> arrConfig = new ArrayList<String>();
			
			//convert the config file first to UTF8
			List<String> configList = getTextData("config.txt");
			writeTextData("config.txt", configList);

			Map<String, String> mappedConfig = readFile("config.txt", "=");

			mappedConfig.keySet().stream().forEach(con -> {
				arrConfig.add(mappedConfig.get(con));
			});

			sourcedirectoryName = replaceFilePath(arrConfig.get(2));
			targetdirectoryName = replaceFilePath(arrConfig.get(1));
			
			//convert the data text File first to UTF8
			String initialTextFile = replaceFilePath(arrConfig.get(0));
			List<String> textDataFilesArr = getTextData(initialTextFile);
			writeTextData(initialTextFile, textDataFilesArr);
			
			dataTextFiles = initialTextFile;

			File sdir = new File(sourcedirectoryName);
			File tdir = new File(targetdirectoryName);

			if (!sdir.exists()) {
				System.out.println("Error: Source Folder Directory Not Found!");
			} else if (!tdir.exists()) {
				System.out.println("Error: Target Folder Directory Not Found!");
			} else if (!new File(dataTextFiles).exists()) {
				System.out.println("Error: Data Text File Not Found!");
			} else {
				System.out.println("---------------------------------");
				System.out.println("--> SOURCE DESTINATION PATH : OK");
				System.out.println("--> TARGET DESTINATION PATH : OK");
				System.out.println("--> DATA TEXT FILE : OK");
				System.out.println("---------------------------------");
				System.out.println("\n");
				System.out.println("--> STARTING TO RENAME FILES...");
			}

			// copy files into target directory
			validateFiles(sdir, tdir);
			
			//rename file in the target directory
			if (tdir.list().length > 0) {
				renameFiles(targetdirectoryName, dataTextFiles);
			}

		} catch (Exception e) {
			System.out.println("There's a problem renaming the File");
			System.out.println("Cause: " + e.getMessage());
		}
	}

	// replace file path with double backslash(\\)
	private static String replaceFilePath(String s) {
		return s.replaceAll("[^A-Za-z0-9:._\\s]", "\\\\\\\\");
	}

	// this method will execute renaming the files
	private static void renameFiles(String folderPath, String textFile) throws IOException {
		File myFolder = new File(folderPath);
		File[] file_array = myFolder.listFiles();

		for (int i = 0; i < file_array.length; i++) {
			if (file_array[i].isFile()) {

				File myFile = new File(folderPath + "\\" + file_array[i].getName());
				String originalFileName = file_array[i].getName();
				String[] splittedText = originalFileName.split("_", 2);

				// this maptextFiles will compare the ip address text from the tables.txt to the
				// ip address in individual filename
				Map<String, String> mappedTextFiles = readFile(textFile, "\\s+");

				mappedTextFiles.keySet().stream().forEach(ip -> {

					if (ip.equals(splittedText[0])) {
						String renamedFile = mappedTextFiles.get(ip) + "_" + splittedText[1];
						String newFilePath = folderPath + "\\" + renamedFile;
						System.out.println(originalFileName + " --> " + renamedFile);

						try {
							Files.deleteIfExists(Paths.get(newFilePath));
							myFile.renameTo(new File(newFilePath));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});

			}
		}
		System.out.println("\n");
		System.out.println("--> ALL FILES RENAMED SUCCESSFULLY...");
		System.out.println("--> LOCATION PATH: " + myFolder.getAbsolutePath());
	}

	private static Map<String, String> readFile(String fileName, String separator) {
		BufferedReader reader = null;
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;

		Map<String, String> pathList = new HashMap<String, String>();

		try {
			String str;
			fileInputStream = new FileInputStream(fileName);
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);

			while ((str = reader.readLine()) != null) {
				String[] data = str.split(separator);
				pathList.put(data[0], data[1]);

			}
		} catch (Exception e) {
			System.out.println("There's a problem renaming the File");
			System.out.println("Cause: " + e.getMessage());
		}

		return pathList;
	}

	private static void validateFiles(File sdir, File tdir) throws IOException {

		if (sdir.isDirectory()) {
			copyFilesfromDirectory(sdir, tdir);
		} else {
			Files.copy(sdir.toPath(), tdir.toPath());
		}
	}

	private static void copyFilesfromDirectory(File source, File target) throws IOException {

		if (!target.exists()) {
			target.mkdir();

		} else {
			for (String items : source.list()) {
				validateFiles(new File(source, items), new File(target, items));
			}
		}
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
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

}
