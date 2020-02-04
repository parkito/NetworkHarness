package ru.siksmfp.network.play.temp.nio;

public class StopNioConnector implements StopNioConnectorMBean {

    @Override
    public void doStop() {
        AppState.isWorking = false;
    }
}