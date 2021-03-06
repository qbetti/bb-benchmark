package ca.uqac.lif.rv.clientchat;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.Constant;
import ca.uqac.lif.cep.functions.Cumulate;
import ca.uqac.lif.cep.functions.CumulativeFunction;
import ca.uqac.lif.cep.io.ReadLines;
import ca.uqac.lif.cep.tmf.Filter;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.tuples.FetchAttribute;
import ca.uqac.lif.cep.tuples.TupleFeeder;
import ca.uqac.lif.cep.util.Numbers;
import ca.uqac.lif.rv.CSV;
import ca.uqac.lif.rv.Check;
import ca.uqac.lif.rv.clientchat.Increment;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class PipeLinesClientsLimit {

	public static void main(String[] args) throws FileNotFoundException {

		ArrayList<String> sigListConnect = new ArrayList<String>();
		sigListConnect.add("javachat.network.Server.connect");
		ArrayList<String> sigListAdd = new ArrayList<String>();
		sigListAdd.add("java.util.ArrayList.add");
		ArrayList<String> sigListDisconnect = new ArrayList<String>();
		sigListDisconnect.add("javachat.network.Client.disconnected");
		ArrayList<String> sigListObjectCaller = new ArrayList<String>();
		sigListObjectCaller.add("[javachat.network.Server$ClientSocket");

		InputStream is = new FileInputStream("./traces/JavaChat.csv");
		ReadLines rl = new ReadLines(is);
		TupleFeeder tf = new TupleFeeder();
		Fork forkTuple = new Fork(3);
		Filter fil = new Filter();

		Processor limit = new ApplyFunction(new Constant(5));
		Processor getMethodNameCon = new ApplyFunction(new FetchAttribute(CSV.Fields.METHOD_NAME));
		Processor getMethodNameDis = new ApplyFunction(new FetchAttribute(CSV.Fields.METHOD_NAME));
		Processor getObjectName = new ApplyFunction(new FetchAttribute(CSV.Fields.CALLING_OBJECT));
		Processor checkConnection = new ApplyFunction(new Check(sigListAdd));
		Processor checkObject = new ApplyFunction(new Check(sigListObjectCaller));
		Processor checkDisconnection = new ApplyFunction(new Check(sigListDisconnect));
		Processor incrementCon = new ApplyFunction(new Increment());
		Processor incrementDis = new ApplyFunction(new Increment());

		Cumulate sum_con = new Cumulate(
				new CumulativeFunction<Number>(Numbers.addition));
		Cumulate sum_dis = new Cumulate(
				new CumulativeFunction<Number>(Numbers.addition));

		ApplyFunction subtraction = new ApplyFunction(Numbers.subtraction);
		ApplyFunction inferior = new ApplyFunction(Numbers.isLessThan);

		Connector.connect(rl, tf, forkTuple);
		Connector.connect(forkTuple, 0, fil, 0);
		Connector.connect(forkTuple, 1, getObjectName, 0);
		Connector.connect(getObjectName,checkObject);
		Connector.connect(checkObject,0,fil,1);
		Connector.connect(fil,getMethodNameCon);
		Connector.connect(getMethodNameCon,checkConnection);
		Connector.connect(checkConnection,incrementCon);
		Connector.connect(incrementCon,sum_con);

		Connector.connect(forkTuple,2,getMethodNameDis,0);
		Connector.connect(getMethodNameDis,checkDisconnection);
		Connector.connect(checkDisconnection,incrementDis);
		Connector.connect(incrementDis,sum_dis);

		Connector.connect(sum_con, 0, subtraction, 0);
		Connector.connect(sum_dis, 0, subtraction, 1);
		Connector.connect(subtraction, 0, inferior, 0);
		Connector.connect(limit, 0, inferior, 1);

		Pullable p = inferior.getPullableOutput();

		long timeStart = System.currentTimeMillis();
		int i = 2;

		System.out.println("Running...");

		while (p.hasNext()) {
			Boolean v = (Boolean) p.pull();

			if (!v)
				System.out.println("Line: "+i+", Property violated." );
			else
				System.out.println("Line: "+i+", Property respected." );
			i++;
		}

		System.out.println("Done in " + (System.currentTimeMillis() - timeStart) + " ms");

	}

}
