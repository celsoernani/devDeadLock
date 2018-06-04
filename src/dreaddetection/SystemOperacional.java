/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dreaddetection;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author celso
 */
public class SystemOperacional implements Runnable {
    
        Semaphore mutex = new Semaphore(1);
	private ArrayList<Recursos> resources = new ArrayList<Recursos>();
	private ArrayList<Processos> processes = new ArrayList<Processos>();
	private int time;
        
        
        public SystemOperacional(int tempo) {
		this.time = tempo;
	
	}

        
    @Override
    public void run() {
        while(true){
            
          
            try {
                mutex.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(SystemOperacional.class.getName()).log(Level.SEVERE, null, ex);
            }
			ArrayList<Integer> deadlocks = this.procuradeadlocks();
                        mutex.release();
                            
                    //se não tiver deadlock avisa no log
			if(deadlocks != null) {
                           
				System.out.println(deadlocks);
			}

            try {
                sleep(this.time);
            } catch (InterruptedException ex) {
                Logger.getLogger(SystemOperacional.class.getName()).log(Level.SEVERE, null, ex);
            }
		}
        
        
                    
        
        }

 //Verifica se há um deadlock acontecendo.
//return null se não houver processos em deadlock.
//se houver retorna um ArrayList com os ids dos processos em deadlock, se houver algum.

    private ArrayList<Integer> procuradeadlocks() {
                int n = this.processes.size();
		int m = this.resources.size();

		//pegando os recursos disponiveis
		int a[] = new int[m];
		for(int i = 0; i < m; i++) {
			a[i] = this.resources.get(i).availableInstances();
		}

		// pegando os recursos que estao ocupados 
		int c[][] = new int[n][m];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
                            c[i][j] = this.processes.get(i).getResourcesInstances()[j];
			}
		}
                
                // pegando os recursos que estao sendo solicitados 
                int r[] = new int[n];
		for(int i = 0; i < n; i++) {
			r[i] = this.processes.get(i).getCurrentRequest();
		}
           
                int runnableProcesses;
		int finishedProcesses = 0;
                

// -1 Significa que o processo não quer nenhum recurso
// -2 Significa que o processo já foi simulado pelo algoritmo
            do {
			runnableProcesses = 0;
			for(int i = 0; i < n; i++) {
				if(r[i] == -1 || r[i] >= 0 && a[r[i]] > 0) {
					for(int k = 0; k < m; k++) {
						a[k] += c[i][k];
						c[i][k] = 0;
					}
					finishedProcesses++;
					runnableProcesses++;
					r[i] = -2;
				}
			}
		} while(n - finishedProcesses > 1 && runnableProcesses > 0);
            if(n - finishedProcesses <= 1) {
			return null;
		}

		ArrayList<Integer> processesInDeadlock = new ArrayList<Integer>();
		for(int i = 0; i < n; i++) {
			if(r[i] >= 0) {
				processesInDeadlock.add(this.processes.get(i).getPid());
			}
		}

		return processesInDeadlock;

	}

 
	private String deadlockString(ArrayList<Integer> pids) {
		String str = "Deadlock:";
		for(Integer pid : pids) {
			str += (" P" + pid);
		}
		return str;
	}
       
 
//Elimina o processo no índice fornecido.
// se matar todos os processos, retorna verdaderiro 
        public boolean killProcessAtIndex(int id) throws InterruptedException {

		mutex.acquire();

		// Obtendo os recursos usadas pelo processo
		int[] resourcesIndexes = this.processes.get(id).getResourcesInstances();


		//mato o processo pela id
		this.processes.get(id).kill();
                //tiro ele da fila
		this.processes.remove(id);

		// libero os recursos que perteciam ao processo
		for(int i = 0; i < resourcesIndexes.length; i++) {
			this.resources.get(i).incrementInstances();
			this.resources.get(i).liberarRecurso(resourcesIndexes[i]);
		}

		boolean empty = this.processes.isEmpty();
		
                mutex.release();

		return empty;

	}
        

   public int randomRecurse(int id) {
        Processos process;

		int i = 0;
                //enquanto nao encontrar o processo procurado..
		while(i < this.processes.size() && this.processes.get(i).getPid() != id) {
			i++;
		}
                        //se não encontrar return -1
		if(i >= this.processes.size()) {
			return -1;
		}

		process = this.processes.get(i);

	
		ArrayList<Integer> possible = new ArrayList<>();
		for(int j = 0; j < this.resources.size(); j++) {
			if(process.obterRecursos()[j] < this.resources.get(j).getQuant()) {
				possible.add(j);
			}
		}

		if(possible.isEmpty()) {
			return -1;
		}

		Random random = new Random();
		return possible.get(random.nextInt(possible.size()));
                
    }

   
public Recursos getResourceById(int id)
	{
		for (Recursos rs : resources) {
			if(rs.getId()==id)
				return rs;
		}
		return null;
	}

// Getters and Setters

	public int getTime() {
		return time;
	}
        
    


	public void addResource(Recursos resource) {
		this.resources.add(resource);
	}

	public void addResources(ArrayList<Recursos> resources) {
		this.resources.addAll(resources);
	}

	public void addProcess(Processos process) {

		this.processes.add(process);
	}

	public void addProcesses(ArrayList<Processos> processes) {
		this.processes.addAll(processes);
	}

        //matar todos os processos
	public void restartSystem()
	{
		for (Processos process : processes) {
			process.setKeepAlice(false);
		}

		processes.clear();
		resources.clear();

	}

        
	public Recursos getResourceAt(int index) {	
		return resources.get(index);
	}

	
        public Recursos getRecursoid(int id)
	{
		for (Recursos rs : resources) {
			if(rs.getId()==id)
				return rs;
		}
		return null;
	}
	
	
	public int getindexProcess(int pid) {
		for(int i = 0; i < this.processes.size(); i++) {
			if(this.processes.get(i).getPid() == pid) {
				return i;
			}
		}
		return -1;
	}

	public int getProccessCount() {
		return this.processes.size();
	}

	public Object[][] recuperarProcessos() {
		Object[][] data = new Object[processes.size()][resources.size()];
		int i, j;
		for (Processos proc : processes) {
			for(i = 0,j=0; j<resources.size();j++)
			{
				data[i][j] = proc.getResourcesInstances()[j];
			}
			i++;

		}
		return data;
	}


   

}
        
        
   