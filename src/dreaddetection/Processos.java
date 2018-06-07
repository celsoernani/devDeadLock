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
    private final int processRequestTime;
    private final int processUsageTime;
    private int[] recursos;
    private Recursos requestedResouce;
    private int currentRequest = -1;
    private final SystemOperacional sistemaOperacional;
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
        this.telagrafo.Log.appendText("Processo "+this.Pid+" criado\n");
        desenharprocesso(this.Pid, true);
        
        
    } 
    
    @Override
    public void run() {
        //auxiliar de tempo
        int aux = 0;
        //variavel auxiliar para controle de bloqueios
        boolean foiBloqueado = false;
        //varaivel para finalizar processo/recurso
        int finishedResource;
        
        //variavel keepalive serve para manter o processo vivo
        while(keepAlive){
            
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Processos.class.getName()).log(Level.SEVERE, null, ex);
            }
            aux++;
            
            if(aux % processRequestTime==0){
              //quando o tempo em segundos de solicitação passar ele tem que solicitar um recurso ainda nao usado por ele próprio  
                try {
                    mutex.acquire();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Processos.class.getName()).log(Level.SEVERE, null, ex);
                }
               
                                    //sorteia o recurso aleatoriamente 
                                    currentRequest  =sistemaOperacional.randomRecurse(this.Pid);
                                  
                   if(currentRequest >= 0) {
                               //fila de recursos recebe o recursso atual
                            requestedResouce = sistemaOperacional.getResourceById(currentRequest + 1);
                            telagrafo.Log.appendText("P"+this.Pid+" solicitou "+requestedResouce.getName()+"\n");
                            //se não houver processos disponiveis bloqueia o processo
                            //TODO desenhar solicitação
                           if(requestedResouce.getRecursosDisp() == 0)
					{
                                                desenharstatus(this.Pid, false);
						telagrafo.Log.appendText("P"+this.Pid+" bloqueiou com  "+requestedResouce.getName()+"\n");	
                                                 foiBloqueado = true;
                                        }

                    mutex.release();
                    boolean bloqueado;
                    do{         
                        //recurso ocupado, da um down no semafaro dele
                        try {
                                    requestedResouce.downSemafaroRec();
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Processos.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                              
						if(foiBloqueado)
						{
							telagrafo.Log.appendText("P"+this.Pid+" desbloqueiou com  "+requestedResouce.getName()+"\n");
							foiBloqueado = false;
		
						}
                                try {
                                    mutex.acquire();
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Processos.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                //se ainda estiver vivo ele libera o recurso que estava usando
                                bloqueado = this.keepAlive && this.requestedResouce.deadProcesses > 0;
						if(bloqueado) 
						{
							this.requestedResouce.liberarRecurso();
						}
						mutex.release();
                                                
					} while(bloqueado);
                    
                    
						if(this.keepAlive) 
					{
                                                try {
                                                    mutex.acquire();
                                                } catch (InterruptedException ex) {
                                                    Logger.getLogger(Processos.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                             //decrementa os recursos daquele tipo disponiveis que é só um
                                                requestedResouce.decrementInstances();

						//bota ele no array de recursos
						this.recursos[currentRequest]++;
						resourcesHeld.add(requestedResouce);
                                                //bota na fila de tempos // time line
						resourcesTimes.add(processUsageTime+1);

						//o atual processo possui um recursos ou ja foi usado
						currentRequest = -1;

						//process runs for a certain amount of time
						telagrafo.Log.appendText( "P"+this.Pid+" roda com "+requestedResouce.getName()+"\n");
                                                desenharstatus(this.Pid, true);
                                                mutex.release();

					} else {
                                            mutex.release();
                                        }
                                   }
			}
            
                            //se ainda estiver vivo 
                        if(this.keepAlive) {
                            //decrementa o tempo de uso enquanto esta vivo
				decrementResourcesTimes(resourcesTimes);
				//se zerar seta a variavel auxliar  liberando entao o recurso
				finishedResource = resourcesTimesIsZero(resourcesTimes);
				if(finishedResource!=-1) 
				{
					liberarRecurso(finishedResource);
				}
			}
		}
         telagrafo.Log.appendText("O processo "+(Pid+1)+" morreu\n");

		
        try {
            finalizaProcesso();
        } catch (InterruptedException ex) {
            Logger.getLogger(Processos.class.getName()).log(Level.SEVERE, null, ex);
        }
            }
                  
    private void finalizaProcesso() throws InterruptedException {
	
		mutex.acquire();
		
                    //processo morto terminou
		if(this.currentRequest >= 0) {
			this.requestedResouce.deadProcesses--;
		}
		
                //liberando os recursos
             		for(Recursos resource : this.resourcesHeld) {
			telagrafo.Log.appendText( "P" + this.Pid + " liberou " + resource.getName()+"\n");
                         desenharstatus(this.Pid, false);
			resource.incrementInstances();
			resource.liberarRecurso();
		}
		
		telagrafo.Log.appendText("P" + this.Pid + " finalizou\n");
              
		
	mutex.release();
		
	}

    
    
    public void liberarRecurso(int resourceId)
	{
		
		telagrafo.Log.appendText( "P"+this.Pid + " liberou " + resourcesHeld.get(resourceId).getName()+"\n");

		//Remove da timline e dos recursos em uso, e incrementa nos recurssos disponiveis
		resourcesTimes.remove(resourceId);
		this.recursos[resourcesHeld.get(resourceId).getId()-1]--;
		resourcesHeld.get(resourceId).incrementInstances();
		resourcesHeld.get(resourceId).liberarRecurso();
		resourcesHeld.remove(resourceId);
		
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
        

      public int[] getRecursos() {
        return recursos;
       
        }

      
      
    

    
    

}
