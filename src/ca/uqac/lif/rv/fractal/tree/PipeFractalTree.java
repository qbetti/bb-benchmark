package ca.uqac.lif.rv.fractal.tree;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.io.ReadLines;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.tuples.FetchAttribute;
import ca.uqac.lif.cep.tuples.TupleFeeder;
import ca.uqac.lif.rv.CSV;
import ca.uqac.lif.rv.SplitParamToArray;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PipeFractalTree {

    public static void main(String[] args) throws FileNotFoundException {

        InputStream is = new FileInputStream("./FractalTree.csv");
        Processor rl = new ReadLines(is);
        Processor tf = new TupleFeeder();

        Connector.connect(rl, tf);

        Fork forkTuple = new Fork(2);
        Processor getParamValues = new ApplyFunction(new FetchAttribute(CSV.Fields.METHOD_PARAM_VALUES));
        Processor getMethodName = new ApplyFunction(new FetchAttribute(CSV.Fields.METHOD_NAME));

        Connector.connect(tf, forkTuple);
        Connector.connect(forkTuple, 0, getParamValues, 0);
        Connector.connect(forkTuple, 1, getMethodName, 0);

        Processor splitParamValues = new ApplyFunction(new SplitParamToArray());
        Connector.connect(getParamValues, splitParamValues);

        Processor comparePosition = new ApplyFunction(new ComparePosition());
        Connector.connect(splitParamValues, 0, comparePosition, 0);
        Connector.connect(getMethodName, 0, comparePosition, 1);


        Processor endingProc = comparePosition;

        Pullable p = endingProc.getPullableOutput();

        int counter = 2;
        boolean isViolated = false;
        long timeStart = System.currentTimeMillis();

        System.out.println("Running...");

        while (p.hasNext()) {
            Boolean b = (Boolean) p.pull();

            if (!b && !isViolated) {
                System.out.println("Property is violated at line " + counter);
                isViolated = true;
            }
            counter++;
        }

        if (!isViolated) {
            System.out.println("Property is verified!");
        }

        System.out.println("Done in " + (System.currentTimeMillis() - timeStart) + " ms");
    }
}
