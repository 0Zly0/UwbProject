package DataInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.net.Socket;

/**
 *  messageHeader:4个连续的0xFF表示消息开始
 *  packetLength:消息体长度
 *  requestCommand:命令码
 *  sequenceId:消息流水号
 *  versionId:协议版本
 *  anchorId:基站ID
 *  tagId:标签ID
 *  distance:标签与基站间的距离
 *  tagStatus:标签的状态
 *  batchSn:测距序号
 *  XorByte:该字节前所有字节的异或校验
 */
@Data
@AllArgsConstructor
@ToString
public class UwbReceiveInfo {
    private Integer[] messageHeader = new Integer[4];
    private Integer[] packetLength = new Integer[4];
    private Integer[] requestCommand = new Integer[4];
    private Integer[] sequenceId = new Integer[4];
    private Integer[] versionId = new Integer[4];
    private Integer[] anchorId = new Integer[4];
    private Integer[] tagId = new Integer[4];
    private Integer[] distance = new Integer[4];
    private Integer tagStatus;
    private Integer batchSn;
    private Integer xorInteger;


}
