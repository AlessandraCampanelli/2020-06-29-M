package it.polito.tdp.imdb.model;

import java.util.Comparator;

public class ComparatoreAttori implements  Comparator<DirectorPeso> {

	public int compare(DirectorPeso a,DirectorPeso b) {
		return -(int)(a.getPeso()-b.getPeso());
	}
	
	
}
