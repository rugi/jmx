import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Set;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

/**
 *
 * Este ejemplo esta basado en el mostrado en:
 * http://middlewaremagic.com/weblogic/?p=185 de Jay SenSharma
 *
 * @author RuGI (S&P Solutions S.A de C.V)
 */
public class WLS_ALL_JMX_URLS {

    private MBeanServerConnection connection;
    private JMXConnector connector;

    public void initConnection(String hostname, String portString,
            String username, String password) throws IOException,
            MalformedURLException {
        String protocol = "t3";
        Integer portInteger = Integer.valueOf(portString);
        int port = portInteger.intValue();
        String jndiroot = "/jndi/";
        String mserver = "weblogic.management.mbeanservers.domainruntime";
        String urlPath =jndiroot + mserver;
        JMXServiceURL serviceURL = new JMXServiceURL(protocol, hostname,
                port,urlPath );
        Hashtable environment = new Hashtable();
        environment.put(Context.SECURITY_PRINCIPAL, username);
        environment.put(Context.SECURITY_CREDENTIALS, password);
        environment.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,
                "weblogic.management.remote");
        environment.put("jmx.remote.x.request.waiting.timeout", new Long(10000));
        connector = JMXConnectorFactory.connect(serviceURL, environment);
        connection = connector.getMBeanServerConnection();
    }

    public void showAllMBeans() throws Exception {
        if (connection != null) {
            Set<ObjectName> mbeans = connection.queryNames(null, null);
            for (ObjectName mbeanName : mbeans) {
                System.out.println(mbeanName);
            }
        } else {
            System.out.println("Aun no hay conexion.");
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("---------------------------");
        String hostname = "localhost";
        String portString = "7001";
        String username = "weblogic";
        String password = "welcome1";
        WLS_ALL_JMX_URLS all = new WLS_ALL_JMX_URLS();
        all.initConnection(hostname, portString, username, password);
        all.showAllMBeans();
    }
}//class
