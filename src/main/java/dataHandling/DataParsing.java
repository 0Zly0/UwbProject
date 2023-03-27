package dataHandling;

import DataInfo.UwbReceiveInfo;
import enums.StatusMachineEnum;
import tools.XorTool;

import java.net.Socket;

/**
 * @author 张璐洋1
 * UWB传输的数据解析
 */
public class DataParsing {
    private Integer state = StatusMachineEnum.HEAD_STATUS;
    private Integer[] messageHeader = new Integer[4];
    private Integer[] packetLength = new Integer[4];
    private Integer[] requestCommand = new Integer[4];
    private Integer[] sequenceId = new Integer[4];
    private Integer[] versionId = new Integer[4];
    private Integer[] anchorId = new Integer[4];
    private Integer[] tagId = new Integer[4];
    private Integer[] distance = new Integer[4];
    private Integer tagStatus ;
    private Integer batchSn ;
    private Integer xorByte;
    private Integer count = 0;



    public UwbReceiveInfo parsingData( Integer dataByte){
        UwbReceiveInfo uwbReceiveInfo = null;
        switch (this.state){
            case StatusMachineEnum.HEAD_STATUS:
                if (dataByte == 0xFF){
                    messageHeader[count] = dataByte;
                    count ++;
                    if(count == 4){
                        state = StatusMachineEnum.PACKET_LENGTH;
                        count = 0;
                    }
                }
                break;
            case StatusMachineEnum.PACKET_LENGTH:
                    packetLength[count] = dataByte;
                    count ++;
                    if(count == 4){
                        state = StatusMachineEnum.REQUEST_COMMAND;
                        count = 0;
                    }
                    break;
            case StatusMachineEnum.REQUEST_COMMAND:
                requestCommand[count] = dataByte;
                count ++;
                if(count == 4){
                    state = StatusMachineEnum.SEQUENCE_ID;
                    count = 0;
                }
                break;
            case StatusMachineEnum.SEQUENCE_ID:
                sequenceId[count] = dataByte;
                count ++;
                if(count == 4){
                    state = StatusMachineEnum.VERSION_ID;
                    count = 0;
                }
                break;
            case StatusMachineEnum.VERSION_ID:
                versionId[count] = dataByte;
                count ++;
                if(count == 4){
                    state = StatusMachineEnum.ANCHOR_ID;
                    count = 0;
                }
                break;
            case StatusMachineEnum.ANCHOR_ID:
                anchorId[count] = dataByte;
                count ++;
                if(count == 4){
                    state = StatusMachineEnum.TAG_ID;
                    count = 0;
                }
                break;
            case StatusMachineEnum.TAG_ID:
                tagId[count] = dataByte;
                count ++;
                if(count == 4){
                    state = StatusMachineEnum.DISTANCE;
                    count = 0;
                }
                break;
            case StatusMachineEnum.DISTANCE:
                distance[count] = dataByte;
                count ++;
                if(count == 4){
                    state = StatusMachineEnum.TAG_STATUS;
                    count = 0;
                }
                break;
            case StatusMachineEnum.TAG_STATUS:
                tagStatus = dataByte;
                state = StatusMachineEnum.BATCH_SN;
                break;
            case StatusMachineEnum.BATCH_SN:
                batchSn = dataByte;
                state = StatusMachineEnum.XOR_BYTE;
                break;
            case StatusMachineEnum.XOR_BYTE:
                xorByte = dataByte;
                uwbReceiveInfo = new UwbReceiveInfo(messageHeader, packetLength, requestCommand, sequenceId, versionId,
                        anchorId, tagId, distance, tagStatus, batchSn, xorByte);
                int xor = XorTool.jiSuan(uwbReceiveInfo);
//                System.out.println("xor = " + xor + "     xorByte = " + xorByte);
                if(xor != xorByte){
                    uwbReceiveInfo = null;
//                    System.out.println("数据校验失败！！");
                }else {
//                    System.out.println("数据校验正确！！！");

                }


                state = StatusMachineEnum.HEAD_STATUS;
                break;
            default:
                System.out.println("状态机bug！");

        }
        return uwbReceiveInfo;
    }
}
