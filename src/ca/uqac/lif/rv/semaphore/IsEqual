package ca.uqac.lif.rv.semaphore;

import ca.uqac.lif.cep.functions.BinaryFunction;
import ca.uqac.lif.cep.functions.UnaryFunction;

public final class IsEqual extends BinaryFunction<Number,Number,Boolean>
{
	protected  IsEqual()
	{
		super(Number.class, Number.class, Boolean.class);
	}
	
	@Override
	public Boolean getValue(Number x, Number y)
	{
		return x.floatValue() == y.floatValue();
	}
}
