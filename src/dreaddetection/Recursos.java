/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dreaddetection;

import java.util.concurrent.Semaphore;


/**
 *
 * @author celso
 */
public class Recursos {
	
        private String nome;
	private int id;
        private final int quant;
        private int availableInstances;
        Semaphore disponivel = new Semaphore(1);
   
	

	
	public Recursos( String nome, int quant, int id) {
		this.nome = nome;
                this.quant = quant;
		this.availableInstances = this.quant;
		this.id = id;
	}

	
	
           public int getAvailableInstances() {
		return this.availableInstances;
	}
           
           
           public void decrementInstances() {
		this.availableInstances--;
            }
	
	public void incrementInstances() {
		this.availableInstances++;
	}
        
        
        public void takeInstance() throws InterruptedException {
		this.disponivel.acquire();
	}
        
        public int availableInstances() {
		return this.disponivel.availablePermits();
	}
        
        //função que liberar o semafaro do recurso, assim o liberando 
        
        public void liberarRecurso() throws InterruptedException {
		disponivel.acquire();
	}
	
	  //função que liberar o semafaro do recurso, na quantidade de processos que o possuem ou tentem assim o liberando 
	public void liberarRecurso(int quant) throws InterruptedException {
		disponivel.acquire(quant);
	}
	
	
	// Getters and Setters
	
	public String getName() {
		return nome;
	}
	
	public void setName(String name) {
		this.nome = nome;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
        public int getQuant() {
		return quant;
	}
	
	
	
	
	@Override
	public String toString() {
		return "Id: " + this.id + "\nName: " + this.nome + "\nAmount: " + this.quant;
	}
	
}
    
 