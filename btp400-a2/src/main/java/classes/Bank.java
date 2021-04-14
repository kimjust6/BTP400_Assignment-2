package classes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.request.node.RequestAccountHistory;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountHistory.BlockInfo;
import uk.oczadly.karl.jnano.rpc.util.wallet.LocalRpcWalletAccount;
import uk.oczadly.karl.jnano.rpc.util.wallet.WalletActionException;
import uk.oczadly.karl.jnano.util.WalletUtil;
import uk.oczadly.karl.jnano.util.blockproducer.BlockProducer;
import uk.oczadly.karl.jnano.util.blockproducer.BlockProducerSpecification;
import uk.oczadly.karl.jnano.util.blockproducer.StateBlockProducer;
import uk.oczadly.karl.jnano.util.workgen.OpenCLWorkGenerator;
import uk.oczadly.karl.jnano.util.workgen.OpenCLWorkGenerator.OpenCLInitializerException;

public class Bank {

	static HexData seed = new HexData("991A38BED0D022D6622E9AD47513E2A14AC0DA58F15D8AFC81075DEC11CAF29D");
//	static HexData seed = new HexData("8178C293072C204532F8A7A798A53DF9636ADAB45F65A6850CA3FBA6775AC721");
	static final double BAN_NAN_MULT = 10;
	final String node = "https://kaliumapi.appditto.com/api";
	// final String node = "http://192.168.1.167:7072";
	RpcQueryNode rpc;
	final String rep = "ban_1fomoz167m7o38gw4rzt7hz67oq6itejpt4yocrfywujbpatd711cjew8gjj";
	final String prefix = "ban";
	BlockProducer blockProducer;

	double theBalance = -1;

	public Bank() {
		try {

			rpc = new RpcQueryNode(new URL(node));

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			blockProducer = new StateBlockProducer(BlockProducerSpecification.builder().defaultRepresentative(rep)
					.workGenerator(new OpenCLWorkGenerator()) // Local work on gpu
//					.workGenerator(new NodeWorkGenerator(rpc))
					.addressPrefix(prefix).build());
		} catch (OpenCLInitializerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// amount you want to send and the account number you want to send to
	public boolean send(int fromAccountNo, int toAccountNo, double amount) {

		HexData skFrom = WalletUtil.deriveKeyFromSeed(seed, fromAccountNo);
		HexData skTo = WalletUtil.deriveKeyFromSeed(seed, toAccountNo);

		// Create account from private key
		LocalRpcWalletAccount account = new LocalRpcWalletAccount(skFrom, // Secret key
				rpc, // Kalium RPC
				blockProducer); // Using our BlockProducer defined above

		// Send funds
		HexData hash = null;

		// Send funds to another account
		System.out.printf("Send block hash: %s%n", hash);
		try {
			
			hash = account.send(NanoAccount.fromPrivateKey(skTo, prefix),
					NanoAmount.valueOfNano(String.valueOf(amount / BAN_NAN_MULT))).getHash();
		} catch (WalletActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.printf("Send block hash: %s%n", hash);
		return false;
	}

	public List<BlockInfo> getAccountHistory(int accountNo) throws IOException, RpcException
	{
//			HexData skFrom = WalletUtil.deriveKeyFromSeed(seed, accountNo);
//			System.out.println( WalletUtil.deriveKeyFromSeed(skFrom).toString());
		
//			RequestAccountHistory history = new RequestAccountHistory("ban_1kkjqhqsjy1t54ufxzsyr1n9wetrut8shm9mgihauz1dh7owic1xcsyyqrq5");
		RequestAccountHistory history = new RequestAccountHistory(getPubAddress(accountNo));
			return this.rpc.processRequest(history).getHistory();
		
	}
//	public Account getAccount(int accountNo) throws WalletActionException {
//		HexData privateKey = WalletUtil.deriveKeyFromSeed(seed, accountNo);
//		LocalRpcWalletAccount account = new LocalRpcWalletAccount(privateKey, // Private key
//				rpc, // Kalium RPC
//				blockProducer); // Using our BlockProducer defined above
//
//		// account.receiveAll();
//
//		return new Account(accountNo, account);
//	}

	public void updateBalance(int accountNo) {
		HexData privateKey = WalletUtil.deriveKeyFromSeed(seed, accountNo);

		LocalRpcWalletAccount account = new LocalRpcWalletAccount(privateKey, // Private key
				rpc, // Kalium RPC
				blockProducer); // Using our BlockProducer defined above

		try {
			account.receiveAll();
		} catch (WalletActionException e) {
			e.printStackTrace();
		}
	}

	public double getBalance(int accountNo) {
		try {
			HexData skFrom = WalletUtil.deriveKeyFromSeed(seed, accountNo);
			LocalRpcWalletAccount wallet = new LocalRpcWalletAccount(skFrom, rpc, blockProducer);

			wallet.receiveAll();

			return wallet.getBalance().getAsNano().doubleValue() * Bank.BAN_NAN_MULT;

		} catch (WalletActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;
	}

	public String getPubAddress(int accountNo) {
		return NanoAccount.fromPrivateKey(WalletUtil.deriveKeyFromSeed(seed, accountNo), prefix).toString();
	}

	public static void main(String args[])
			throws WalletActionException, OpenCLInitializerException, IOException, RpcException {
		System.out.println("Nice");
		Bank b = new Bank();

		List<BlockInfo> aList = b.getAccountHistory(0); 
		for (int i = 0; i < aList.size(); ++i)
		{
			BlockInfo binfo = aList.get(i);

			System.out.println(i + ": " + " " + binfo.getType() + ": "
					+ binfo.getAmount().getAsNano().doubleValue() * Bank.BAN_NAN_MULT + " " + binfo.getTimestamp());

		}
	}

}
