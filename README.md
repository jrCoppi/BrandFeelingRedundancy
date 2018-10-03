# BrandFeelingRedundancy

Representation of the redundancy system on Brand Feeling's old version, this part of the system controls 3 separted servers, via RMI, testing for failures on the process to make sure the process is done correctly.

The system used to recieve the call to start the process from a SOAP application.

Some parts were changed to maintain the system's privacy.

Main parts:

 - Webservice/SOAP : webservice Package or WSProcessaInterface.java
 - Redundancy System/RMI : Under thr RMI package, method "leitura" and "efetuaLeitura" from "RMIProcessa" creates each of the rmi servers
 - Database : The package Banco was omitted because of privacy
