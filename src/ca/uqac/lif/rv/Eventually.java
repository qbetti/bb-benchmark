package ca.uqac.lif.rv;

import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.ltl.Troolean;

public class Eventually extends UnaryFunction<Troolean.Value, Troolean.Value> {

    private Troolean.Value result;

    public Eventually() {
        super(Troolean.Value.class, Troolean.Value.class);
    }


    @Override
    public Troolean.Value getValue(Troolean.Value value) {

        if (result == Troolean.Value.TRUE) {
            return result;
        } else if (value == Troolean.Value.TRUE) {
            result = value;
            return result;
        } else {
            result = Troolean.Value.INCONCLUSIVE;
            return result;
        }
    }
}
