package rmi;

import Banco.Banco;
import Dados.ResultadoBusca;
import Dados.Termo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;


public class RMIProcessa {
    private static RMIProcessa instance;
    private Banco banco;
    private ArrayList<String> marcas;

    public static RMIProcessa getInstance(){
        if (instance == null)
            instance = new RMIProcessa();
        return instance;
    }

    public RMIProcessa() {
        this.banco = new Banco();
    }
    
    //Efetua leitura a partir do produto vindo do web-service
    public Integer leitura(String marca, String categoria) {
        String[] arrMarcas;
        this.marcas = new ArrayList<>();
        HashMap<String,ResultadoBusca> retornoFinal;
            
        //separa e colocar as marcas num array
        arrMarcas = marca.split(";");
        for (String marcaArray : arrMarcas) {
            this.marcas.add(marcaArray);
        }
        
        //registra pesquisa na base, guarda retorno com os IDs
        HashMap<String,Integer> hashIds = this.registraPesquisa(arrMarcas,categoria);
        //this.manipula.salvaDados("Antes leitura");
        
        retornoFinal = this.efetuaLeitura(this.marcas,Integer.valueOf(categoria));
        
        //com o retorno definido salvamos em base
        this.registraResultados(hashIds, retornoFinal);

        return hashIds.get("pesquisa");
    }
    
    private HashMap<String,ResultadoBusca> efetuaLeitura(ArrayList<String> marcas, Integer categoria){
        try {     
            RMIExecuta executa1,executa2,executa3;
            HashMap<String,ResultadoBusca> retornoFinal;
            
            //Dispara leitura
            executa1 = new RMIExecuta(1, marcas, categoria);
            executa1.start();
            
            executa2 = new RMIExecuta(2, marcas, categoria);
            executa2.start();
            
            executa3 = new RMIExecuta(3, marcas, categoria);
            executa3.start();
            
            executa1.join();
            executa2.join();
            executa3.join();
         
            //Verifica a igualdade entre os resultados do servidores
            retornoFinal = this.efetuaVotacao(executa1.getRetorno(),executa2.getRetorno(),executa3.getRetorno());
            return retornoFinal;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Registra o resultado das pesquisas em base
     * @param hashIds
     * @param retorno 
     */
    private void registraResultados(HashMap<String,Integer> hashIds,HashMap<String,ResultadoBusca> retorno){
        //pegar o set, percorrer e salvar
        Set<String> chaves = retorno.keySet();  
        Integer idPesquisa = hashIds.get("pesquisa");
        Integer idResultado;
        Integer idMarca;
        String dados;
        String termosInsert = "";
        String resultadoInsert = "";
        String separador = "";
        Integer[] nrTermosTipo;
        
        for (String chave : chaves) {
            ResultadoBusca resultado = retorno.get(chave);
            idMarca = hashIds.get(chave);
            
            //insere resultado
            nrTermosTipo = resultado.getNrResultadosMarca();
            dados = idMarca + ";" + idPesquisa + ";" + String.valueOf(nrTermosTipo[0]) + ";" + String.valueOf(nrTermosTipo[1]);
            idResultado = this.banco.insertResultado(dados);
            
            System.out.println("Positivo:" + String.valueOf(nrTermosTipo[0]) +"| Negativo" + String.valueOf(nrTermosTipo[1]));
            
            //insere termos
            for (Termo termo : resultado.getHashTermos()) {
                //novo termo
                termosInsert += separador + "('" + termo.getTermo() + "') ";
                
                resultadoInsert += 
                    separador + "("+idResultado+",(SELECT id_termo FROM termo WHERE ds_termo = CONVERT(_latin1'" + termo.getTermo() + "'USING utf8)),"
                        +termo.getOcorrenciaPorMarca(chave)+","
                        +String.valueOf(termo.getNrResultadosMarca().get(chave)[0])+","
                        +String.valueOf(termo.getNrResultadosMarca().get(chave)[1])+")";
                separador = ",";
            } 
            
            //System.out.println(resultadoInsert);
            
            
           this.banco.insereVariosTermos(termosInsert);
           this.banco.insertVariosResultadoTermo(resultadoInsert);
        }
    }
    
    /**
     * Registra a pesquisa em base
     * @param arrMarcas
     * @return HashMap
     */
    private HashMap<String,Integer> registraPesquisa(String[] arrMarcas, String categoria){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Integer idMarca;
        HashMap<String,Integer> hashResultado = new HashMap<>();
        
        //nova pesquisa
        Integer pesquisa = banco.insertPesquisa(dateFormat.format(date),categoria);
        hashResultado.put("pesquisa", pesquisa);
        
        //para cada marca insere no banco
        for (String marca : arrMarcas) {
            idMarca = banco.novaMarca(marca);
            banco.insertPesquisaMarca(pesquisa, idMarca);
            hashResultado.put(marca, idMarca);
        }

        return hashResultado;
    }
    
    
    
    /**
     * Efetua a votação dos servidores
     * @param listaResultado1
     * @param listaResultado2
     * @param marcas
     */
    private HashMap<String,ResultadoBusca> efetuaVotacao(
            HashMap<String,ResultadoBusca> listaResultado1, 
            HashMap<String,ResultadoBusca> listaResultado2, 
            HashMap<String,ResultadoBusca> listaResultado3 
    ){
        //Primeiro verifica se os termos são identicos
        if(this.comparaResultados(listaResultado1,listaResultado2)){
            return listaResultado1;
        }
        
        if(this.comparaResultados(listaResultado2,listaResultado3)){
            return listaResultado2;
        }
        
        if(this.comparaResultados(listaResultado3,listaResultado1)){
            return listaResultado3;
        }

        //Ultima validação de erro
        if(!listaResultado3.isEmpty()){
            return listaResultado3;
        }
        
        if(!listaResultado2.isEmpty()){
            return listaResultado2;
        }
        
        return listaResultado1;
    }
    
    /**
     * Compara os resultados dos servidores para verificar se estão corretos
     * @param resultado1
     * @param resultado2
     * @param marcas
     * @return 
     */
    private boolean comparaResultados(
            HashMap<String,ResultadoBusca> listaResultado1, 
            HashMap<String,ResultadoBusca> listaResultado2
    ){
        ResultadoBusca resultado1;
        ResultadoBusca resultado2;

        try{
            for (String marca : this.marcas) {
                resultado1 = listaResultado1.get(marca);
                resultado2 = listaResultado2.get(marca);

                //Verifica se na mesma marca os valores batem
                if(
                    (resultado1.getNrTermosTipo()[0] != resultado2.getNrTermosTipo()[0]) ||
                    (resultado1.getNrTermosTipo()[1] != resultado2.getNrTermosTipo()[1]) ||
                    (resultado1.getNrTermos()!= resultado2.getNrTermos()) 
                ) {
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
           return false;
        }
    }
}
