package ru.siksmfp.rx.play.connector.nio;

public class StopNioConnector implements StopNioConnectorMBean {

    @Override
    public void doStop() {
        AppState.isWorking = false;
    }
}