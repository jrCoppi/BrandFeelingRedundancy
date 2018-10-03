/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

/**
 *
 * interface de comunicacao RMI
 */
import Dados.ResultadoBusca;
import java.rmi.*;
import java.util.ArrayList;
import java.util.HashMap;

public interface Comunicacao extends Remote {
   public HashMap<String,ResultadoBusca> efetuaLeitura(ArrayList<String> marcas, Integer categoria) throws RemoteException;
}

