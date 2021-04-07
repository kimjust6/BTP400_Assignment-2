//package bankingV19_1_deprecated;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import uk.oczadly.karl.jnano.model.HexData;
//import uk.oczadly.karl.jnano.model.NanoAccount;
//import uk.oczadly.karl.jnano.model.NanoAmount;
//import uk.oczadly.karl.jnano.rpc.RpcQueryNode;
//import uk.oczadly.karl.jnano.rpc.util.wallet.LocalRpcWalletAccount;
//import uk.oczadly.karl.jnano.rpc.util.wallet.RpcWalletSpecification;
//import uk.oczadly.karl.jnano.rpc.util.wallet.WalletActionException;
//import uk.oczadly.karl.jnano.util.WalletUtil;
//import uk.oczadly.karl.jnano.util.workgen.OpenCLWorkGenerator;
//import uk.oczadly.karl.jnano.util.workgen.OpenCLWorkGenerator.OpenCLInitializerException;
//
//public class Banking {
//
//	static RpcWalletSpecification spec;
//	static HexData seed = new HexData("991A38BED0D022D6622E9AD47513E2A14AC0DA58F15D8AFC81075DEC11CAF29D");
//	
//	final String node = "https://kaliumapi.appditto.com/api";
//	final String rep = "ban_1fomoz167m7o38gw4rzt7hz67oq6itejpt4yocrfywujbpatd711cjew8gjj";
//	final String prefix = "ban";
//	
//	
//	Banking() {
//		try {
//			// Construct the specification
//			spec = RpcWalletSpecification.builder()
//					//declare which node you want to connect to
//					.rpcClient(new RpcQueryNode(new URL(node)))
////					.rpcClient(new RpcQueryNode(new URL("http://192.168.1.167:7072")))  //this is my local node
//					.defaultRepresentative(rep) //choose a representative
////					.workGenerator(new CPUWorkGenerator())
//					.workGenerator(new OpenCLWorkGenerator()) //gpu acceleration
//					.addressPrefix(prefix) //change the prefix
//					.build();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (OpenCLInitializerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	//amount you want to send and the account number you want to send to
//	boolean send(int fromAccountNo, int toAccountNo, double amount) {
//		
//		
//		HexData pkFrom = WalletUtil.deriveKeyFromSeed(seed, fromAccountNo);
//		HexData pkTo = WalletUtil.deriveKeyFromSeed(seed, toAccountNo);
//		LocalRpcWalletAccount account = new LocalRpcWalletAccount(spec, pkFrom);
//		
//		// Send funds
//		HexData hash = null;
//		
//		try {
//			hash = account.sendFunds(
//					NanoAccount.fromPrivateKey(pkTo,prefix),
//					NanoAmount.valueOfNano(String.valueOf(amount/10)));
//		} catch (WalletActionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.printf("Send block hash: %s%n", hash);
//		return false;
//	}
//
//	double viewAccount(int accountNo) {
//		double theBalance = -1;
//		HexData privateKey = WalletUtil.deriveKeyFromSeed(seed, accountNo);
//		LocalRpcWalletAccount account = new LocalRpcWalletAccount(spec, privateKey);
//		// Receive pending funds
//		try {
//			account.receiveAllPending();
//			theBalance = account.getBalance().getAsNano().doubleValue()*10;
//			System.out.printf("AccountNo: " + accountNo + "\nBalance: %s%n", theBalance + "\nAddress: " + account.getAddress());
//		} catch (WalletActionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		return theBalance;
//	}
//
//	public static void main(String args[]) throws WalletActionException
//	 {
//
//		Banking b = new Banking();
//		b.viewAccount(1);
//		b.send(1, 0, 1);
//
//	}
//
//}
