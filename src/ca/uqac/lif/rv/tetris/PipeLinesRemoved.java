package ca.uqac.lif.rv.tetris;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.io.ReadLines;
import ca.uqac.lif.cep.ltl.Troolean;
import ca.uqac.lif.cep.ltl.TrooleanCast;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.tuples.FetchAttribute;
import ca.uqac.lif.cep.tuples.TupleFeeder;
import ca.uqac.lif.rv.CSV;
import ca.uqac.lif.rv.Eventually;
import ca.uqac.lif.rv.SplitParamToArray;
import ca.uqac.lif.rv.TrooleanToBoolean;
import jdk.nashorn.internal.runtime.PrototypeObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PipeLinesRemoved {

    public static void main(String[] args) throws FileNotFoundException {

        InputStream is = new FileInputStream("C:\\Users\\quent\\Downloads\\TraceTetris1 (1).csv");
        ReadLines rl = new ReadLines(is);
        TupleFeeder tf = new TupleFeeder();
        Fork forkTuple = new Fork(2);

        Connector.connect(rl, tf, forkTuple);


        Processor getMethodName = new ApplyFunction(new FetchAttribute(CSV.Fields.METHOD_NAME));
        Processor getParamValues = new ApplyFunction(new FetchAttribute(CSV.Fields.METHOD_PARAM_VALUES));

        Connector.connect(forkTuple, 0, getMethodName, 0);
        Connector.connect(forkTuple, 1, getParamValues, 0);

        Fork forkMethodName = new Fork(2);
        Connector.connect(getMethodName, forkMethodName);

        Processor splitValues = new ApplyFunction(new SplitParamToArray());
        Connector.connect(getParamValues, splitValues);

        Processor generateNewPiece = new ApplyFunction(new GenerateNewPiece());
        Connector.connect(forkMethodName, 0, generateNewPiece, 0);

        Processor hasFallingFinished = new ApplyFunction(new HasFallingFinished());
        Connector.connect(forkMethodName, 1, hasFallingFinished, 0);
        Connector.connect(splitValues, 0, hasFallingFinished, 1);

        Processor not = new ApplyFunction(Troolean.NOT_FUNCTION);
        Connector.connect(hasFallingFinished, not);

        Processor and = new ApplyFunction(Troolean.AND_FUNCTION);
        Connector.connect(generateNewPiece, 0, and, 0);
        Connector.connect(not, 0, and, 1);


        Processor eventually = new ApplyFunction(new Eventually());
        Connector.connect(and, eventually);

        Processor notEventually = new ApplyFunction(Troolean.NOT_FUNCTION);
        Connector.connect(eventually, notEventually);




        Processor end = notEventually;

        Pullable p = end.getPullableOutput();

        int i = 1;
        while (p.hasNext()) {
            i++;
           Troolean.Value v = (Troolean.Value) p.pull();
           System.out.println(i+" "+v);

            if (v == Troolean.Value.FALSE) {
                System.out.print("");
            }
        }


    }


}
