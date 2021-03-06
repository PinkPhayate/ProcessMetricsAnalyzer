package GUI;

import javax.swing.*;
import java.io.File;
import java.awt.BorderLayout;
import java.awt.event.*;
import main.DiffAnalyzerMain;
import main.DiffAnalyzerMainThread;
public class JFileChooserTest5 extends JFrame implements ActionListener{

	JLabel label;
	JPanel buttonPanelLower  = null;
	JButton buttonUpper = null;
	JButton buttonLower = null;
	JTextField upperText  = null;
	JTextField lowerText  = null;
	JButton commenceButton = null;
	JProgressBar bar = null;
	
	String softwareType = "cs";

	public static void main(String[] args){
		JFileChooserTest5 frame = new JFileChooserTest5();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(10, 10, 300, 200);
		frame.setTitle("DIMA");
		frame.setVisible(true);
	}

	JFileChooserTest5(){
		label = new JLabel();
		JPanel labelPanel = new JPanel();
		labelPanel.add(label);
		getContentPane().add(labelPanel, BorderLayout.CENTER);


		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		this.buttonUpper = new JButton("Browse");
		this.buttonUpper.setBounds(210,10,80,20);
		this.buttonUpper.addActionListener(this);
		buttonPanel.add(this.buttonUpper);
		getContentPane().add(buttonPanel, BorderLayout.CENTER);

		JPanel p = new JPanel();

		this.upperText = new JTextField(null);
		this.upperText.setBounds(10, 10, 200, 20);
		buttonPanel.add(this.upperText);    
		this.upperText.setLayout(null);


		buttonPanelLower = new JPanel();
		buttonPanelLower.setLayout(null);
		buttonLower = new JButton("Browse");
		buttonLower.setBounds(210,50,80,20);
		buttonLower.addActionListener(this);
		buttonPanel.add(buttonLower);
		
		this.lowerText = new JTextField(null);
		this.lowerText.setBounds(10, 50, 200, 20);
		buttonPanel.add(this.lowerText);    
		this.lowerText.setLayout(null);

		this.commenceButton = new JButton("Analize");
		this.commenceButton.setBounds(110,90,80,20);
		this.commenceButton.addActionListener(this);
		buttonPanel.add(this.commenceButton);
		getContentPane().add(buttonPanel, BorderLayout.CENTER);
		
		this.bar = new JProgressBar();
		this.bar.setLayout(null);
		this.bar.setValue(0);
		this.bar.setBounds(10, 180, 280, 20);
		buttonPanel.add(this.bar);

	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == this.commenceButton){
			this.commenceAnalyze();
			return;
		}
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int selected = filechooser.showOpenDialog(this);
		if (selected == JFileChooser.APPROVE_OPTION){
			File file = filechooser.getSelectedFile();
			if(e.getSource() == this.buttonUpper){
				upperText.setText(file.getPath());
			}
			if(e.getSource() == this.buttonLower){
				lowerText.setText(file.getPath());
			}
		}

		//    }else if (selected == JFileChooser.CANCEL_OPTION){
		//      label.setText("キャンセルされました");
		//    }else if (selected == JFileChooser.ERROR_OPTION){
		//      label.setText("エラー又は取消しがありました");
		//    }
	}

	private void commenceAnalyze() {
		if (upperText.getText() == null || lowerText.getText() == null) {
			
		}
		else {
		    bar = new JProgressBar();
		    bar.setValue(0);
		    bar.setMaximum(200);

			String[] args = { 
					upperText.getText(),
					lowerText.getText(),
					softwareType
			};
			DiffAnalyzerMainThread diffAnalyzerMainThread
				= new DiffAnalyzerMainThread (args);
			diffAnalyzerMainThread.start();
		}
	}

}