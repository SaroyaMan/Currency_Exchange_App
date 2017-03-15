package com.yoav.currencyExchange;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * The CalculatorFrame class is a frame which takes part of the View. it represents the calculator
 * frame. The CalculatorFrame opened when the user wants to converts between 2 currencies.
 * @author Yoav Saroya
 */

public class CalculatorFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField amount;
	private JTextField result;
	private JTextField searchCurrCoin;
	private JTextField searchNewCoin;
	private JComboBox<String> convertFrom;
	private JComboBox<String> convertTo;
	
	private MainView mainView;
	private JTable table;
	private ArrayList<Currency> currencies;
	private Application controller;
	private ArrayList<String> codeList;
	
	public JComboBox<String> getConvertFrom() {return convertFrom;}
	public JComboBox<String> getConvertTo() {return convertTo;}
	
	public CalculatorFrame(JTable table, Application controller, ArrayList<Currency> currs, ArrayList<String> cl, MainView mv) {
		super();
		this.mainView = mv;
		this.table = table;
		this.controller = controller;
		this.currencies = currs;
		this.codeList = cl;
		initializeCalculator();
	}

	/**
	 * Initialize the contents of the calculator frame.
	 */
	private void initializeCalculator() {
		LogWriter.getInstance().logger.info("Initializing the calculator frame.");
		this.setResizable(false);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("data/calculator-icon.png"));
		this.setTitle("Calculator");
		this.setBounds(100, 100, 686, 212);
		this.getContentPane().setLayout(null);
		this.setLocation((int)(mainView.getLocationOnScreen().getX())+650,(int)mainView.getLocationOnScreen().getY());

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				resetCalculatorFrame(table);
				setVisible(false);
				LogWriter.getInstance().logger.info("Closing the add-coin frame.");
			}
		});

		JLabel currLabel = new JLabel("Current Coin:");
		currLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		currLabel.setBounds(25, 19, 107, 16);
		this.getContentPane().add(currLabel);
		String[] list = new String[codeList.size()];
		list = codeList.toArray(list);
		convertFrom = new JComboBox<String>();
		convertFrom.setModel(new DefaultComboBoxModel<String>(list));
		convertFrom.setBounds(115, 17, 82, 22);
		convertFrom.setSelectedIndex(0);
		this.getContentPane().add(convertFrom);

		JLabel amountLabel = new JLabel("Amount:");
		amountLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		amountLabel.setBounds(248, 19, 93, 16);
		this.getContentPane().add(amountLabel);

		amount = new JTextField();
		amount.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (amount.getText().length() > 14) e.consume();
			}
		});
		amount.setText("0");
		amount.setColumns(10);
		amount.setBounds(311, 17, 116, 22);
		this.getContentPane().add(amount);

		JLabel conLabel = new JLabel("Convert to:");
		conLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		conLabel.setBounds(452, 19, 82, 16);
		this.getContentPane().add(conLabel);

		convertTo = new JComboBox<String>();
		convertTo.setModel(new DefaultComboBoxModel<String>(list));
		convertTo.setBounds(531, 17, 82, 22);
		convertTo.setSelectedIndex(0);
		this.getContentPane().add(convertTo);

		JButton conButton = new JButton("Convert!");
		conButton.setIcon(new ImageIcon("data/coin-icon.png"));
		conButton.setForeground(Color.DARK_GRAY);

		//an event that occurs by clicking the convert button.
		conButton.addActionListener((ActionEvent arg0)-> {									  
			//initializing the needed variables.
			double amounted = 0;
			double sum = 0;
			Currency currCurrency = currencies.get(0);
			Currency newCurrency = currencies.get(0);
			String  currCoin = (String) convertFrom.getSelectedItem();
			String newCoin = (String) convertTo.getSelectedItem();
			//searching the currencies according to their code.
			for (int i=0; i<currencies.size(); i++) {
				if (currencies.get(i).getCode().equals(currCoin)) currCurrency = currencies.get(i);			
				if (currencies.get(i).getCode().equals(newCoin)) newCurrency = currencies.get(i);
			}
			try {
				//validates that amount money is a positive number.
				amounted = Double.parseDouble(amount.getText());
				if (amounted < 0) throw new NumberFormatException();

				//calling the convert method of the controller, and store it in the result field.
				sum = controller.convert(amounted, currCurrency,newCurrency);
				DecimalFormat myFormatter = new DecimalFormat("###,###.###");
				result.setText(myFormatter.format(sum)+" " +newCurrency.getCode());
				LogWriter.getInstance().logger.trace("A successfull converting from "+currCurrency.getCode()+" to "+newCurrency.getCode());
			}
			catch(NumberFormatException e) {
				LogWriter.getInstance().logger.trace("An input error in converting - amount of money: "+amount.getText());
				JOptionPane.showMessageDialog(null, "Only positive numbers are allowed!", "Input Error!", 0);
			}
		}
				);
		conButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		conButton.setBackground(SystemColor.activeCaption);
		conButton.setBounds(160, 83, 161, 43);
		this.getContentPane().add(conButton);

		JLabel resultLabel = new JLabel("Result:");
		resultLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		resultLabel.setBounds(250, 137, 82, 16);
		this.getContentPane().add(resultLabel);

		result = new JTextField();
		result.setEditable(false);
		result.setColumns(10);
		result.setBounds(299, 135, 130, 22);
		this.getContentPane().add(result);

		JLabel creditLabel = new JLabel("Made By Yoav Saroya\u00AE");
		creditLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		creditLabel.setBounds(515, 140, 170, 52);
		this.getContentPane().add(creditLabel);

		JLabel bucketImg = new JLabel("");
		bucketImg.setIcon(new ImageIcon("data/Coins.png"));
		bucketImg.setBounds(569, 74, 101, 95);
		this.getContentPane().add(bucketImg);

		JButton resetButton = new JButton("Reset");
		//an event that occurs by clicking the reset button.
		resetButton.addActionListener((ActionEvent arg0)-> {
			//just call the reset method and records it in the Log file.
			resetCalculatorFrame(table);
			LogWriter.getInstance().logger.trace("Restarting the calculator frame.");
		});
		resetButton.setIcon(new ImageIcon("data/reset.png"));
		resetButton.setForeground(Color.DARK_GRAY);
		resetButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		resetButton.setBackground(SystemColor.activeCaption);
		resetButton.setBounds(385, 83, 161, 43);
		this.getContentPane().add(resetButton);

		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSearch.setBounds(55, 50, 60, 16);
		this.getContentPane().add(lblSearch);

		searchCurrCoin = new JTextField();
		searchCurrCoin.setBounds(115, 48, 60, 22);
		this.getContentPane().add(searchCurrCoin);
		searchCurrCoin.setColumns(10);
		searchCurrCoin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (searchCurrCoin.getText().length() > 2) e.consume();
			}
		});

		searchCurrCoin.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchCurrCoin.getText().equals("NotFound")) searchCurrCoin.setText("");
			}
		});


		JLabel lblbyCode = new JLabel("(by code)");
		lblbyCode.setBounds(55, 65, 56, 16);
		this.getContentPane().add(lblbyCode);

		JButton searchButtonCurrCoin = new JButton("");

		//an events that occurs according to the search button.
		searchButtonCurrCoin.addActionListener((ActionEvent arg0)-> {
			searchCoin(searchCurrCoin, convertFrom);
		});
		searchButtonCurrCoin.setIcon(new ImageIcon("data/search.png"));
		searchButtonCurrCoin.setBounds(174, 48, 20, 21);
		this.getContentPane().add(searchButtonCurrCoin);

		JButton searchButtonNewCoin = new JButton("");
		//an events that occurs according to the search button.
		searchButtonNewCoin.addActionListener((ActionEvent arg0)-> {
				searchCoin(searchNewCoin, convertTo);
			});

		searchButtonNewCoin.setIcon(new ImageIcon("data/search.png"));
		searchButtonNewCoin.setBounds(590, 47, 20, 21);
		this.getContentPane().add(searchButtonNewCoin);

		searchNewCoin = new JTextField();
		searchNewCoin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (searchNewCoin.getText().length() > 2) e.consume();
			}
		});

		searchNewCoin.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchNewCoin.getText().equals("NotFound")) searchNewCoin.setText("");
			}
		});

		searchNewCoin.setColumns(10);
		searchNewCoin.setBounds(531, 47, 60, 22);
		this.getContentPane().add(searchNewCoin);

		JLabel labelSearch2 = new JLabel("Search:");
		labelSearch2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		labelSearch2.setBounds(471, 51, 64, 12);
		this.getContentPane().add(labelSearch2);

		JLabel lblbyCode2 = new JLabel("(by code)");
		lblbyCode2.setBounds(471, 66, 60, 13);
		this.getContentPane().add(lblbyCode2);
	}

	//search a specific coin from a ComboBox list.
	private void searchCoin(JTextField code, JComboBox<String> codeList) {
		for (int i = 0; i <currencies.size(); i++) {
			if (code.getText().equals(currencies.get(i).getCode())) {
				codeList.setSelectedIndex(i);
				if (i != 0) table.addRowSelectionInterval(i-1, i-1);
				return;
			}
		}
		code.setText("NotFound");
	}
	
	
	//reset the calculator frame back to default properties.
	public void resetCalculatorFrame(JTable table) {
		convertTo.setSelectedIndex(0);
		convertFrom.setSelectedIndex(0);
		amount.setText("0");
		result.setText("");
		searchCurrCoin.setText("");
		searchNewCoin.setText("");
		table.clearSelection();
	}
}
