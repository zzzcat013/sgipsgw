/**
 * 
 */
package org.duodo.sgip13s.encoder;

import org.duodo.netty3ext.global.GlobalVars;
import org.duodo.netty3ext.message.Message;
import org.duodo.netty3ext.packet.PacketType;
import org.duodo.sgip13s.message.SubmitRequestMessage;
import org.duodo.sgip13s.packet.SgipPacketType;
import org.duodo.sgip13s.packet.SubmitRequest;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.google.common.primitives.Bytes;

/**
 * @author huzorro(huzorro@gmail.com)
 * 
 */
public class SubmitRequestEncoder extends OneToOneEncoder {
	private PacketType packetType;

	public SubmitRequestEncoder() {
		this(SgipPacketType.SUBMITREQUEST);
	}

	public SubmitRequestEncoder(PacketType packetType) {
		this.packetType = packetType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.handler.codec.oneone.OneToOneEncoder#encode(org.jboss
	 * .netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel,
	 * java.lang.Object)
	 */
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if (!(msg instanceof Message))
			return msg;
		Message message = (Message) msg;
		long commandId = ((Long) message.getHeader().getCommandId())
				.longValue();
		if (commandId != packetType.getCommandId())
			return msg;

		SubmitRequestMessage requestMessage = (SubmitRequestMessage) message;

		ChannelBuffer bodyBuffer = ChannelBuffers.dynamicBuffer();

		bodyBuffer.writeBytes(Bytes.ensureCapacity(requestMessage.getSpnumber()
				.getBytes(GlobalVars.defaultTransportCharset),
				SubmitRequest.SPNUMBER.getLength(), 0));
		bodyBuffer.writeBytes(Bytes.ensureCapacity(
				requestMessage.getChargenumber().getBytes(
						GlobalVars.defaultTransportCharset),
				SubmitRequest.CHARGENUMBER.getLength(), 0));
		bodyBuffer.writeByte(requestMessage.getUsercount());
		bodyBuffer.writeBytes(Bytes.ensureCapacity(requestMessage
				.getUsernumber().getBytes(GlobalVars.defaultTransportCharset),
				SubmitRequest.USERNUMBER.getLength(), 0));
		bodyBuffer.writeBytes(Bytes.ensureCapacity(requestMessage.getCorpid()
				.getBytes(GlobalVars.defaultTransportCharset),
				SubmitRequest.CORPID.getLength(), 0));
		bodyBuffer.writeBytes(Bytes.ensureCapacity(requestMessage
				.getServicetype().getBytes(GlobalVars.defaultTransportCharset),
				SubmitRequest.SERVICETYPE.getLength(), 0));
		bodyBuffer.writeByte(requestMessage.getFeetype());
		bodyBuffer.writeBytes(Bytes.ensureCapacity(requestMessage.getFeevalue()
				.getBytes(GlobalVars.defaultTransportCharset),
				SubmitRequest.FEEVALUE.getLength(), 0));
		bodyBuffer.writeBytes(Bytes.ensureCapacity(requestMessage
				.getGivenvalue().getBytes(GlobalVars.defaultTransportCharset),
				SubmitRequest.GIVENVALUE.getLength(), 0));
		bodyBuffer.writeByte(requestMessage.getAgentflag());
		bodyBuffer.writeByte(requestMessage.getMorelatetomtflag());
		bodyBuffer.writeByte(requestMessage.getPriority());
		bodyBuffer.writeBytes(Bytes.ensureCapacity(requestMessage
				.getExpiretime().getBytes(GlobalVars.defaultTransportCharset),
				SubmitRequest.EXPIRETIME.getLength(), 0));
		bodyBuffer.writeBytes(Bytes.ensureCapacity(
				requestMessage.getScheduletime().getBytes(
						GlobalVars.defaultTransportCharset),
				SubmitRequest.SCHEDULETIME.getLength(), 0));
		bodyBuffer.writeByte(requestMessage.getReportflag());
		bodyBuffer.writeByte(requestMessage.getTppid());
		bodyBuffer.writeByte(requestMessage.getTpudhi());
		bodyBuffer.writeByte(requestMessage.getMessagecoding());
		bodyBuffer.writeByte(requestMessage.getMessagetype());
		bodyBuffer.writeInt((int) requestMessage.getMessagelength());
		bodyBuffer.writeBytes(requestMessage.getMsgContentBytes());
		bodyBuffer.writeBytes(Bytes.ensureCapacity(requestMessage.getReserve()
				.getBytes(GlobalVars.defaultTransportCharset),
				SubmitRequest.RESERVE.getLength(), 0));
		
		requestMessage.setBodyBuffer(bodyBuffer.copy().array());
		
		ChannelBuffer msgBuffer = ChannelBuffers.dynamicBuffer();
		
		msgBuffer.writeBytes(requestMessage.getHeader().getHeadBuffer());
		msgBuffer.writeBytes(requestMessage.getBodyBuffer());
		
		return msgBuffer;
	}

}
