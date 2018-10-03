/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import Dados.ResultadoBusca;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author junio
 */
public class RMIExecuta extends Thread {
    private HashMap<String,ResultadoBusca> retorno;
    private int id;
    private int categoria; 
    private ArrayList<String> arrMarcas;

    public RMIExecuta(int id,ArrayList<String> arrMarcas, int categoria) {
        this.retorno = new HashMap<>();
        this.id = id;
        this.arrMarcas = arrMarcas;
        this.categoria = categoria;
    }
    
    public void run(){
        this.processaServidor();
    }

    public HashMap<String, ResultadoBusca> getRetorno() {
        return retorno;
    }

    public void setRetorno(HashMap<String, ResultadoBusca> retorno) {
        this.retorno = retorno;
    }
    
    
    
    public void processaServidor(){
        try { 
            Comunicacao comunica = (Comunicacao)Naming.lookup("/" + "localhost" + "/ServidorLeitura"+id);
            
            System.out.println(id + "- antes");
            HashMap<String,ResultadoBusca> retorno  = comunica.efetuaLeitura(this.arrMarcas,this.categoria);
            System.out.println(id + " - depois");
            
            this.setRetorno(retorno);
        } catch (Exception ex) {
            Logger.getLogger(RMIExecuta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
