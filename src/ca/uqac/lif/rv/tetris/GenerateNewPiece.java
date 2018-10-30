package ca.uqac.lif.rv.tetris;

import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.ltl.Troolean;
import ca.uqac.lif.cep.ltl.TrooleanCast;

public class GenerateNewPiece extends UnaryFunction<String, Troolean.Value> {

    public GenerateNewPiece() {
        super(String.class, Troolean.Value.class);
    }

    @Override
    public Troolean.Value getValue(String methodName) {
        return TrooleanCast.instance.getValue(methodName.equals("tetris.Board.newPiece"));
    }
}
