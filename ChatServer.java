/*
 * 
 * ��  �ܣ���ȡ�ͻ������벢���ظ����ͻ���
 * ��  �ߣ�����
 * ��  ����1.0
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
	 * ��дstart����������Boolean������ѭ��ִ��
	 * ����������˺���ܿͻ������Ӻ�newһ���߳�
	 * 
	 * */
	public void start() {
		
		try{
			ss = new ServerSocket(8888);
			started = true;
		
		}catch (BindException e) {
			System.out.println("�˿��Ѿ���ʹ����...��ص��������������г���");
			System.exit(0);
		}catch (IOException e){
			e.printStackTrace();
		}
		
		try {	
			//����whileѭ��������Socket�˿ڲ�ͬ������
			while(started) {
				Socket s = ss.accept();
				
				//��Socket����s��ӵ�Client[]�����У�ÿ���һ�����һ����ʾ���
				Client c = new Client(s);
				Clients.add(c);
                System.out.println("A client has been connected");
                
                //��Socket�������̲߳�����
                new Thread(c).start();
			}
		} catch(IOException e){
			e.printStackTrace();
			
		}
	  }
	
	
    //����Client��ʵ��Runnable�ӿ�
	class Client implements Runnable{
		private Socket s;
		private DataInputStream dis;
		private DataOutputStream dos;
		boolean canTransmission = false;
		
		
		/*
		 *��дrun��������ȡ����˵��ַ���
		 *����Clients���飬����ȡ���ַ������͸�ÿ���ͻ���
		 *���������֮��ر�Socket���Ӻ����������
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
		 *����Client���췽��������Socket����
		 *����ʼ���˿ڣ�����ȡ������˺ͽ��ն�
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
		
	    
		//����Send�����������ַ���д���ļ������
		public void Send(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				Clients.remove(this);
				System.out.println("һ��Client�˳��ˣ�����Client�������Ƴ�");
				//e.printStackTrace();
			}
		}
	}
}