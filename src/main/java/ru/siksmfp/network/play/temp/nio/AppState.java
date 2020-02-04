package ru.siksmfp.network.play.temp.nio;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class AppState {
    private static MBeanServer mbs;

    public volatile static boolean isWorking;

    static {
        isWorking = true;

        mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            mbs.registerMBean(
                    new StopNioConnector(),
                    new ObjectName("ru.siksmfp.rx.play.connector.nio.jmx:type=StopNioConnectorImpl")
            );
            System.out.println("Construct jmx server");
        } catch (MalformedObjectNameException |
                NotCompliantMBeanException |
                InstanceAlreadyExistsException |
                MBeanRegistrationException e) {
            e.printStackTrace();
        }
    }
}


