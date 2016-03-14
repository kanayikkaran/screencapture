package screencapture;

import java.awt.AWTException;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Execute {

	static String scenario;
	
	public static void capture(String testScenario, String filePath){
		
		String path = filePath; 
		String scenario = testScenario;
		
		Robot r = null; 
		try{		
		r = new Robot();
		}
		catch(AWTException e)
		{
		System.out.println("Unable to create Robot");
		}
		
		Date date = new Date();
		String format = "jpg";
		SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddHHmmss");
		Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage img = r.createScreenCapture(rect);
		
		try
		{
		ImageIO.write(img, format, new File(path+"//"+scenario+"_"+ft.format(date)+"."+format));
		}
		catch(FileNotFoundException e)
		{
		System.out.println("FileNotFoundException:"+e.getMessage());
		}
		catch(IOException e)
		{
		System.out.println("IOException:"+e.getMessage());
		}
		catch(Exception e)
		{
		System.out.println("Exception at Capture:"+e.getMessage());
		}

	}
	
	
	public static void main(String args[]) throws Exception
	{
		System.setOut(new PrintStream(new FileOutputStream("log.txt")));
		
		final JFrame frame = new JFrame("Capture");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		final JTextField folder = new JTextField("D:\\");
		final JTextField text = new JTextField("ScenarioName");
		JButton button = new JButton("Capture");
		
		frame.add(folder);
		frame.add(text);
		frame.add(button);
		
		frame.setLayout(new FlowLayout(FlowLayout.CENTER));
		frame.pack();
		
		frame.setVisible(true);
		
			frame.addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowLostFocus(WindowEvent arg0) {
				
			}
			
			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				
				frame.pack();
		
			}
		});
		
		button.addActionListener(new ActionListener()
		{
		
			public void actionPerformed(ActionEvent a)
			{
				boolean folderstatus = false;
				
				if(folder.getText().isEmpty())
				{
					folder.setText("D:\\");
					folderstatus = true;
				}
				else
				{
					File fold = new File(folder.getText());
					folderstatus = fold.exists();
					if(!folderstatus)
					{
						try
						{
						folderstatus = fold.mkdir();
						}
						catch(Exception e)
						{
						System.out.println("Exception in mkdir");	
						}
						
						System.out.println("Folder "+fold.getName()+" Created? "+folderstatus);
					}
				}
				
				if(text.getText().isEmpty())
					text.setText("Scenario");
				
				if(folderstatus)
				capture(text.getText(), folder.getText());
				else if(!folderstatus)
				System.out.println("Invalid Folder(s)! Image not Captured");
				
				
			}
				
		});
		
	}
	

}
