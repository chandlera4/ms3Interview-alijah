package com.alijah.MS3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.io.FileReader;
import java.io.FileWriter;

public class MS3 {

	public static void main(String[] args) throws SQLException, IOException {
		String currentDir = (System.getProperty("user.dir")+"\\src\\com\\alijah\\MS3\\");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String fileTimestamp = timestamp.toString().replace(" ", "_").replace(":", "_");
		BufferedWriter badDataWriter = new  BufferedWriter(new FileWriter(currentDir+"bad-data-"+fileTimestamp+".csv"));
		BufferedWriter logWriter = new  BufferedWriter(new FileWriter(currentDir+"log.txt"));
		String line = "";
		Connection con = DriverManager.getConnection("jdbc:sqlite:"+currentDir+"ms3db.db","","");;
		Statement stmt = con.createStatement();
		System.out.println("Starting...");
		
		try (BufferedReader br = new BufferedReader(new FileReader(currentDir+"ms3Interview.csv"))){
			
			line = br.readLine();
			
			String[] row = line.split(",");
			String columnNames = "("+row[0]+", "+row[1]+", "+row[2]+", "+row[3]+", "+row[4]+", "+row[5]+", "+row[6]+", "+row[7]+", "+row[8]+", "+row[9]+")";
			int recordsReceived = 0;
			int recordsSuccessful  = 0;
			int recordsFailed  = 0;
			
		while ((line = br.readLine()) != null) {
			recordsReceived = recordsReceived+1;
			row = line.split(",");
			
			for (int i=0; i<row.length; i++) {
			if(row[i].contains(",")) {
				row[i] = '"'+row[i]+'"';			
			}
			}
			
			if(row[0].length() > 0 && row[1].length() > 0 && row[2].length() > 0 && row[3].length() > 0 && row[4].length() > 0 && row[5].length() > 0 && row[6].length() > 0 && row[7].length() > 0 && row[8].length() > 0 && row[9].length() > 0) {
			recordsSuccessful = recordsSuccessful+1;
			String entry = "insert into X "+columnNames+" values "+"("+"'"+row[0]+"'"+", "+"'"+row[1]+"'"+", "+"'"+row[2]+"'"+", "+"'"+row[3]+"'"+", "+"'"+row[4]+"'"+", "+"'"+row[5]+"'"+", "+"'"+row[6]+"'"+", "+"'"+row[7]+"'"+", "+"'"+row[8]+"'"+", "+"'"+row[9]+"'"+")";
			stmt.executeUpdate(entry);
			}
			else {
				recordsFailed = recordsFailed+ 1;
				badDataWriter.write(line+"\r\n"+"\r\n");
			}
		}
		System.out.print("Records received: "+recordsReceived+"\r\n"+"Records successful: "+recordsSuccessful+"\r\n"+"Records failed: "+recordsFailed);
		logWriter.write("Records received: "+recordsReceived+"\r\n"+"Records successful: "+recordsSuccessful+"\r\n"+"Records failed: "+recordsFailed);
		logWriter.close();
		badDataWriter.close();
		stmt.close();
		}
		
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}