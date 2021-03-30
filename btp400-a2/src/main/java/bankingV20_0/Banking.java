package bankingV20_0;

import java.net.MalformedURLException;
import java.net.URL;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
import uk.oczadly.karl.jnano.rpc.util.RpcServiceProviders;
import uk.oczadly.karl.jnano.rpc.util.wallet.LocalRpcWalletAccount;
import uk.oczadly.karl.jnano.rpc.util.wallet.WalletActionException;
import uk.oczadly.karl.jnano.util.WalletUtil;
import uk.oczadly.karl.jnano.util.blockproducer.BlockProducer;
import uk.oczadly.karl.jnano.util.blockproducer.BlockProducerSpecification;
import uk.oczadly.karl.jnano.util.blockproducer.StateBlockProducer;
import uk.oczadly.karl.jnano.util.workgen.OpenCLWorkGenerator;
import uk.oczadly.karl.jnano.util.workgen.OpenCLWorkGenerator.OpenCLInitializerException;

public class Banking {

	static HexData seed = new HexData("991A38BED0D022D6622E9AD47513E2A14AC0DA58F15D8AFC81075DEC11CAF29D");
	static final double BAN_NAN_MULT = 10;
	final String node = "https://kaliumapi.appditto.com/api";
	//final String node = "http://192.168.1.167:7072";
	RpcQueryNode rpc;
	final String rep = "ban_1fomoz167m7o38gw4rzt7hz67oq6itejpt4yocrfywujbpatd711cjew8gjj";
	final String prefix = "ban";
	BlockProducer blockProducer;

	Banking() {
		try {
			blockProducer = new StateBlockProducer(
					BlockProducerSpecification.builder().defaultRepresentative(rep)
					.workGenerator(new OpenCLWorkGenerator()) //Local work on gpu
					.addressPrefix(prefix)
					.build());
		} catch (OpenCLInitializerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			rpc = new RpcQueryNode(new URL(node));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// amount you want to send and the account number you want to send to
	boolean send(int fromAccountNo, int toAccountNo, double amount) {

		HexData pkFrom = WalletUtil.deriveKeyFromSeed(seed, fromAccountNo);
		HexData pkTo = WalletUtil.deriveKeyFromSeed(seed, toAccountNo);

		// Create account from private key
		LocalRpcWalletAccount account = new LocalRpcWalletAccount(pkFrom, // Private key
				rpc, // Kalium RPC
				blockProducer); // Using our BlockProducer defined above

		// Send funds
		HexData hash = null;

		// Send funds to another account
		System.out.printf("Send block hash: %s%n", hash);
		try {
			hash = account
					.send(NanoAccount.fromPrivateKey(pkTo, prefix),
							NanoAmount.valueOfNano(String.valueOf(amount / BAN_NAN_MULT)))
					.getHash();
		} catch (WalletActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.printf("Send block hash: %s%n", hash);
		return false;
	}

	double viewAccount(int accountNo) {
		double theBalance = -1;
		HexData privateKey = WalletUtil.deriveKeyFromSeed(seed, accountNo);

		// Create account from private key
		LocalRpcWalletAccount account = new LocalRpcWalletAccount(privateKey, // Private key
				rpc, // Kalium RPC
				blockProducer); // Using our BlockProducer defined above


		System.out.printf("Using account address %s%n", account.getAccount());
		try {
			
			// Receive all pending funds
			//theBalance = account.receiveAll().size();
			
			System.out.printf("Received %,d blocks%n", account.receiveAll().size());
			// Print account info
			theBalance = account.getBalance().getAsNano().doubleValue()*BAN_NAN_MULT;
			System.out.printf("Balance: %s%n", theBalance);
			
			

		} catch (WalletActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		return theBalance;
	}

	public static void main(String args[]) throws WalletActionException, OpenCLInitializerException {
		//System.out.println("Nice");
		Banking b = new Banking();
		b.viewAccount(1);
		//b.send(1, 0, 3);
	
		
	}

}
