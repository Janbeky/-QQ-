/*
 * 
 * 功  能：监听Text窗口，并将信息发送到Server端，
 * 并接收服务端穿来的数据
 * 作  者：陈振东
 * 版  本：1.0
 * 
 * */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Chat_1 extends Frame {	
	TextField tf = new TextField();
	TextArea ta = new TextArea();
	Socket s = null;
	private boolean bConneted = false;
	
	DataOutputStream dos = null;
	
	DataInputStream dis = null;

	public static void main(String[] args) {
		new Chat_1().lauchFrame();
	}
	
	/*
	 * 创建窗口，设置初始位置和尺寸
	 * 设置两个区域，设置窗口监听
	 */
	public void lauchFrame() {
		this.setLocation(600, 300);
		this.setSize(600, 500);
		add(ta, BorderLayout.NORTH);
		add(tf, BorderLayout.SOUTH);
		pack();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				disConnet();
				System.exit(0);
			}
});
		tf.addActionListener(new tfListener());
		this.setVisible(true);
		
		//窗口可见后建立Socket连接
		Connet();
		
		//创建一个接收线程并启动
		new Thread(new tfListener().new RecvThread()).start();
	}
	
	
	 //连接服务端，并获取服务端的输入和输出端
	public void Connet() {
		try {
			 s = new Socket("192.168.1.105",8888);
			 dos = new DataOutputStream(s.getOutputStream());
			 dis = new DataInputStream(s.getInputStream());
			 System.out.println("---Connet---");
			 bConneted = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//当调用disConnet方法时，关闭输出端和输入端，关闭Socket端
	public void disConnet() {
		try {
			bConneted = false;
			//dis.close();
			dos.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	


	private class tfListener implements ActionListener{
		
        //创建时间监听方法，把TextArea中的字符串赋值给Str
		//并清空area中的字符串发送给服务端
		public void actionPerformed(ActionEvent e) {
			String str = tf.getText().trim();
			tf.setText("");
			try {
				dos.writeUTF(str);
				dos.flush();
				//sendServer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		
		//创建接收类，实现Runnable接口
		//重写run方法，一直读取服务器端发来的字符串
		//并将字符串显示在TextArea窗口
		private class RecvThread implements Runnable{

			public void run() {
				try {
				while(bConneted) {
					
						String str = dis.readUTF();
						ta.setText(ta.getText()+str+"\r\n");	
				}
					
				} catch (SocketException e) {
					System.out.println("退出了");
				} catch (IOException e) {
						e.printStackTrace();
					}
					
				}	
			}	
	}	
}