package GUI;

import javax.swing.*;
import java.io.File;
import java.awt.BorderLayout;
import java.awt.event.*;
import main.DiffAnalyzerMain;
public class JFileChooserTest5 extends JFrame implements ActionListener{

	JLabel label;
	JPanel buttonPanelLower  = null;
	JButton buttonUpper = null;
	JButton buttonLower = null;
	JTextField upperText  = null;
	JTextField lowerText  = null;
	JButton commenceButton = null;
	
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
		this.buttonUpper.setBounds(200,10,80,20);
		this.buttonUpper.addActionListener(this);
		buttonPanel.add(this.buttonUpper);
		getContentPane().add(buttonPanel, BorderLayout.CENTER);

		JPanel p = new JPanel();

		this.upperText = new JTextField(null);
		this.upperText.setBounds(0, 10, 200, 20);
		buttonPanel.add(this.upperText);    
		this.upperText.setLayout(null);


		buttonPanelLower = new JPanel();
		buttonPanelLower.setLayout(null);
		buttonLower = new JButton("Browse");
		buttonLower.setBounds(200,50,80,20);
		buttonLower.addActionListener(this);
		buttonPanel.add(buttonLower);
		
		this.lowerText = new JTextField(null);
		this.lowerText.setBounds(0, 50, 200, 20);
		buttonPanel.add(this.lowerText);    
		this.lowerText.setLayout(null);

		this.commenceButton = new JButton("Start");
		this.commenceButton.setBounds(110,90,80,20);
		this.commenceButton.addActionListener(this);
		buttonPanel.add(this.commenceButton);
		getContentPane().add(buttonPanel, BorderLayout.CENTER);

	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == this.commenceButton){
			this.commenceAnalyze();
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
			String[] args = { 
					upperText.getText(),
					lowerText.getText(),
					softwareType
			};
			DiffAnalyzerMain.main(args);
		}
	}

}