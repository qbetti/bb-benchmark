package ca.uqac.lif.rv.clientchat;

import ca.uqac.lif.cep.functions.UnaryFunction;

public class Increment extends UnaryFunction<Boolean,Number>{
	
	public Increment(){
		super(Boolean.class, Number.class);
		}
	
	@Override
	public Number getValue(Boolean x){
		if (x)
			return 1;
		else
			return 0;
		}
	}
