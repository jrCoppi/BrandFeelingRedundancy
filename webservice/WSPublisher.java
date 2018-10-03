/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import javax.xml.ws.Endpoint;

/**
 * Classe que publica o Ws
 */
public class WSPublisher {
     
    public static void main(String[] args)
    {
        Endpoint.publish("http://127.0.0.1:9876/webservice", new WSProcessaImpl());
        System.out.println("SErvidor no Ar!!!");
    }
}
