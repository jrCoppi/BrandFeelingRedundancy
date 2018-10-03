package webservice;

import javax.jws.WebService;
import rmi.RMIProcessa;

//Classe responsavel por ser a intermediaria entre o WebService para o RMI
@WebService(endpointInterface = "webservice.WSProcessaInterface")
public class WSProcessaImpl implements WSProcessaInterface {
    private RMIProcessa rmi;

    public WSProcessaImpl() {
        this.rmi = RMIProcessa.getInstance();
    }

    //Efetua leitura a partir da chamada webservice usando RMI
    @Override
    public Integer leitura(String marcas, String categoria) {
        return this.rmi.leitura(marcas, categoria);
    }
}
