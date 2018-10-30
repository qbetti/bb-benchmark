package ca.uqac.lif.rv;

import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.ltl.Troolean;

public class TrooleanToBoolean extends UnaryFunction<Troolean.Value, Boolean> {


    public TrooleanToBoolean() {
        super(Troolean.Value.class, Boolean.class);
    }

    @Override
    public Boolean getValue(Troolean.Value value) {
        if (value == Troolean.Value.TRUE)
            return true;
        else if (value == Troolean.Value.FALSE)
            return false;
        else
            return false;
    }
}
