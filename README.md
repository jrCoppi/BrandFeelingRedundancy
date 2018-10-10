# BrandFeelingRedundancy

Representation of the redundancy system on Brand Feeling's old version, this part of the system controls 3 separted servers, via RMI, testing for failures on the process to make sure the process is done correctly.

The system used to recieve the call to start the process from a SOAP application.

Some parts were changed to maintain the system's privacy, because of that this aplication doest NOT run.

Main parts:

 - Webservice/SOAP : webservice Package or WSProcessaInterface.java
 - Redundancy System/RMI : Under thr RMI package, method "leitura" and "efetuaLeitura" from "RMIProcessa" creates each of the rmi servers
 - Database : The package Banco was omitted because of privacy

This part of the system recieves the call from the user input on the front end (https://github.com/jrCoppi/BrandFeelingFrontEnd) and then prepares and execute the system's analysys on each of the backend servers (https://github.com/jrCoppi/BrandFeelingBackEnd)
