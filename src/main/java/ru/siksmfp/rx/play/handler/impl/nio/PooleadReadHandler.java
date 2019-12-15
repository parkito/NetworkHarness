package ru.siksmfp.rx.play.handler.impl.nio;

import ru.siksmfp.rx.play.handler.api.Handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import static java.nio.channels.SelectionKey.OP_WRITE;
import static ru.siksmfp.rx.play.utils.TransmogrifyUtils.transmogrify;

public class PooleadReadHandler implements Handler<SelectionKey> {

    private Map<SocketChannel, Queue<ByteBuffer>> pendingData;
    private ExecutorService executorService;
    private Queue<Runnable> selectorAction;

    public PooleadReadHandler(Map<SocketChannel, Queue<ByteBuffer>> pendingData,
                              ExecutorService executorService,
                              Queue<Runnable> selectorActions) {
        this.pendingData = pendingData;
        this.executorService = executorService;
        this.selectorAction = selectorActions;
    }

    @Override
    public void handle(SelectionKey selectionKey) throws IOException {
        SocketChannel sc = (SocketChannel) selectionKey.channel();
        ByteBuffer bb = ByteBuffer.allocateDirect(80);
        int read = sc.read(bb);

        if (read == -1) {
            pendingData.remove(sc);
            sc.close();
            System.out.println("Disconnected from in read" + sc);
            return;
        }

        if (read > 0) {
            executorService.submit(() -> {
                        transmogrify(bb);
                        pendingData.get(sc).add(bb);
                        selectorAction.add(() -> selectionKey.interestOps(OP_WRITE));
                        selectionKey.selector().wakeup();
                    }
            );
        }
    }
}
