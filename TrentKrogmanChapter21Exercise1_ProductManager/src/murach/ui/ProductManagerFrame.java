package murach.ui;

/*
 * Name: Trent Krogman
 * Date: 11/17/20
 * This is the Product Manager Frame class for Chapter 21 Exercise 1
 */

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import murach.business.Product;
import murach.db.ProductDB;
import murach.db.DBException;

@SuppressWarnings("serial")
public class ProductManagerFrame extends JFrame {
    private JTable productTable;
    private ProductTableModel productTableModel;
    
    public ProductManagerFrame() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println(e);
        }        
        setTitle("Product Manager");
        setSize(768, 384);
        setLocationByPlatform(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        add(buildButtonPanel(), BorderLayout.SOUTH);
        productTable = buildProductTable();
        add(new JScrollPane(productTable), BorderLayout.CENTER);
        setVisible(true);        
    }
    
    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        
        JButton addButton = new JButton("Add");
        addButton.setToolTipText("Add product");
        addButton.addActionListener((ActionEvent) -> {
            doAddButton();
        });
        panel.add(addButton);
        
        JButton editButton = new JButton("Edit");
        editButton.setToolTipText("Edit selected product");
        editButton.addActionListener((ActionEvent) -> {
            doEditButton();
        });
        panel.add(editButton);
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.setToolTipText("Delete selected product");
        deleteButton.addActionListener((ActionEvent) -> {
            doDeleteButton();
        });
        panel.add(deleteButton);
        
        JButton printButton = new JButton("Print");
        printButton.setToolTipText("Print selected product");
        printButton.addActionListener((ActionEvent) -> {
        	doPrintButton();
        });
        panel.add(printButton);
        
        return panel;
    }
    
    private void doAddButton() {	
    	String code = "";
    	double price = 0.0;
    	
    	List<String> productCodes = new ArrayList<>();	
		try{
    		 productCodes = ProductDB.getProductCodes();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		
    	boolean invalidCode;
		do{
			invalidCode = false;
			code = JOptionPane.showInputDialog(this, "Please enter a code", "Add Product", 3);
			for(String c : productCodes){
				if(code.equalsIgnoreCase(c)){
					invalidCode = true;
					JOptionPane.showMessageDialog(this, "Code already in use", "Add Product", 2);
				}
			}
			if(code.trim().isEmpty()){
				invalidCode = true;
				JOptionPane.showMessageDialog(this, "Code must have a value.", "Add Product", 2);
			}		
		}while(invalidCode);
		
		String description = JOptionPane.showInputDialog(this, "Please enter a description", "Add Product", 3);
		
    	boolean invalidPrice = true;
		while(invalidPrice){
			try{
				price = Double.parseDouble(JOptionPane.showInputDialog(this, "Please enter a price", "Add Product", 3));
				if(price < 0){
    				JOptionPane.showMessageDialog(this,
	                        "Price must be equal to or greater than 0.", 
	                        "Invalid Price", JOptionPane.ERROR_MESSAGE);
    			}
    			else if(price > 1000){
    				JOptionPane.showMessageDialog(this,
	                        "Price must be less than or equal to 1000.", 
	                        "Invalid Price", JOptionPane.ERROR_MESSAGE);
    			}
    			else{
    				invalidPrice = false;
    			}
			}catch(Exception e){
				JOptionPane.showMessageDialog(this, "Price must be a number", "Add Product", 2);
			}
		}
		
		Product addProduct = new Product(code, description, price);
		
		try{
			murach.db.ProductDB.add(addProduct);
			productTableModel.databaseUpdated();
		}catch(Exception e){
			e.printStackTrace();
		}
    }
    
    private void doEditButton() {
    	String newCode = "";
    	String newDesc = "";
    	double newPrice = 0.0;
    	
    	List<String> productCodes = new ArrayList<>();	
		try{
    		 productCodes = ProductDB.getProductCodes();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	int selectedRow = productTable.getSelectedRow();
    	
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "No product is currently selected.", 
                    "No product selected", JOptionPane.ERROR_MESSAGE);
        } else {
        	Product product = productTableModel.getProduct(selectedRow);
        	productCodes.remove(product.getCode());
        	
        	boolean invalidCode = true;
        	while(invalidCode){
	        	newCode = JOptionPane.showInputDialog(this, "Update code", product.getCode());
	        	
	        	if(newCode.trim().isEmpty()){
	        		JOptionPane.showMessageDialog(this,
	                        "Please enter a code.", 
	                        "No code entered", JOptionPane.ERROR_MESSAGE);
	        	}else{
	        		invalidCode = false;
	        	}
	        	
	        	for(String c : productCodes){
	        		if(c.equalsIgnoreCase(newCode)){
	        			invalidCode = true;
	        			JOptionPane.showMessageDialog(this, "Code already in use.", "Invalid Code", JOptionPane.ERROR_MESSAGE);
	        		}
	        	}
        	}  	

        	newDesc = JOptionPane.showInputDialog(this, "Update description", product.getDescription());
                	
        	boolean invalidPrice = true;
        	while(invalidPrice){
        		try{
        			newPrice = Double.parseDouble(JOptionPane.showInputDialog(this, "Update price", product.getPrice()));
        			if(newPrice < 0){
        				JOptionPane.showMessageDialog(this,
    	                        "Price must be equal to or greater than 0.", 
    	                        "Invalid Price", JOptionPane.ERROR_MESSAGE);
        			}
        			else if(newPrice > 1000){
        				JOptionPane.showMessageDialog(this,
    	                        "Price must be less than or equal to 1000.", 
    	                        "Invalid Price", JOptionPane.ERROR_MESSAGE);
        			}
        			else{
        				invalidPrice = false;
        			}	
        		}catch(NumberFormatException e){
        			JOptionPane.showMessageDialog(this,
	                        "Price must be a number.", 
	                        "Invalid Price", JOptionPane.ERROR_MESSAGE);
        		}
        	}       	
        	product.setCode(newCode);
        	product.setDescription(newDesc);
        	product.setPrice(newPrice);
        	
        	try{
        		ProductDB.update(product);
        		productTableModel.databaseUpdated();
        	}catch(DBException e){
        		e.printStackTrace();
        	}
        }
    }
    
    private void doDeleteButton() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "No product is currently selected.", 
                    "No product selected", JOptionPane.ERROR_MESSAGE);
        } else {
            Product product = productTableModel.getProduct(selectedRow);
            int option = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete " + 
                            product.getDescription() + " from the database?",
                    "Confirm delete", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {                    
                    ProductDB.delete(product);
                    productTableModel.databaseUpdated();
                } catch (DBException e) {
                    System.out.println(e);
                }
            }
        }
    }
    
    private void doPrintButton(){
    	int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "No product is currently selected.", 
                    "No product selected", JOptionPane.ERROR_MESSAGE);
        }else{
        	JOptionPane.showMessageDialog(this, "Product has been sent to the printer.",
    									"Print Product", 1);
        }
    }
    
    private JTable buildProductTable() {
        productTableModel = new ProductTableModel();
        JTable table = new JTable(productTableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setBorder(null);
        return table;
    }
}