package it.polito.tdp.imdb.model;

import java.util.ArrayList; 
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.sun.tools.javac.util.List;

import it.polito.tdp.imdb.model.Adiacenza;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private int sommaAttori;
	private int sommaAttoriFinale;
	private LinkedList<Director> soluzioneMigliore ;
	private ImdbDAO dao;
	private SimpleWeightedGraph<Director,DefaultWeightedEdge> grafo;
	private Map <Integer,Director>idMap;
	public Model() {
		this.dao = new ImdbDAO();
		idMap= new HashMap <Integer,Director> ();
	}
	public void creaGrafo(Integer anno){
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao.listAllDirectors(idMap,anno);
		Graphs.addAllVertices(grafo, idMap.values());
		
		for(Adiacenza a:dao.getAdiacenze(idMap, anno))
		{
			if(this.grafo.getEdge(a.getD1(), a.getD2())==null)//SE NON HO NESSUN ARCO, DEVO SEMPRE FARE QUESTO CONTROLLO
		 Graphs.addEdgeWithVertices(grafo, a.getD1(), a.getD2(), a.getPeso());
		}
	}
	public Set<Director> getVertexSet(){
		return this.grafo.vertexSet();
	}
	
	public Set<DefaultWeightedEdge> getEdgeSet(){
		return this.grafo.edgeSet();
	}
	
	public LinkedList<DirectorPeso> trovaVicini  (Director d ){
		LinkedList<DirectorPeso> lista = new LinkedList<>();
		for(Director director : Graphs.neighborListOf(grafo, d)) {
			Integer peso = (int) grafo.getEdgeWeight(grafo.getEdge(d, director));
			lista.add(new DirectorPeso(director, peso));}
		//FINIRE
		Collections.sort(lista);
		return lista;
	}
	private double peso;
	public LinkedList<Director> trovaPercorso(Director d, int c){
		
		peso=0;
		soluzioneMigliore =new LinkedList<Director>();
		LinkedList<Director>parziale =new LinkedList<>();
		parziale.add(d);
		cerca(c,parziale);
		return soluzioneMigliore;
	}
	
	private void cerca(int c,LinkedList<Director> parziale) {
	//CASI TERMINALI
		//in questo caso io ho due condizioni terminali 

		// che il numero di attori condivisi sia  minore del massimo
     //ma che il numero di registi sia massimo
		//Quindi la prima condizione di terminazione deve essere sul numero di registi e non sul peso 
		
		
		if(peso==c) { 
			if(parziale.size()>this.soluzioneMigliore.size()) {
				soluzioneMigliore = new LinkedList<Director>(parziale);
			
			}
			return;
		}
		else if(peso<c && parziale.size()>this.soluzioneMigliore.size()){ 
			soluzioneMigliore = new LinkedList<Director>(parziale);
		}
		for(Director vicino:Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			
			DefaultWeightedEdge e= this.grafo.getEdge(parziale.get(parziale.size()-1), vicino);//primo passo
			 int t=(int) this.grafo.getEdgeWeight(e);
			
			 if(!parziale.contains(vicino) && (peso+t)<=c ) {
				 
				 parziale.add(vicino);
				 peso=peso+t;
				 cerca(c,parziale);
				 //DEVO RIMUOVERE ANCHE IL PESO 
				 peso=peso-t;
				 parziale.remove(parziale.size()-1);
			 }
	}}

}
