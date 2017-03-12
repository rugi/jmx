



Para compilar:
			%> javac -cp "wljmxclient.jar;wlcient.jar;.;" ManageDomainPartition.java

Para ejecutar:
			%>java -cp "wljmxclient.jar;wlcient.jar;.;" ManageDomainPartition

Conocer el estado de nuestra partición:
			%>java -cp "wljmxclient.jar;wlcient.jar;.;" ManageDomainPartition state


Suspender el funcionamiento de la partición:
			%>java -cp "wljmxclient.jar;wlcient.jar;.;" ManageDomainPartition suspend


Reanudar el funcionamiento de la partición:
			%>java -cp "wljmxclient.jar;wlcient.jar;.;" ManageDomainPartition resume