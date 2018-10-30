package ca.uqac.lif.rv;

import ca.uqac.lif.cep.functions.UnaryFunction;

public class SplitParamToArray extends UnaryFunction<Object, String[]> {


    public SplitParamToArray() {
        super(Object.class, String[].class);
    }

    @Override
    public String[] getValue(Object attr) {
        String params = (String) attr;

        if (params.equals("NA") || params.isEmpty())
            return new String[]{};

        return params.split(CSV.PARAM_SEP);
    }
}
