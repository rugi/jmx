/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * Tomado de https://docs.oracle.com/cd/E13222_01/wls/docs90/jmx/editWLS.html
 *
 * @author     Oracle Co.
 */
public class PrintServerState {

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
    }

    /* 
   * Print an array of ServerRuntimeMBeans. 
   * This MBean is the root of the runtime MBean hierarchy, and
   * each server in the domain hosts its own instance.
     */
    public static ObjectName[] getServerRuntimes() throws Exception {        
        return (ObjectName[]) connection.getAttribute(service, "ServerRuntimes");
    }

    /* 
   * Iterate through ServerRuntimeMBeans and get the name and state
     */
    public void printNameAndState() throws Exception {
        ObjectName[] serverRT = getServerRuntimes();
        System.out.println("got server runtimes");
        int length = (int) serverRT.length;
        for (int i = 0; i < length; i++) {
            String name = (String) connection.getAttribute(serverRT[i], "Name");
            String state = (String) connection.getAttribute(serverRT[i], "State");
            System.out.println("Server name: " + name + ".   Server state: " + state);


         }//for
    }//method

    public static void main(String[] args) throws Exception {
        String hostname = "localhost";
        String portString = "7001";
        String username = "weblogic";
        String password = "welcome1";
        PrintServerState s = new PrintServerState();
        initConnection(hostname, portString, username, password);
        s.printNameAndState();
        connector.close();
    }
}
