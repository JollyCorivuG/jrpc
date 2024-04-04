package bupt.edu.jhc.jrpc.protocol;

import bupt.edu.jhc.jrpc.domain.constants.RPCConstants;
import bupt.edu.jhc.jrpc.domain.req.RPCReq;
import bupt.edu.jhc.jrpc.protocol.enums.ProtocolMsgSerializerEnum;
import bupt.edu.jhc.jrpc.protocol.enums.ProtocolMsgStatusEnum;
import bupt.edu.jhc.jrpc.protocol.enums.ProtocolMsgTypeEnum;
import cn.hutool.core.util.IdUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @Description: 协议消息测试类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/4/4
 */
public class ProtocolMsgTest {
    @Test
    public void testEncodeAndDecode() throws IOException {
        // 构造消息
        var protocolMsg = new ProtocolMsg<>();
        var header = new ProtocolMsg.Header();
        header.setMagic(ProtocolConstants.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstants.PROTOCOL_VERSION);
        header.setSerializer(ProtocolMsgSerializerEnum.JDK.getKey().byteValue());
        header.setType(ProtocolMsgTypeEnum.REQUEST.getType().byteValue());
        header.setStatus(ProtocolMsgStatusEnum.OK.getCode().byteValue());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);
        var rpcRequest = new RPCReq();
        rpcRequest.setServiceName("myService");
        rpcRequest.setMethodName("myMethod");
        rpcRequest.setServiceVersion(RPCConstants.DEFAULT_SERVICE_VERSION);
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setParams(new Object[]{"aaa", "bbb"});
        protocolMsg.setHeader(header);
        protocolMsg.setBody(rpcRequest);

        var encodeBuffer = ProtocolMsgEncoder.encode(protocolMsg);
        var message = ProtocolMsgDecoder.decode(encodeBuffer);
        System.out.println(message);
        Assert.assertNotNull(message);
    }
}
