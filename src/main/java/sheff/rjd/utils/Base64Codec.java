package sheff.rjd.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;



public class Base64Codec
{
	private static Logger	log	= Logger.getLogger(Base64Codec.class);
    static final int lineLength = 72;
    private static final char encodedChar[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
        'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 
        'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
        'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', 
        '8', '9', '+', '/'
    };
    private static byte decoded6BitValue[];

    public Base64Codec()
    {
    }

    public static byte[] base64Encode(byte data[])
    {
        return base64Encode(data, true);
    }

    public static byte[] base64Encode(byte decodedData[], boolean doAddLineFeeds)
    {
        if(decodedData == null)
        {
            return null;
        }
        int nTriples = decodedData.length / 3;
        int nTrailingBytes = decodedData.length % 3;
        int nOutputBytes = 4 * (nTriples + (nTrailingBytes != 0 ? 1 : 0));
        if(doAddLineFeeds)
        {
            int nOutputLines = ((nOutputBytes + 72) - 1) / 72;
            if(nOutputLines > 0)
            {
                nOutputBytes += nOutputLines - 1;
            }
        }
        byte encodedData[] = new byte[nOutputBytes];
        int encodedIndex = 0;
        int nBytesOnLine = 0;
        for(int decodedIndex = 0; decodedIndex < nTriples * 3; decodedIndex += 3)
        {
            encodeTriple(decodedData, decodedIndex, 3, encodedData, encodedIndex);
            encodedIndex += 4;
            nBytesOnLine += 4;
            if(doAddLineFeeds && nBytesOnLine == 72 && decodedIndex < decodedData.length - 3)
            {
                encodedData[encodedIndex++] = 10;
                nBytesOnLine = 0;
            }
        }

        if(nTrailingBytes != 0)
        {
            encodeTriple(decodedData, nTriples * 3, nTrailingBytes, encodedData, encodedIndex);
        }
        return encodedData;
    }

    public static byte[] base64Decode(byte encodedData[])
    {
        return base64Decode(encodedData, encodedData.length);
    }

    public static byte[] base64Decode(byte encodedBytes[], int nEncodedBytes)
    {
        if(encodedBytes == null)
        {
            return null;
        }
        if(nEncodedBytes > encodedBytes.length)
        {
            nEncodedBytes = encodedBytes.length;
        }
        byte decodedBuffer[] = new byte[nEncodedBytes];
        int nDecodedBytes = 0;
        int accumulator = 0;
        int nBytesOfQuadSeen = 0;
        for(int i = 0; i < nEncodedBytes; i++)
        {
            byte aByte = decoded6BitValue[encodedBytes[i]];
            if(aByte < 0)
            {
                continue;
            }
            accumulator <<= 6;
            accumulator |= aByte;
            if(++nBytesOfQuadSeen == 4)
            {
                decodedBuffer[nDecodedBytes++] = (byte)(accumulator >>> 16);
                decodedBuffer[nDecodedBytes++] = (byte)((accumulator & 0xff00) >>> 8);
                decodedBuffer[nDecodedBytes++] = (byte)(accumulator & 0xff);
                accumulator = 0;
                nBytesOfQuadSeen = 0;
            }
        }

        switch(nBytesOfQuadSeen)
        {
        case 0: // '\0'
        default:
            break;

        case 1: // '\001'
            decodedBuffer[nDecodedBytes++] = (byte)(accumulator << 2);
            break;

        case 2: // '\002'
            decodedBuffer[nDecodedBytes++] = (byte)(accumulator >>> 4);
            byte b = (byte)((accumulator & 0xf) << 4);
            if(b != 0)
            {
                decodedBuffer[nDecodedBytes++] = b;
            }
            break;

        case 3: // '\003'
            decodedBuffer[nDecodedBytes++] = (byte)(accumulator >>> 10);
            decodedBuffer[nDecodedBytes++] = (byte)((accumulator & 0x3fc) >>> 2);
            byte c = (byte)((accumulator & 3) << 6);
            if(c != 0)
            {
                decodedBuffer[nDecodedBytes++] = c;
            }
            break;
        }
        byte decodedData[] = new byte[nDecodedBytes];
        System.arraycopy(decodedBuffer, 0, decodedData, 0, nDecodedBytes);
        return decodedData;
    }

    private static byte[] encodeTriple(byte decodedData[], int decodedIndex, int nDecodedBytes, byte encodedData[], int encodedIndex)
    {
        if(nDecodedBytes == 1)
        {
            byte a = decodedData[decodedIndex];
            byte b = 0;
            encodedData[encodedIndex] = (byte)encodedChar[a >>> 2 & 0x3f];
            encodedData[encodedIndex + 1] = (byte)encodedChar[(a << 4 & 0x30) + (b >>> 4 & 0xf)];
            encodedData[encodedIndex + 2] = 61;
            encodedData[encodedIndex + 3] = 61;
        } else
        if(nDecodedBytes == 2)
        {
            byte a = decodedData[decodedIndex];
            byte b = decodedData[decodedIndex + 1];
            byte c = 0;
            encodedData[encodedIndex] = (byte)encodedChar[a >>> 2 & 0x3f];
            encodedData[encodedIndex + 1] = (byte)encodedChar[(a << 4 & 0x30) + (b >>> 4 & 0xf)];
            encodedData[encodedIndex + 2] = (byte)encodedChar[(b << 2 & 0x3c) + (c >>> 6 & 3)];
            encodedData[encodedIndex + 3] = 61;
        } else
        if(nDecodedBytes == 3)
        {
            byte a = decodedData[decodedIndex];
            byte b = decodedData[decodedIndex + 1];
            byte c = decodedData[decodedIndex + 2];
            encodedData[encodedIndex] = (byte)encodedChar[a >>> 2 & 0x3f];
            encodedData[encodedIndex + 1] = (byte)encodedChar[(a << 4 & 0x30) + (b >>> 4 & 0xf)];
            encodedData[encodedIndex + 2] = (byte)encodedChar[(b << 2 & 0x3c) + (c >>> 6 & 3)];
            encodedData[encodedIndex + 3] = (byte)encodedChar[c & 0x3f];
        } else
        {
            throw new IllegalArgumentException("length must be in the range [1,3]");
        }
        return encodedData;
    }

    static 
    {
        decoded6BitValue = new byte[256];
        for(int i = 0; i < 256; i++)
        {
            decoded6BitValue[i] = -1;
        }

        for(int i = 65; i <= 90; i++)
        {
            decoded6BitValue[i] = (byte)(i - 65);
        }

        for(int i = 97; i <= 122; i++)
        {
            decoded6BitValue[i] = (byte)((26 + i) - 97);
        }

        for(int i = 48; i <= 57; i++)
        {
            decoded6BitValue[i] = (byte)((52 + i) - 48);
        }

        decoded6BitValue[43] = 62;
        decoded6BitValue[47] = 63;
    }
}

