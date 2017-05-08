# README
C�digo utilizado en el art�culo:

JMX. La gu�a perdida. Parte 3. JMX, Weblogic y Multitenant.

Por Isaac Ruiz

Publicado en Marzo 2017

URL:
http://www.oracle.com/technetwork/es/articles/java/jmx-weblogic-multitenant-p3-3703521-esa.html

* Substituye wljmxclient.jar.txt;wlcient.jar.txt por los jars de tu instalaci�n de WLS 12c

Para compilar:
			%>javac -cp "wljmxclient.jar;wlcient.jar;.;" ManageDomainPartition.java

Para ejecutar:
			%>java -cp "wljmxclient.jar;wlcient.jar;.;" ManageDomainPartition

Conocer el estado de nuestra partici�n:
			%>java -cp "wljmxclient.jar;wlcient.jar;.;" ManageDomainPartition state


Suspender el funcionamiento de la partici�n:
			%>java -cp "wljmxclient.jar;wlcient.jar;.;" ManageDomainPartition suspend


Reanudar el funcionamiento de la partici�n:
			%>java -cp "wljmxclient.jar;wlcient.jar;.;" ManageDomainPartition resume