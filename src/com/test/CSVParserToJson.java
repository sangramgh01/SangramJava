package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.JsonFlattener;

public class CSVParserToJson {
	public static void main(String[] args) throws Exception {
		parseCsv("D:\\events_2018072313h.csv","D:\\events_2018072313h.json","abc");
	}
	public static  ObjectMapper parseCsv(String inputfile,String outputFile,String clientName){
		BufferedReader reader = null;
		CSV csv;
		List<String> fieldNames = null;
		List<Map<String,String>> csvlist = new ArrayList<>(); 
		//long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		List < String > x = null;
		try {
			reader = Files.newBufferedReader(Paths.get(inputfile));
			csv = new CSV(true, ',', reader);
			if (csv.hasNext()) 
		    	fieldNames = new ArrayList<>(csv.next());
		    while (csv.hasNext()) {
		         x= csv.next();
		        while(x.size() != fieldNames.size()) {
		        	x.add("");
		        }
		        Map < String, String > obj = new LinkedHashMap < > ();
		        for (int i = 0; i < fieldNames.size(); i++) {
		            obj.put(fieldNames.get(i), x.get(i));
		        }
		        csvlist.add(obj);
		    }
		}catch (IOException e) {
			e.printStackTrace();
		}catch (Exception ae) {
			 System.out.println("Error File for clientName : " + clientName + " is : "+ inputfile);
			 ae.printStackTrace();
		}
		//long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		ObjectMapper  mapper = new ObjectMapper();
		BufferedWriter bw = null;
		try {
			FileOutputStream fos = new FileOutputStream(outputFile);
	        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			bw = new BufferedWriter(osw);
			bw.append("[");
			long counter = 0;
			for(Map<String,String> mapObj : csvlist) {
				counter++; 
				for(Map.Entry<String, String> map : mapObj.entrySet()) {
					String val = map.getValue();
					if(val!= null && val.length() > 0 && val.startsWith("\"") && val.endsWith("\"")){
						val = val.substring(1, val.length()-1);
					}
					mapObj.put(map.getKey(), val);
				}
				String jsonString = mapper.writeValueAsString(mapObj);
				jsonString = jsonString.replace("\\", "");
				jsonString = jsonString.replace("\"{", "{");
				jsonString = jsonString.replace("}\"", "}");
				System.out.println(jsonString);
				String jsonStr = JsonFlattener.flatten(jsonString);
				bw.write(jsonStr);
				if(counter  != csvlist.size()){
				   bw.append(",");
				}
				bw.newLine();
				
			}
			bw.append("]");
			if(bw != null) {
				bw.close();
			}
			if(osw != null) {
				osw.close();
			}
			if(fos != null) {
				fos.close();
			}
			System.out.println("--done--");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return mapper;
	}
}
