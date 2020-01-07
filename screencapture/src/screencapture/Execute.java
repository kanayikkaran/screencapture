package screencapture;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Execute {

	static String defaultScenario = "ScenarioName";
	static String defaultFolder = System.getProperty("user.dir");
	static int count = 0;
	
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
		String format = "png";
		SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd_HHmmss");
		/*To get full screen including the task bar*/
		/*Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage img = r.createScreenCapture(rect);*/
		Rectangle windowRect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		BufferedImage img = r.createScreenCapture(windowRect);
		
		try
		{
		ImageIO.write(img, format, new File(path+"//"+scenario+"_"+ft.format(date)+"."+format));
		count++;
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
	
	public static void writeToFile(String input) throws Exception
	{
		File file = new File(System.getProperty("user.dir")+"/file");
	    FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir")+"/file");
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(input);
	    oos.close();
	}
	
	public static String readFromFile() throws Exception
	{
		String output;
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/file");
		ObjectInputStream ois = new ObjectInputStream(fis);
		output = ois.readObject().toString();
		ois.close();
		return output;
	}
	
	public static void main(String args[]) throws Exception
	{
		System.setOut(new PrintStream(new FileOutputStream("log.txt")));
		
		final JFrame frame = new JFrame("Capture");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		final JTextField folder = new JTextField(defaultFolder);
		final JTextField text = new JTextField(defaultScenario);
		JButton button = new JButton("Capture");
		
		folder.setToolTipText("Enter folder path");
		text.setToolTipText("Enter scenario name");
		button.setToolTipText("Take screenshot");
		
		/*PromptSupport.setPrompt("Folder Path", folder);
		PromptSupport.setPrompt("Scenario Name", text);*/
		
		frame.add(folder);
		frame.add(text);
		frame.add(button);
		
		frame.setLayout(new FlowLayout(FlowLayout.CENTER));
		frame.pack();
		try
		{
			ImageIcon icon = new ImageIcon("\\lib\\record-icon.png");
			System.out.println("Icon Load Status : "+icon.getImageLoadStatus());
			frame.setIconImage(icon.getImage());
			//frame.setIconImage(ImageIO.read(new File("/lib/record-icon.png")));
			//getClass().getClassLoader().getResourceAsStream("\\icon.png");
		}
		catch(Exception e)
		{
			System.out.println("Error in reading icon image:"+e.getMessage());
		}
		frame.setVisible(true);
		
			frame.addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowLostFocus(WindowEvent arg0) {
				
			}
			
			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				if(folder.getText().isEmpty())
					folder.setText(defaultFolder);
				if(text.getText().isEmpty())
					text.setText(defaultScenario);
				frame.pack();
			}
		});
		
		button.addActionListener(new ActionListener()
		{
		
			public void actionPerformed(ActionEvent a)
			{
				boolean folderstatus = false;
				
				if(text.getText().isEmpty())
					text.setText(defaultScenario);
				
				if(folder.getText().isEmpty())
				{
					folder.setText(defaultFolder);
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
						if(folderstatus)
							System.out.println("Folder "+fold.getName()+" is created");
						else
							System.out.println("Folder "+folder.getText()+" is NOT created");
					}
				}
								
				if(folderstatus)
				{
				frame.setState(Frame.ICONIFIED);
				capture(text.getText(), folder.getText());
				}
				else if(!folderstatus)
				{
				folder.setText(defaultFolder);
				System.out.println("Invalid Folder(s)! Saved to "+folder.getText());
				frame.setState(Frame.ICONIFIED);
				capture(text.getText(), folder.getText());
				}
			}
		});
	}
}
