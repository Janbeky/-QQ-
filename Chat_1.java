/*
 * 
 * ��  �ܣ�����Text���ڣ�������Ϣ���͵�Server�ˣ�
 * �����շ���˴���������
 * ��  �ߣ�����
 * ��  ����1.0
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
	 * �������ڣ����ó�ʼλ�úͳߴ�
	 * ���������������ô��ڼ���
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
		
		//���ڿɼ�����Socket����
		Connet();
		
		//����һ�������̲߳�����
		new Thread(new tfListener().new RecvThread()).start();
	}
	
	
	 //���ӷ���ˣ�����ȡ����˵�����������
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
	
	
	//������disConnet����ʱ���ر�����˺�����ˣ��ر�Socket��
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
		
        //����ʱ�������������TextArea�е��ַ�����ֵ��Str
		//�����area�е��ַ������͸������
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
		
		
		//���������࣬ʵ��Runnable�ӿ�
		//��дrun������һֱ��ȡ�������˷������ַ���
		//�����ַ�����ʾ��TextArea����
		private class RecvThread implements Runnable{

			public void run() {
				try {
				while(bConneted) {
					
						String str = dis.readUTF();
						ta.setText(ta.getText()+str+"\r\n");	
				}
					
				} catch (SocketException e) {
					System.out.println("�˳���");
				} catch (IOException e) {
						e.printStackTrace();
					}
					
				}	
			}	
	}	
}