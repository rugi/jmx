import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

/**
 *
 * @author S&P Solutions S.A de C.V.
 */
public class ManageDomainPartition {

    private static MBeanServerConnection connection;
    private static JMXConnector connector;
    private static final ObjectName service;

    // Initializing the object name for DomainRuntimeServiceMBean
    // so it can be used throughout the class.
    static {
        try {
            service = new ObjectName("com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
        } catch (MalformedObjectNameException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    /*
    * Initialize connection to the Domain Runtime MBean Server
     */
    public static void initConnection(String hostname, String portString,
            String username, String password) throws IOException,
            MalformedURLException {
        //http://docs.oracle.com/javase//1.5.0/docs/api/javax/management/remote/JMXServiceURL.html
        String protocol = "t3";
        Integer portInteger = Integer.valueOf(portString);
        int port = portInteger.intValue();
        String jndiroot = "/jndi/";
        String mserver = "weblogic.management.mbeanservers.domainruntime";
        JMXServiceURL serviceURL = new JMXServiceURL(protocol, hostname,
                port, jndiroot + mserver);
        Hashtable environment = new Hashtable();
        environment.put(Context.SECURITY_PRINCIPAL, username);
        environment.put(Context.SECURITY_CREDENTIALS, password);
        environment.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,
                "weblogic.management.remote");
        environment.put("jmx.remote.x.request.waiting.timeout", new Long(10000));
        connector = JMXConnectorFactory.connect(serviceURL, environment);
        connection = connector.getMBeanServerConnection();
        System.out.println("<Conexion Establecida>");
    }

    public static ObjectName[] getPartitionsRuntimes() throws Exception {
        //https://docs.oracle.com/middleware/1213/wls/WLAPI/weblogic/management/mbeanservers/domainruntime/DomainRuntimeServiceMBean.html
        Object opParams[] = {"dp1"
        };

        String opSig[] = {String.class.getName(),};
        return (ObjectName[]) connection.invoke(service, "findPartitionRuntimes", opParams, opSig);
    }

    /* 
   * Iterate through ServerRuntimeMBeans and get the name and state
     */
    public void printDomainPartitionState() throws Exception {
        //https://docs.oracle.com/middleware/1221/wls/WLAPI/weblogic/management/runtime/PartitionRuntimeMBean.html
        ObjectName[] partitionT = getPartitionsRuntimes();
        System.out.println("------------------- Partition state ----------------------------");
        int length = (int) partitionT.length;
        for (int i = 0; i < length; i++) {
            String name = (String) connection.getAttribute(partitionT[i], "Name");
            String state = (String) connection.getAttribute(partitionT[i], "State");
            String serverName = (String) connection.getAttribute(partitionT[i], "ServerName");
            System.out.println("Partition name: " + name + ".   Partition state: " + state + ". Server Name:" + serverName);
        }//for   
        System.out.println("-------------------     #########     ----------------------------");
    }//method

    public void suspendDomainPartition() throws Exception {
        //https://docs.oracle.com/middleware/1221/wls/WLAPI/weblogic/management/runtime/PartitionRuntimeMBean.html
        ObjectName[] partitionT = getPartitionsRuntimes();
        int length = (int) partitionT.length;

        for (int i = 0; i < length; i++) {
            String name = (String) connection.getAttribute(partitionT[i], "Name");
            String serverName = (String) connection.getAttribute(partitionT[i], "ServerName");
            Object responseForceShutdown = connection.invoke(partitionT[i], "suspend", null, null);
            System.out.println("Se envió mensaje para suspender la particion:" + name + " en el server:" + serverName);
        }//for         
    }//method    

    public void resumeDomainPartition() throws Exception {
        //https://docs.oracle.com/middleware/1221/wls/WLAPI/weblogic/management/runtime/PartitionRuntimeMBean.html
        ObjectName[] partitionT = getPartitionsRuntimes();
        int length = (int) partitionT.length;

        for (int i = 0; i < length; i++) {
            String name = (String) connection.getAttribute(partitionT[i], "Name");
            String serverName = (String) connection.getAttribute(partitionT[i], "ServerName");
            Object responseForceShutdown = connection.invoke(partitionT[i], "resume", null, null);
            System.out.println("Se envió mensaje para reiniciar la particion:" + name + " en el server:" + serverName);
        }//for         
    }//method     

    public static void main(String[] args) throws Exception {
        System.out.println("numero de argumentos " + args.length);
        if (args.length == 1) {
            String operacion = args[0].trim().toLowerCase();
            System.out.println("Ejecutando <" + operacion + ">");
            String hostname = "localhost";
            String portString = "7001";
            String username = "weblogic";
            String password = "welcome1";
            ManageDomainPartition domainPartition = new ManageDomainPartition();
            initConnection(hostname, portString, username, password);
            if (operacion.equals("state")) {
                domainPartition.printDomainPartitionState();
            }
            if (operacion.equals("suspend")) {
                domainPartition.suspendDomainPartition();
            }
            if (operacion.equals("resume")) {
                domainPartition.resumeDomainPartition();
            }
            connector.close();
            System.out.println("<Conexion cerrada>");
        } else {
            System.out.println(" Use alguno de los siguientes comandos:");
            System.out.println("               state| suspend | resume ");
            System.out.println("Ejemplo:");
            System.out.println("              ManageDomainPartition state");
        }
    }//
}//
