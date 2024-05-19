import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
public class Server
{
	
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	
	public Server()
	{
		try {
			ServerSocket server=new ServerSocket(7777);
			System.out.println("server is ready to accept connection");
			System.out.println("waiting...");
			socket=server.accept();
			
			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out=new PrintWriter(socket.getOutputStream());
			
			startReading();
			startWriting();
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	public void startReading()
	{
		//thread for reading data
		Runnable r1=()->
		{
			System.out.println("reader started");
			try 
			{
			while(true)
			{
					String msg = br.readLine();
					if(msg.equals("exit"))
					{
						System.out.println("client terminated the chat");
						socket.close();
						break;
					}
					System.out.println("Client : "+msg);
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
			while(!socket.isClosed())
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
	public static void main(String gg[])
	{
		System.out.println("hello this is our page");
		new Server();
	}
}