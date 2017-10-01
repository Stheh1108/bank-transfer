import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

public class TestClass {
	public static void main(String[] args) throws Exception{
		mainMenu();
	}
	public static void mainMenu() throws Exception{
		Scanner sc = new Scanner(System.in);
		System.out.println("Please choose one of the following options: ");
		System.out.println("1 = Open an account in PNB");
		System.out.println("2 = Open an account in SBI");
		System.out.println("3 = Deposit money in PNB");
		System.out.println("4 = Deposit money in SBI");
		System.out.println("5 = Withdraw money from PNB");
		System.out.println("6 = Withdraw money from SBI");
		System.out.println("7 = Transfer money from PNB to SBI");
		System.out.println("8 = Transfer money from SBI to PNB");
		System.out.println("9 = Transfer money from PNB to PNB");
		System.out.println("10 = Transfer money from SBI to SBI");
		System.out.println("11 = Show all PNB records");
		System.out.println("12 = Show all SBI records");
		System.out.print("Option: ");
		int in = sc.nextInt();
		if(in == 1){
			addBank("pnb");
		}
		else if(in == 2){
			addBank("sbi");
		}
		else if(in == 3){
			addDeposit("pnb");
		}
		else if(in == 4){
			addDeposit("sbi");
		}
		else if(in == 5){
			withdrawal("pnb");
		}
		else if(in ==6){
			withdrawal("sbi");
		}
		else if(in == 7){
			transferMoney("pnb", "sbi");
		}
		else if(in == 8){
			transferMoney("sbi", "pnb");
		}
		else if(in == 9){
			transferMoney("pnb", "pnb");
		}
		else if(in == 10){
			transferMoney("sbi", "sbi");
		}
		else if(in == 11){
			showRecords("pnb");
		}
		else if(in == 12){
			showRecords("sbi");
		}
	}
	public static void withdrawal(String bank) throws Exception{
		Scanner sc = new Scanner(System.in);
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "root", "root");
		System.out.print("Please enter the id of the customer: ");
		int id = sc.nextInt();
		PreparedStatement ps = c.prepareStatement("select * from "+bank+ " where CustID = "+id);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			String fname = rs.getString("FirstName");
			String lname = rs.getString("LastName");
			int balance = rs.getInt("Balance");
			System.out.println("Hi "+fname+" "+lname+"! Your available balance is: "+balance+" INR.");
			System.out.print("Please enter the amount to be withdrawed: ");
			int withdrawal = sc.nextInt();
			if(balance>=withdrawal){
				withdrawal=balance-withdrawal;
				System.out.println("Withdrawal successful.");
				System.out.println("Old balace: "+balance+"INR");
				System.out.println("New balance: "+withdrawal+"INR");
			}
			else{
				System.out.println("Invalid withdrawal. Account balance is low.");
				withdrawal = balance;
			}
			Statement st = c.createStatement();
			st.executeUpdate("update "+bank+" set Balance = "+withdrawal+" where CustID = "+id);
		}
		c.close();
	}
	public static void showRecords(String bank) throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "root", "root");
		PreparedStatement ps = c.prepareStatement("select * from "+bank);
		ResultSet rs = ps.executeQuery();
		System.out.print(String.format("%s\t%s %s\t\t%-30s\t%s\n","CustID","Name","","Email ID","Account Balance"));
		while(rs.next()){
			int id = rs.getInt("CustID");
			String fname = rs.getString("FirstName");
			String lname = rs.getString("LastName");
			String email = rs.getString("Email");
			int balance = rs.getInt("Balance");
			System.out.print(String.format("%d\t%s. %s\t%-30s\t%d INR\n",id,fname.charAt(0),lname,email,balance));
		}
		c.close();
	}
	public static void transferMoney(String bank1, String bank2) throws Exception{
		Scanner sc = new Scanner(System.in);
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "root", "root");
		System.out.print("Please enter the id of the customer from whose account money needs to be transferred: ");
		int id1 = sc.nextInt();
		System.out.print("Please enter the id of the customer whose account money needs to be transferred to: ");
		int id2 = sc.nextInt();
		PreparedStatement ps1 = c.prepareStatement("select * from "+bank1+ " where CustID = "+id1);
		ResultSet rs1 = ps1.executeQuery();
		int transfer = 0;
		while(rs1.next()){
			String fname = rs1.getString("FirstName");
			String lname = rs1.getString("LastName");
		int balance = rs1.getInt("Balance");
			System.out.println("Hi "+fname+" "+lname+"! Your available balance in "+bank1+" is: "+balance+" INR.");
			System.out.print("Please enter the amount to be transferred: ");
			transfer = sc.nextInt();
			if(balance>=transfer){
				balance=balance-transfer;
				System.out.println("New balance for "+fname+" "+lname+" = "+balance+" INR");
			}
			else{
				System.out.println("Invalid withdrawal. Account balance is low.");
			}
			Statement st = c.createStatement();
			st.executeUpdate("update "+bank1+" set Balance = "+balance+" where CustID = "+id1);
		}
		PreparedStatement ps2 = c.prepareStatement("select * from "+bank2+" where CustID = "+id2);
		ResultSet rs2 = ps2.executeQuery();
		while(rs2.next()){
			int dataid = rs2.getInt("CustID");
			String fname = rs2.getString("FirstName");
			String lname = rs2.getString("LastName");
			int balance = rs2.getInt("Balance");
			balance +=transfer;
			System.out.println("Deposit has been made to "+fname+" "+lname+".");
			System.out.println("New balance for "+fname+" "+lname+" = "+balance+" INR");
			Statement st = c.createStatement();
			st.executeUpdate("update "+bank2+" set Balance = "+balance+" where CustID = "+id2);
		}
		c.close();
	}
	public static void addDeposit(String bank) throws Exception{
		Scanner sc = new Scanner(System.in);
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "root", "root");
		System.out.print("Please enter the id of the customer: ");
		int id = sc.nextInt();
		
		PreparedStatement ps = c.prepareStatement("select * from "+bank+" where CustId = "+id);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			String fname = rs.getString("FirstName");
			String lname = rs.getString("LastName");
			int balance = rs.getInt("Balance");
			System.out.println("Hi "+fname+" "+lname+"! Your available balance is: "+balance+" INR.");
			System.out.print("Please enter the amount to be deposited: ");
			int deposit = sc.nextInt();
			deposit+=balance;
			System.out.println("Deposit has been made.");
			System.out.println("Old balace: "+balance+" INR");
			System.out.println("New balance: "+deposit+" INR");
			Statement st = c.createStatement();
			st.executeUpdate("update "+bank+" set Balance = "+deposit+" where CustID = "+id);
		}
		c.close();
	}
	public static void addBank(String bank) throws Exception{
		Random r =new Random();
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "root", "root");
		Scanner sc = new Scanner(System.in);
		Scanner sc2 = new Scanner(System.in);
		PreparedStatement ps = c.prepareStatement("insert into "+bank+" values(?,?,?,?,'0')");
		int id = r.nextInt(9999999);
		ps.setInt(1, id);
		System.out.print("Please enter the first name of the customer: ");
		String fname = sc.next();
		ps.setString(2, fname);
		System.out.print("Please enter the last name of the customer: ");
		String lname = sc.next();
		ps.setString(3, lname);
		System.out.print("Please enter the email ID of the customer: ");
		String email = sc2.nextLine();
		ps.setString(4, email);		
		int ret = ps.executeUpdate();
		if(ret ==1){
			System.out.println("Bank account added. Please note down your customer ID to make any transactions in the future.");
			System.out.println("Account details: ");
			System.out.println("Customer ID: "+id);
			System.out.println("Name: "+fname+" "+lname);
			System.out.println("Email ID: "+email);
		}
		c.close();
	}
}
