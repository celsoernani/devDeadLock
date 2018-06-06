package dreaddetection;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author celso
 */
public class Processos extends Thread{
    
    private static int lastPid = 1;
    private int Pid;
    //recursos já realizados
    private LinkedList<Recursos> resourcesHeld = new LinkedList<>();
    //tempo de recursos
    private ArrayList<Integer> resourcesTimes = new ArrayList<Integer>(); 
    private int processRequestTime;
    private int processUsageTime;
    private int[] recursos;
    private Recursos requestedResouce;
    private int currentRequest = -1;
    private SystemOperacional sistemaOperacional;
    private boolean keepAlive = true;
    private final TelaGrafoController telagrafo;
    Semaphore mutex = new Semaphore(1);
    
    
    //construtor
    public Processos(SystemOperacional so,int processUsageTime, int processRequestTime, int numRecursos,  TelaGrafoController telagrafo){
        this.Pid = lastPid++;
        this.processRequestTime = processRequestTime;
        this.processUsageTime = processUsageTime;
        this.recursos = new int[numRecursos];
        this.telagrafo = telagrafo;
        this.sistemaOperacional = so;
    } 
    
    @Override
    public void run() {
        //auxiliares de tempo
        int aux = 0;
        int aux2 =0;
   
        
        //variavel keepalive serve para manter o processo vivo
        while(keepAlive){
            
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Processos.class.getName()).log(Level.SEVERE, null, ex);
            }
            aux++;
            
            if(aux%processRequestTime==0){
              //quando o tempo em segundos de solicitação passar ele tem que solicitar um recurso ainda nao usado por ele próprio  
                try {
                    mutex.acquire();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Processos.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                                    //sorteia o processo aleatoriamente 
                                    currentRequest  =sistemaOperacional.randomRecurse(this.Pid);
                                  
                   if(currentRequest >= 0) {
                                //fila de recursos recebe o recursso atual
                            requestedResouce = sistemaOperacional.getResourceById(currentRequest + 1);
                            telagrafo.Log.appendText("P"+this.Pid+" solicitou "+requestedResouce.getName());
                            //se não houver processos disponiveis bloqueia o processo
                           if(requestedResouce.getAvailableInstances() == 0)
					{
						telagrafo.Log.appendText("P"+this.Pid+" bloqueiou com  "+requestedResouce.getName());	
					}

                    try {
                        mutex.acquire();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Processos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if(this.keepAlive) {

                                mutex.release();
						//decrementa os recursos disponiveis
						requestedResouce.decrementInstances();

						//bota ele no array de recursos
						this.recursos[currentRequest]++;
						resourcesHeld.add(requestedResouce);
                                                //bota na fila de tempos // time line
						resourcesTimes.add(processUsageTime);

						//o atual processo possui um recursos 
						currentRequest = -1;

						//process runs for a certain amount of time
						telagrafo.Log.appendText( "P"+this.Pid+" roda com "+requestedResouce.getName());

                                try {
                                    mutex.acquire();
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Processos.class.getName()).log(Level.SEVERE, null, ex);
                                }

					} else {
                                try {
                                    requestedResouce.liberarRecurso();
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Processos.class.getName()).log(Level.SEVERE, null, ex);
                                }
					}

				}
			} else {
                try {
                    mutex.acquire();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Processos.class.getName()).log(Level.SEVERE, null, ex);
                }
			}
                        decrementResourcesTimes(resourcesTimes);
			aux2 = resourcesTimesIsZero(resourcesTimes);
                        
                        if(aux2!=-1)
			{
				// Logging the resource release
				telagrafo.Log.appendText("P"+this.Pid + " liberou " + resourcesHeld.get(aux2).getName());

				// Removing resource data from the arrays
				resourcesTimes.remove(aux2);
				this.recursos[resourcesHeld.get(aux2).getId()-1]--;
				resourcesHeld.get(aux2).incrementInstances();
                try {
                    resourcesHeld.get(aux2).liberarRecurso();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Processos.class.getName()).log(Level.SEVERE, null, ex);
                }
				resourcesHeld.remove(aux2);
			}
                   }
                    telagrafo.Log.appendText("P" + this.Pid + " finalizou");
                    
            }
                  
                //matar processo
	public void kill() {
		this.keepAlive = false;
	}

    //conta até o tempo ser -1 um, então liberando o recurso
    private int resourcesTimesIsZero(ArrayList<Integer> resourcesTimes) {
		int i = 0;
		for (Integer time : resourcesTimes) {
			if(time==0)
			{
				return i;
			}
			i++;
		}
		return -1;
	}

	private void decrementResourcesTimes(ArrayList<Integer> resourcesTimes) {
		int i = 0;
		for (Integer time : resourcesTimes) {

			resourcesTimes.set(i, --time);
			i++;
		}

	}


    // Getters and Setters
        
        public int[] getResourcesInstances() {
		return recursos;
	}
 

	public void setRecursos(int[] recursos) {
		this.recursos = recursos;
	}

	

	public int getPedidoatual() {
		return currentRequest;
	}
       
        @Override
	public String toString() {
		return "Processo: " + this.Pid;
	}

	public int getPid() {
		return this.Pid;
	}

	public void setKeepAlice(boolean b) {
		keepAlive = false;

	}
        
	public int getCurrentRequest() {
		return currentRequest;
	}

      public int[] obterRecursos() {
        return recursos;
       
        }

    

    
    

}
