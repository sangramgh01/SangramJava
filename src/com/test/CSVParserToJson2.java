package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CSVParserToJson2 {
	public static void main(String[] args) throws Exception {
		parseCsv("D:\\events_2018072313h.csv","D:\\events_2018072313h.json");
	}
	public static  ObjectMapper parseCsv(String inputfile,String outputFile){
		BufferedReader reader = null;
		CSV csv;
		List<String> fieldNames = null;
		List<Map<String,String>> csvlist = new ArrayList<>(); 
		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		try {
			reader = Files.newBufferedReader(Paths.get(inputfile));
			csv = new CSV(true, ',', reader);
			if (csv.hasNext()) 
		    	fieldNames = new ArrayList<>(csv.next());
		    while (csv.hasNext()) {
		        List < String > x = csv.next();
		        if(x.size() != fieldNames.size()) {
		        	x.add("");
		        }
		        Map < String, String > obj = new LinkedHashMap < > ();
		        for (int i = 0; i < fieldNames.size(); i++) {
		            obj.put(fieldNames.get(i), x.get(i));
		        }
		        csvlist.add(obj);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception ae) {
			ae.printStackTrace();
			 
		}
		long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		System.out.println(afterUsedMem-beforeUsedMem);
	    ObjectMapper mapper = new ObjectMapper();
	   // mapper.enable(SerializationFeature.INDENT_OUTPUT);
	    mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
	   /* 
		try {
			
			
			mapper.writeValue(new File(outputFile),csvlist);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		FileWriter fw;
		
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(outputFile, false);
			bw = new BufferedWriter(fw);
			System.out.println(csvlist.size());
			for(Map<String,String> obj : csvlist) {
			bw.write(mapper.writeValueAsString(obj));
			bw.newLine();
			}
			bw.close();
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		
		/*try {
			String arrayToJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(csvlist);
			System.out.println(arrayToJson);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return mapper;
	}
}
