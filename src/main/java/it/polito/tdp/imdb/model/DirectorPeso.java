package it.polito.tdp.imdb.model;

public class DirectorPeso implements Comparable<DirectorPeso> {
	
private Director d;
private Integer peso;

public Director getD() {
	return d;
}
public void setD(Director d) {
	this.d = d;
}
public Integer  getPeso() {
	return peso;
}
public void setPeso (Integer peso) {
	this.peso = peso;
}
public DirectorPeso(Director d, Integer peso) {
	super();
	this.d = d;
	this.peso = peso;
}

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((d == null) ? 0 : d.hashCode());
	long temp;
	temp = Double.doubleToLongBits(peso);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	DirectorPeso other = (DirectorPeso) obj;
	if (d == null) {
		if (other.d != null)
			return false;
	} else if (!d.equals(other.d))
		return false;
	if (Double.doubleToLongBits(peso) != Double.doubleToLongBits(other.peso))
		return false;
	return true;
}

@Override
public String toString() {
	return "DirectorPeso [d=" + d + ", peso=" + peso + "]";
}
@Override
public int compareTo(DirectorPeso o) {
	
	return this.getPeso().compareTo(o.getPeso());
}

}
