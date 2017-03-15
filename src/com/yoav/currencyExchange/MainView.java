package com.yoav.currencyExchange;
import javax.swing.JFrame;




import javax.swing.JScrollPane;

import java.awt.Toolkit;

import javax.swing.JTable;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.SystemColor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * The View class is the view part of the MVC architecture. it represents the user-interface and the
 * interaction between the application and the user.
 * The view has a reference to controller (association) to do the necessary procedures, and also
 * holds a reference to CurrencyDataHolder, so the view will updates his frames when the data is new.
 * The view part has 3 frames: the main frame - the frame that opened when the application is started.
 * the calculator frame - the frame that opened when the user wants to converts between 2 currencies.
 * the add-coin frame - the frame that opened when the user wants to add a new coin to the application.
 * @author Yoav Saroya
 */

public class MainView extends JFrame {

	// Main frame Variables
	private static final long serialVersionUID = 1L;
	private JTable table;
	private ArrayList<Currency> currencies;
	private String[][] data;
	private Application controller;
	private CurrencyDataHolder model;
	private ArrayList<String> codeList;
	private JLabel labelDate;
	
	private CalculatorFrame calcFrame;
	private AddCoinFrame addCoinFrame;

	/**
	 * Create the application.
	 */
	public MainView(Application controller, CurrencyDataHolder model) {
		this.controller = controller;
		this.model = model;
		controller.update();
		initialize();
		this.setVisible(true);
		calcFrame = new CalculatorFrame(table,controller,currencies,codeList,this);
		addCoinFrame = new AddCoinFrame(calcFrame, codeList,currencies,this);
	}


	/**
	 * Initialize the contents of the main frame.
	 */
	private void initialize() {
		LogWriter.getInstance().logger.info("Initializing the main frame.");
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				LogWriter.getInstance().logger.info("Closing the application.\n\n");
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("data/coin-icon.png"));
		this.setTitle("Currency Converter");
		this.setBounds(100, 100, 638, 476);
		this.getContentPane().setLayout(null);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2-300, dim.height/2-this.getSize().height/2);

		labelDate = new JLabel("");
		labelDate.setFont(new Font("Tahoma", Font.BOLD, 16));
		labelDate.setBounds(430, 62, 190, 16);
		this.getContentPane().add(labelDate);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 83, 608, 247);
		this.getContentPane().add(scrollPane);
		String[] columns =  {"Name", "Unit", "Country", "Code", "Rate", "Change"};
		updateDataTable(false);
		table = new JTable((new DefaultTableModel(data, columns))) {
			private static final long serialVersionUID = -94313380417681847L;
			@Override
			public boolean isCellEditable(int data, int columns) {return false;}
		};
		table.setFont(new Font("Tahoma", Font.PLAIN, 16));
		table.getTableHeader().setReorderingAllowed(false);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for(int x=0;x<table.getColumnCount();x++)
			table.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
		scrollPane.setViewportView(table);

		JButton refreshButton = new JButton("Refresh");
		refreshButton.setIcon(new ImageIcon("data/refresh.png"));
		
		/* activates the update method of the controller - which updates the view and the model.
		 * happens in a seperate thread to avoid stocking the UI.
		 */
		refreshButton.addActionListener((ActionEvent e)-> {
//			new Thread(()->controller.update(), "Refresh-Thread").start();
//			controller.update();
			controller.setUpdater(true);
		});
		refreshButton.setForeground(Color.DARK_GRAY);
		refreshButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		refreshButton.setBackground(SystemColor.activeCaption);
		refreshButton.setBounds(54, 343, 154, 43);
		this.getContentPane().add(refreshButton);

		JLabel lblExchangeRates = new JLabel("Exchange rates:");
		lblExchangeRates.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblExchangeRates.setBounds(127, 58, 181, 22);
		this.getContentPane().add(lblExchangeRates);

		JLabel label = new JLabel("Made By Yoav Saroya\u00AE");
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setBounds(468, 403, 170, 52);
		this.getContentPane().add(label);

		JButton calcButton = new JButton("Calculator");
		//an event that occurs by the calculator button. it opens the calculator frame.
		calcButton.addActionListener((ActionEvent e)-> {
				if (calcFrame==null) calcFrame = new CalculatorFrame(table,controller,currencies,codeList,this);
				if(!calcFrame.isVisible()) {
					calcFrame.setLocation((int)(this.getLocationOnScreen().getX())+650,(int)this.getLocationOnScreen().getY());
					calcFrame.setVisible(true);
					LogWriter.getInstance().logger.trace("Displaying the calculator frame.");
				}
			});
		calcButton.setIcon(new ImageIcon("data/calculator-icon.png"));
		calcButton.setForeground(Color.DARK_GRAY);
		calcButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		calcButton.setBackground(SystemColor.activeCaption);
		calcButton.setBounds(419, 343, 161, 43);
		this.getContentPane().add(calcButton);

		JLabel labelUpdate = new JLabel("Last Update:");
		labelUpdate.setFont(new Font("Tahoma", Font.BOLD, 16));
		labelUpdate.setBounds(320, 62, 110, 16);
		this.getContentPane().add(labelUpdate);

		JButton btnCreateANew = new JButton("Create Coin");
		//an event that occurs by the create coin button. it opens the add-coin frame.
		btnCreateANew.addActionListener((ActionEvent arg0)-> {
				if (addCoinFrame==null) addCoinFrame = new AddCoinFrame(calcFrame, codeList,currencies,this);
				if (!addCoinFrame.isVisible()) {
					addCoinFrame.setLocation((int)(this.getLocationOnScreen().getX())+650,(int)this.getLocationOnScreen().getY()+224);
					addCoinFrame.setVisible(true);
					LogWriter.getInstance().logger.trace("Displaying the add-coin frame.");
				}
			});
		btnCreateANew.setIcon(new ImageIcon("data/add-icon.png"));
		btnCreateANew.setForeground(Color.DARK_GRAY);
		btnCreateANew.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCreateANew.setBackground(SystemColor.activeCaption);
		btnCreateANew.setBounds(237, 343, 154, 43);
		this.getContentPane().add(btnCreateANew);
		LogWriter.getInstance().logger.trace("Displaying the main window frame.");
	}

	//updating the table of the view.
	public void updateDataTable(boolean update) {
		//getting the currencies according to the local XML file.
		ArrayList<Currency> tempCurrList = null;
		try {
			tempCurrList = new ArrayList<>(model.parseToCurrencies().values());
		} catch (SAXException | IOException | ParserConfigurationException e) {
			LogWriter.getInstance().logger.error("failed to update the View frames.");
			e.printStackTrace();
		}
		if (!update) currencies = tempCurrList;

		//if a currency has changed, so the vector of currencies updates.
		for (int i = 0; i < tempCurrList.size(); i++) {
			if (!tempCurrList.get(i).equals(currencies.get(i))) {
				currencies.remove(i);
				currencies.add(tempCurrList.get(i));
			}
		}

		//the codeList gets the code of each currency object.
		codeList = new ArrayList<>();
		for (int i=0; i< currencies.size(); i++)
			codeList.add(currencies.get(i).getCode());

		data = controller.parseToTable(currencies);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		labelDate.setText(dateFormat.format(new Date()));

		if (update) {
			//updating the JTable object.
			for (int i=0; i < table.getRowCount(); i++) {
				for (int j=0; j < table.getColumnCount(); j++) {
					table.setValueAt(data[i][j], i, j);
				}
			}
			DefaultTableModel dtm = ((DefaultTableModel) table.getModel());
			for (int i = table.getRowCount(); i < data.length; i++)
				dtm.addRow(new Object[] {data[i][0],data[i][1],data[i][2], data[i][3],
						data[i][4],data[i][5]});
		}
	}
}