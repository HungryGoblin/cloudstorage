package nettyserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import transport.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class EnvelopeHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(EnvelopeHandler.class);
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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Envelope envelope = (Envelope) msg;
//        if (msg.getSenderName().equals(UserConstants.DEFAULT_SENDER_NAME)) {
//            msg.setSenderName(name);
//        }
//
//        for (ChannelHandlerContext client : clients) {
//            client.writeAndFlush(msg);
//        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        clients.remove(ctx);
        LOG.debug("client disconnected ");
    }

}
