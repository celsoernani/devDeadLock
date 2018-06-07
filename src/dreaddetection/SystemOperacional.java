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
public class SystemOperacional extends Thread {
    
        Semaphore mutex = new Semaphore(1);
	private ArrayList<Recursos> resources = new ArrayList<>();
	private ArrayList<Processos> processes = new ArrayList<>();
	private int time;
        private final TelaGrafoController telagrafo;
        String lastDead = "";
        
        
        public SystemOperacional(int tempo, TelaGrafoController telagrafo) {
		this.time = tempo;
                this.telagrafo = telagrafo;
	
	}

        
    @Override
    public void run() {
        while(true){
            
          
            try {
                mutex.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(SystemOperacional.class.getName()).log(Level.SEVERE, null, ex);
            }
			ArrayList<Integer> deadlocks = procuradeadlocks();
                        mutex.release();
                            
                    //se hovuer deadlock chama a função que procura e joga em uma string para setar no log
			if(deadlocks != null) {
                            
                                String str = this.deadlockString(deadlocks);
                                if(!str.equals(this.lastDead)) {
					this.lastDead = str;
					this.telagrafo.Log.appendText(this.deadlockString(deadlocks));
				}
                                else{
                                // se houver um deadlock e depois de matar um processo acabar o deadlock da essa mensagem
                                    if(!this.lastDead.equals("")) {
					this.lastDead = "";
					this.telagrafo.Log.appendText("\n");
				}
                                    
                                }
			}
                    //  enquanto nao ocorre deadlock  
                        else{
                            this.telagrafo.Log.appendText("Não tem deadLock \n");
                        }
                       

            try {
                sleep(this.time*1000);
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

		//pegando os recursos disponiveis pode assumir valores 1 e 0
		int a[] = new int[m];
		for(int i = 0; i < m; i++) {
			a[i] = this.resources.get(i).getRecursosDisp();
		}

		// alocando o vetor de recursos existentes na matriz de processos existentes, assim criando o grafo
                //ou seja cada linha é um processo que recebe um vetor de recursos existentes
		int c[][] = new int[n][m];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
                            c[i][j] = this.processes.get(i).getRecursos()[j];
			}
		}
                
                // pegando os recursos que estao sendo solicitados 
                //o processo começa com pedidoatual = -1
                // r[i] == 0 esta bloqueado se maior pega um recurso
                int r[] = new int[n];
		for(int i = 0; i < n; i++) {
			r[i] = this.processes.get(i).getPedidoatual();
		}
           
                
                //variaveis auxiliares
                int runnableProcesses;
		int finishedProcesses = 0;
                

// -1 Significa que o processo não quer nenhum recurso
// -2 Significa que o processo já foi simulado pelo algoritmo
            do {
                        //no coemço nenhum processo é executavel
			runnableProcesses = 0;
                        //faz a busca
			for(int i = 0; i < n; i++) {
                            //se ainda nao tiver pedido recurso ou esta de possse de um recurso qualquer e o recurso atual nao esta dipnivel
				if(r[i] == -1 || r[i] >= 0 && a[r[i]] > 0) {
					for(int k = 0; k < m; k++) {
                                               //percorre todos os processos e os recursos e soma a quantidade de procesos que estao em deadlock
                                                a[k] += c[i][k];
                                                //seta no local da matriz para nao voltar la 
						c[i][k] = 0;
					}
                                        
					finishedProcesses++;
					runnableProcesses++;
					r[i] = -2;
				}
			}
                        //repete isso até percorrer todos os recurssos
		} while(n - finishedProcesses > 1 && runnableProcesses > 0);
            //não há deadlock
            if(n - finishedProcesses <= 1) {
			return null;
		}

		ArrayList<Integer> processesInDeadlock = new ArrayList<Integer>();
                //há deadlock
		for(int i = 0; i < n; i++) {
			if(r[i] >= 0) {
				processesInDeadlock.add(this.processes.get(i).getPid());
                             
                        }
		}

		return processesInDeadlock;

	}

 //função para organizar o print
	private String deadlockString(ArrayList<Integer> pids) {
		String str = "Deadlock:";
		for(Integer pid : pids) {
			str += (" P" + pid);
		}
		return str;
	}
       
 
//Elimina o processo no índice fornecido.

        public void killProcessAtIndex(int id) throws InterruptedException {

		mutex.acquire();
		Processos process = this.processes.get(id);

		this.processes.remove(id);
                        //setando a varivel keepAlive para false
		process.kill();

		// se o processo tiver bloqueado
		if(process.getPedidoatual() >= 0) {
                        //incrementa a variavel dos processos que foram mortos
			this.resources.get(process.getPedidoatual()).deadProcesses++;

			//incrementa o semafaro do recurso que ele tinha
			this.resources.get(process.getPedidoatual()).liberarRecurso();
		}
             
                telagrafo.Log.appendText("O processo "+(id+1)+" morreu\n");
                desenharprocesso(id+1, false);
                

		
                mutex.release();
                

	}
        //sorteia um recurso
        

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
		//põe em uma lista aqueles recursos que podem ainda ser sorteados
                for(int j = 0; j < this.resources.size(); j++) {
			if(process.getRecursos()[j] < this.resources.get(j).getQuant()) {
				possible.add(j);
			}
		}
                    //verificação   
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
        
    




	public void addResources(ArrayList<Recursos> resources) {
		this.resources = resources;               
	}

	public void addProcess(Processos process) {

		this.processes.add(process);
	}

	public void addProcesses(ArrayList<Processos> processes) {
		this.processes = processes;
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
               return (pid-1);
	}

	public int getProccessCount() {
		return this.processes.size();
	}

   
 private void desenharprocesso(int id, boolean draw){
               switch (id) {
            case 1:  telagrafo.ancoraProcesso1.setVisible(draw);
                     break;
            case 2:  telagrafo.ancoraProcesso2.setVisible(draw);
                     break;
            case 3:  telagrafo.ancoraProcesso3.setVisible(draw);
                     break;
            case 4:  telagrafo.ancoraProcesso4.setVisible(draw);
                     break;
            case 5: telagrafo.ancoraProcesso5.setVisible(draw);
                     break;
            case 6:  telagrafo.ancoraProcesso6.setVisible(draw);
                     break;
            case 7:  telagrafo.ancoraProcesso7.setVisible(draw);
                     break;
            case 8:  telagrafo.ancoraProcesso8.setVisible(draw);
                     break;
            case 9:  telagrafo.ancoraProcesso9.setVisible(draw);
                     break;
            case 10: telagrafo.ancoraProcesso10.setVisible(draw);
                     break;
   
            default:
                     break;
        }
            
        
        }
     private void desenharstatus(int id, boolean status){
               switch (id) {
            case 1:  telagrafo.statusPro1.setVisible(status);
                    
                     break;
            case 2:  telagrafo.statusPro2.setVisible(status);
                    
                     break;
            case 3:  telagrafo.statusPro3.setVisible(status);
                     
                     break;
            case 4:  telagrafo.statusPro4.setVisible(status);
                    
                     break;
            case 5: telagrafo.statusPro5.setVisible(status);
                 
                     break;
            case 6:  telagrafo.statusPro6.setVisible(status);
              
                     break;
            case 7:  telagrafo.statusPro7.setVisible(status);
                     
                     break;
            case 8:  telagrafo.statusPro8.setVisible(status);
                    
                     break;
            case 9:  telagrafo.statusPro9.setVisible(status);
                   
                     break;
            case 10: telagrafo.statusPro10.setVisible(status);
                   
                     break;
   
            default:
                     break;
        }
            
        
        }
     
    
}
        
        
   