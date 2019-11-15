package assignment_3;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

public class Receiver { //server
	
	/**
	 * Creates a server socket to receive an xml document over the network.
	 * @return The received document. Null if something goes horribly wrong.
	 */
	public static Document receiveDoc() {
		Document doc = null;
		SAXBuilder docBuilder = new SAXBuilder();
		
		//Getting port number for connection
		Scanner in = new java.util.Scanner(System.in);
		System.out.print("Enter a port number: ");
		int portNum;
		
		while(true) {
			
			try{
				portNum = in.nextInt();
				if(portNum >= 0 && portNum <= 65535) {
					break;
				}
				else {
					System.out.print("Port must be between 0 and 65535!\nPlease try again: ");
				}
			}
			catch(InputMismatchException e) {
				System.err.print("Port must be an integer!\nPlease try again:");
			}
			catch(Exception e) {
				System.err.println("Error reading port number.\nQuitting!");
				e.printStackTrace();
				in.close();
				System.exit(-1);
			}
			
		}
		
		in.close();
		
		//creating server socket, and then client socket that has connected
		try(
				ServerSocket serverSocket = new ServerSocket(portNum);
				Socket clientSocket = serverSocket.accept();
				BufferedReader sockIn = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
				)
		
		{
			String inputLine;
			StringBuilder stringDoc = new StringBuilder();
			
			//getting string version of xml document
			while((inputLine = sockIn.readLine()) != null) {
				stringDoc.append(inputLine);
			}
			
			InputStream strStream = new ByteArrayInputStream( stringDoc.toString().getBytes("UTF-8") );
			doc = docBuilder.build(strStream);
		}
		catch(Exception e) {
			System.err.println("Error creating socket.\nQuitting!");
			e.printStackTrace();
			System.exit(-1);
		}
		return doc;
	}
	
}
