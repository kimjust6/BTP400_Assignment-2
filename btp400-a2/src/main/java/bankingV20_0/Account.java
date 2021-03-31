package bankingV20_0;

public class Account {
	int accountNo;
	double balance;
	String pubAddress;
	Account(){
		accountNo = -1;
		balance = 0;
		pubAddress = "";
	}
	
	public Account(int accountNo, String pubAddress, double balance) {
		this.accountNo = accountNo;
		this.balance = balance;
		this.pubAddress = pubAddress;
	}
	
	public int getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(int accountNo) {
		this.accountNo = accountNo;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getPubAddress() {
		return pubAddress;
	}
	public void setPubAddress(String pubAddress) {
		this.pubAddress = pubAddress;
	}

	
}
