package ca.uqac.lif.rv;

import java.util.ArrayList;

import ca.uqac.lif.cep.functions.UnaryFunction;

public class Check extends UnaryFunction<Object,Boolean>
{

	int i;
	ArrayList<String> sigList;

	public Check(ArrayList<String> sigList){

		super(Object.class, Boolean.class);
		this.sigList=sigList;
	}

	@Override
	public Boolean getValue(Object x){
		for(int i =0; i < sigList.size(); i++){
			if (((String)x).startsWith(sigList.get(i))){
				return true;
			}
		}
		return false;
	}
}
