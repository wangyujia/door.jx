package JX.Framework.Service.Msg;

import JX.*;
import JX.Framework.*;
import java.nio.*;


/**
 * ��Ӧ��Ϣͷ
 */
public class MsgResponse
{
    public static byte HeaderType = 3;
    public static byte HeaderSize = 20;

    public byte     headType;                   // ͷ������(��'DCOP_MSG_HEAD_TYPE')
    public byte     headSize;                   // ͷ����С(��'DCOP_MSG_HEAD_SIZE')
    public short    valueLen;                   // ͷ�������ֵ�ĳ���
    public int      retCode;                    // ������(0Ϊ��ȷ,��0Ϊ������)
    public short    recordCount;                // ��¼����
    public short    recordIndex;                // ���μ�¼����
    public short    paraCount;                  // ��������
    public short    paraLen;                    // ��������

    public MsgPacket msg;                       // ָ�����Ϣ��
    public int      valuePosition;              // �Ự��ֵλ����Ϣ����������λ��


    public MsgResponse()
    {
        headType = HeaderType;
        headSize = HeaderSize;
        valueLen = 0;
        paraCount = 0;
        paraLen = 0;
        msg = null;
        valuePosition = 0;
    }


    /**
     * ����Ϣ���н�����Ӧ��Ϣͷ
     * @param MsgPacket msg ��Ϣ��
     * @return MsgResponse ��Ӧ��Ϣͷ
     */
    public MsgResponse parse(MsgPacket msg)
    {
        if (msg == null)
        {
            return this;
        }

        ByteBuffer buffer = msg.data;
        if (msg.data == null)
        {
            return this;
        }

        /// �������Ŀɶ����ȱ�����ڻ���֡��
        int position = buffer.position();
        int bufLen = buffer.limit() - position;
        if (bufLen < HeaderSize)
        {
            return this;
        }

        /// ͷ��У�������ȷ
        byte headType  = buffer.get();
        byte headSize  = buffer.get();
        if ((headType != HeaderType) ||
            (headSize != HeaderSize))
        {
            buffer.position(position);
            return this;
        }

        /// ��ȡ֡ͷ
        this.headType = headType;
        this.headSize = headSize;
        this.valueLen = buffer.getShort();
        this.retCode = buffer.getInt();
        this.recordCount = buffer.getShort();
        this.recordIndex = buffer.getShort();
        this.paraCount = buffer.getShort();
        this.paraLen = buffer.getShort();
        this.msg = msg;
        this.valuePosition = buffer.position();

        /// ƫ�ƹ������������
        buffer.position(position + headSize);
        return this;
    }


    /**
     * ����Ӧ��Ϣͷת��Ϊ������
     * @return ByteBuffer ������
     */
    public ByteBuffer pack()
    {
        /// ���仺��������ȡ��Ϣͷ
        ByteBuffer headBuf = ByteBuffer.allocate(headSize);
        headBuf.put(headType);
        headBuf.put(headSize);
        headBuf.putShort(valueLen);
        headBuf.putInt(retCode);
        headBuf.putShort(recordCount);
        headBuf.putShort(recordIndex);
        headBuf.putShort(paraCount);
        headBuf.putShort(paraLen);

        headBuf.flip();
        return headBuf;
    }

}


