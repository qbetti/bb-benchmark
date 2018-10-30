package ca.uqac.lif.rv.tetris;

import ca.uqac.lif.cep.functions.BinaryFunction;
import ca.uqac.lif.cep.ltl.Troolean;
import ca.uqac.lif.cep.ltl.TrooleanCast;

public class HasFallingFinished extends BinaryFunction<String, String[], Troolean.Value> {

    private Troolean.Value hasFinishedFalling;

    public HasFallingFinished() {
        super(String.class, String[].class, Troolean.Value.class);
        hasFinishedFalling = Troolean.Value.TRUE;
    }

    @Override
    public Troolean.Value getValue(String methodName, String[] params) {

        if (methodName.equals("tetris.Board.isFallingFinished") && params.length == 1) {
            boolean value = Boolean.valueOf(params[0]);
            hasFinishedFalling = TrooleanCast.instance.getValue(value);
            return hasFinishedFalling;
        }
        else {
            return hasFinishedFalling;
        }
    }
}
