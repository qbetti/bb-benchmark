package ca.uqac.lif.rv.iskey;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.BinaryFunction;
import ca.uqac.lif.cep.io.ReadLines;
import ca.uqac.lif.cep.tmf.Filter;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.tuples.FetchAttribute;
import ca.uqac.lif.cep.tuples.TupleFeeder;
import ca.uqac.lif.rv.CSV;
import ca.uqac.lif.rv.SplitParamToArray;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PipeIsKey {

    public static void main(String[] args) throws FileNotFoundException {

        InputStream is = new FileInputStream("./traces/AESenc.csv");
        ReadLines rl = new ReadLines(is);
        TupleFeeder tf = new TupleFeeder();
        Fork forkTuple = new Fork(3);
        Connector.connect(rl, tf, forkTuple);

        Processor getParamValues = new ApplyFunction(new FetchAttribute(CSV.Fields.METHOD_PARAM_VALUES));
        Processor getMethodName = new ApplyFunction(new FetchAttribute(CSV.Fields.METHOD_NAME));
        Processor getMethodDepth = new ApplyFunction(new FetchAttribute(CSV.Fields.METHOD_CALL_DEPTH));
        Connector.connect(forkTuple, 0, getParamValues, 0);
        Connector.connect(forkTuple, 1, getMethodName, 0);
        Connector.connect(forkTuple, 2, getMethodDepth, 0);

        Processor splitParamValues = new ApplyFunction(new SplitParamToArray());
        Connector.connect(getParamValues, splitParamValues);

        Processor methodMatches = new ApplyFunction(
                new BinaryFunction<Object, Object, Boolean>(Object.class, Object.class, Boolean.class) {
                    @Override
                    public Boolean getValue(Object methodName, Object methodDepth) {
                        return ((String) methodName).contains("AESenc.encrypt") && ((String) methodDepth).equals("1");
                    }
        });
        Connector.connect(getMethodName, 0, methodMatches, 0);
        Connector.connect(getMethodDepth, 0, methodMatches, 1);

        Filter filterEncryptEvents = new Filter();
        Connector.connect(splitParamValues, 0, filterEncryptEvents, 0);
        Connector.connect(methodMatches, 0, filterEncryptEvents, 1);

        Processor isKey = new ApplyFunction(new IsKey());
        Connector.connect(filterEncryptEvents, isKey);

        Processor ending = isKey;
        Pullable p = ending.getPullableOutput();

        boolean isViolated = false;
        long timeStart = System.currentTimeMillis();

        while (p.hasNext()) {
            Boolean b = (Boolean) p.pull();

            if (!b && !isViolated) {
                System.out.println("Property is violated");
                isViolated = true;
            }
        }

        if (!isViolated) {
            System.out.println("Property is verified!");
        }

        System.out.println("Done in " + (System.currentTimeMillis() - timeStart) + " ms");
    }
}
