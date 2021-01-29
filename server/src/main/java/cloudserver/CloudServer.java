package cloudserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloudServer {

    private static final Logger LOG = LoggerFactory.getLogger(CloudServer.class);

    public CloudServer() {
        BasicConfigurator.configure();
        EventLoopGroup auth = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(auth, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new DataHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(ServerSetting.PORT).sync();
            LOG.info(String.format("server started on PORT = %d", ServerSetting.PORT));
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            LOG.error("**ERROR ", e);
        }
    }

    public static void main(String[] args) {
        try {
            CloudServer ns = new CloudServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
