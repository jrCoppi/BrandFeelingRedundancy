package Dados;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Salva o resutlado de uma busca
 * @author junio
 */
public class ResultadoBusca implements Serializable {
    private ArrayList<Termo> hashTermos;
    private int nrTermos;
    private Integer[] nrResultadosMarca;
    
    //Numero de termos distruibos entre positivo, negativo e neurto
    private Integer[] nrTermosTipo;

    public ResultadoBusca() {
        this.hashTermos = new ArrayList<>();
        this.nrTermos = 0;
        this.nrResultadosMarca = new Integer[2];
        this.nrTermosTipo = new Integer[2];
        this.nrTermosTipo[0] = 0;
        this.nrTermosTipo[1] = 0;
    }

    public ArrayList<Termo> getHashTermos() {
        return hashTermos;
    }

    public void setHashTermos(ArrayList<Termo> hashTermos) {
        this.hashTermos = hashTermos;
    }

    public int getNrTermos() {
        return nrTermos;
    }

    public void aumentaNrTermos(int termos) {
        this.nrTermos += termos;
    }

    public Integer[] getNrResultadosMarca() {
        return nrResultadosMarca;
    }

    public void setNrResultadosMarca(Integer[] nrResultadosMarca) {
        this.nrResultadosMarca = nrResultadosMarca;
    }

    public Integer[] getNrTermosTipo() {
        return nrTermosTipo;
    }

    public void setNrTermosTipo(Integer[] nrTermosTipo) {
        this.nrTermosTipo = nrTermosTipo;
    }
    
    public void incrementaTermoTipo(Integer tipo) {
        this.nrTermosTipo[tipo]++;
    }
}
