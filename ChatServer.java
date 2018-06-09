/*
 * 
 * 功  能：获取客户端输入并传回各个客户端
 * 作  者：陈振东
 * 版  本：1.0
 * 
 * */
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	ServerSocket ss = null;
	List<Client> Clients = new ArrayList<Client>();
	boolean started = false;
	

	public static void main(String[] args) {
		new ChatServer().start();
		
	}
	
	/*
	 * 重写start方法，设置Boolean来控制循环执行
	 * 并创建服务端后接受客户端连接后new一个线程
	 * 
	 * */
	public void start() {
		
		try{
			ss = new ServerSocket(8888);
			started = true;
		
		}catch (BindException e) {
			System.out.println("端口已经在使用中...请关掉服务器重新运行程序");
			System.exit(0);
		}catch (IOException e){
			e.printStackTrace();
		}
		
		try {	
			//建立while循环，监听Socket端口并同意连接
			while(started) {
				Socket s = ss.accept();
				
				//将Socket对象s添加到Client[]数组中，每添加一个输出一句提示语句
				Client c = new Client(s);
				Clients.add(c);
                System.out.println("A client has been connected");
                
                //将Socket对象传入线程并启动
                new Thread(c).start();
			}
		} catch(IOException e){
			e.printStackTrace();
			
		}
	  }
	
	
    //创建Client类实现Runnable接口
	class Client implements Runnable{
		private Socket s;
		private DataInputStream dis;
		private DataOutputStream dos;
		boolean canTransmission = false;
		
		
		/*
		 *重写run方法，读取输入端的字符串
		 *遍历Clients数组，将获取的字符串发送给每个客户端
		 *当接受完毕之后关闭Socket连接和输入输出流
		 * */
		public void run() {
			try {
				while(canTransmission) {
	            	 String str = dis.readUTF();
                     System.out.println(str);
                     
                 for(int i = 0; i<Clients.size(); i++) {
                	 Client c = Clients.get(i);
                	 c.Send(str);
                 }
	               }

			  // }catch(EOFException e) {
				   
	           }catch(Exception e) {
	            	System.out.println("connet closed");
	           }finally{
	        	   
					try {
						if(s != null)s.close();
						if(dis != null)dis.close();
						if(dos != null)dos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}	 	
			    }
		     }	
		
		
		/*
		 *创建Client构造方法，接受Socket对象
		 *并初始化端口，并获取其输入端和接收端
		*/

		public Client(Socket s) {
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				canTransmission = true;	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	    
		//创建Send方法，并将字符串写入文件输出端
		public void Send(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				Clients.remove(this);
				System.out.println("一个Client退出了，并从Client数组中移除");
				//e.printStackTrace();
			}
		}
	}
}