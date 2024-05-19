import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class Client {
	
	BufferedReader br;
	PrintWriter out;
	Socket socket;
	
	public Client()
	{
		try
		{
			System.out.println("sending request to server");
			socket=new Socket("127.0.0.1",7777);
			System.out.println("connection done.");
			
			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out=new PrintWriter(socket.getOutputStream());
			
			startReading();
			startWriting();
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void startReading()
	{
		//thread for reading data
		Runnable r1=()->
		{
			System.out.println("reader started");
			try {
			while(true)
			{
					String msg = br.readLine();
					if(msg.equals("exit"))
					{
						System.out.println("Server terminated the chat");
						socket.close();
						break;
					}
					System.out.println("Server : "+msg);
			}
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("connection closed ");
			}
		};
		
		new Thread(r1).start();
	}
	public void startWriting()
	{
		// thread for writing 
		Runnable r2=()->
		{
			System.out.println("writer started ");
			try
			{
			while( !socket.isClosed())
			{		
					BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
					String content=br1.readLine();
					out.println(content);
					out.flush();
					
					if(content.equals("exit"))
					{
						socket.close();
						break;
					}
			}
			}catch(Exception e)
			{
				//e.printStackTrace();
				System.out.println("connection closed ");
			}
		};
		
		new Thread(r2).start();
	}
	public static void main(String[] args) {
		new Client();
	}

}
