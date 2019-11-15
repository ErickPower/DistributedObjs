package assignment_3;



import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.sound.midi.SysexMessage;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Sender { //client
	
	/**
	 * creates a client socket to send an xml document over the network to a server socket.
	 * @param doc the document to send over the network
	 */
	public static void sendDoc(Document doc) {
		String hostName = "assignment_3.Receiver";
		System.out.println("Document receiver MUST be currently running!!\n");
		//Getting the port number of the server:
		Scanner in = new java.util.Scanner(System.in);
		System.out.print("Enter the port number of the receiving socket: ");
		int portNum;
		while(true) {
			try{
				portNum = Integer.parseInt(in.nextLine());
				if(portNum >= 0 && portNum <= 65535) {
					break;
				}
				else {
					System.out.print("Port must be between 0 and 65535!\nPlease try again: ");
				}
			}
			catch(NumberFormatException e) {
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
		
		//Creating socket for connection: 
		try(
				Socket sendSock = new Socket(hostName, portNum);
				PrintWriter out = new PrintWriter(sendSock.getOutputStream(), true);
		){
			//Sending contents of doc as string.
			XMLOutputter myXMLOutput = new XMLOutputter();
			myXMLOutput.setFormat(Format.getRawFormat());
			out.println(myXMLOutput.outputString(doc));
		}
		catch (Exception e) {
			System.err.println("Error in send socket creation.\nQuitting!");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	
	
}
