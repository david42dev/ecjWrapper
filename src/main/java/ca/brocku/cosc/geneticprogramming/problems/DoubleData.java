/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package ca.brocku.cosc.geneticprogramming.problems;

import ec.gp.GPData;

public class DoubleData extends GPData {

	public double x;    // return value

	public void copyTo(final GPData gpd)   // copy my stuff to another DoubleData
	{
		((DoubleData) gpd).x = x;
	}
}


