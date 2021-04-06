package classes;

import classes.Bank;
import uk.oczadly.karl.jnano.rpc.util.wallet.LocalRpcWalletAccount;
import uk.oczadly.karl.jnano.rpc.util.wallet.WalletActionException;

public class Account {
	private int accountNo;
	private LocalRpcWalletAccount wallet;

	Account() {
		accountNo = -1;
		wallet = null;
	}

	public Account(int accountNo, LocalRpcWalletAccount wallet) {
		this.accountNo = accountNo;
		this.wallet = wallet;
	}

	public int getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(int accountNo) {
		this.accountNo = accountNo;
	}

	// return new Account(accountNo, account.getAccount().toAddress(),
	// account.getBalance().getAsNano().doubleValue()*BAN_NAN_MULT);
	public double getBalance() {
		double balance = 0;
		try {
			balance = this.wallet.getBalance().getAsNano().doubleValue() * Bank.BAN_NAN_MULT;
		} catch (WalletActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return balance;
	}

	public String getPubAddress() {
		try {
			this.wallet.receiveAll();
		} catch (WalletActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.wallet.getAccount().toAddress();
	}

}
