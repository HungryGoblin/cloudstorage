package cloudserver;

import filesystem.FileHelper;
import filesystem.FileList;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import transport.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DataHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(DataHandler.class);
    private static final ConcurrentLinkedDeque<ChannelHandlerContext> clients = new ConcurrentLinkedDeque<>();
    private static int cnt = 0;
    private static String name;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx);
        cnt++;
        super.channelActive(ctx);
        name = "user#" + cnt;
        LOG.debug(String.format("client connected: %s", name));
    }

    // removes synchronized files from the file list
    public FileList removeExistingFileEntity (FileList fileList) throws IOException {
        for (int i = fileList.getFileList().size() - 1; i >= 0; i--) {
            Path path = ServerSetting.getSyncPath().resolve(fileList.getPath(i));
            if (FileHelper.fileExists(path) && FileHelper.fileEntityEquals(fileList.get(i), path))
                fileList.remove(i);
        }
        return fileList;
    }

    public void createFile(FileMessage fileMessage) throws IOException {
        Path path = ServerSetting.getSyncPath().resolve(fileMessage.getPath());
        FileHelper.createDirectoryIfNotExists(path);
        fileMessage.saveFile(path);
        LOG.info(String.format("File %s created", path.toString()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Container container = (Container) msg;
        LOG.info("ENVELOPE RECEIVED: " + container);
        if (container.getUser().isEmpty())
            container = new Container(ServerSetting.NAME, "UNKNOWN USER");
        else {
            if (container.getMessageType() == MessageType.FILE_LIST) {
                FileList fileList = removeExistingFileEntity((FileList) container.getMessage());
                container = new Container(ServerSetting.NAME, fileList, MessageType.FILE_LIST);
            } else if (container.getMessageType() == MessageType.FILE) {
                FileMessage fileMessage = (FileMessage) container.getMessage();
                createFile(fileMessage);
            } else {
                container = new Container(ServerSetting.NAME, "DATA RECEIVED");
            }
        }
        for (ChannelHandlerContext client : clients) {
            client.writeAndFlush(container);
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        clients.remove(ctx);
        LOG.info("client disconnected ");
    }

}
