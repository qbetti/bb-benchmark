package ca.uqac.lif.rv.semaphore;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.Cumulate;
import ca.uqac.lif.cep.functions.CumulativeFunction;
import ca.uqac.lif.cep.io.ReadLines;
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

public class PipeLinesSafeLock {

	public static void main(String[] args) throws FileNotFoundException {

		ArrayList<String> sigListAcq = new ArrayList<String>();
		sigListAcq.add("java.util.concurrent.Semaphore.acquire");
		ArrayList<String> sigListRea = new ArrayList<String>();
		sigListRea.add("java.util.concurrent.Semaphore.release");


		InputStream is = new FileInputStream("./traces/Semaphore.csv");
		ReadLines rl = new ReadLines(is);
		TupleFeeder tf = new TupleFeeder();
		Fork forkTuple = new Fork(3);

		Connector.connect(rl, tf, forkTuple);

		Processor checkAcquire = new ApplyFunction(new Check(sigListAcq));
		Processor checkRelease = new ApplyFunction(new Check(sigListRea));

		Processor getMethodNameRel = new ApplyFunction(new FetchAttribute(CSV.Fields.METHOD_NAME));
		Processor getMethodNameAcq = new ApplyFunction(new FetchAttribute(CSV.Fields.METHOD_NAME));

		Processor incrementRel = new ApplyFunction(new Increment());
		Processor incrementAcq = new ApplyFunction(new Increment());
		Cumulate sum_Rel = new Cumulate(
				new CumulativeFunction<Number>(Numbers.addition));
		Cumulate sum_Acq  = new Cumulate(
				new CumulativeFunction<Number>(Numbers.addition));

		ApplyFunction equal = new ApplyFunction(new IsEqual());


		Connector.connect(forkTuple, 0, getMethodNameAcq, 0);
		Connector.connect(forkTuple, 1, getMethodNameRel, 0);
		Connector.connect(getMethodNameAcq,checkAcquire);
		Connector.connect(getMethodNameRel,checkRelease);
		Connector.connect(checkAcquire,incrementAcq);
		Connector.connect(incrementAcq,sum_Acq);
		Connector.connect(checkRelease,incrementRel);
		Connector.connect(incrementRel,sum_Rel);
		Connector.connect(sum_Rel,0,equal,0);
		Connector.connect(sum_Acq,0,equal,1);


		Pullable p = equal.getPullableOutput();


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
