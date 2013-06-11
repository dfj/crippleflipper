import java.io.IOException;
import java.io.RandomAccessFile;

/*
 * Cripple a JAR or other zip file by flipping a bit in the first local file header signature.
 * This process can be reversed by flipping the bit back.
 * Useful for rendering JARs non-executable.
 */

public class CrippleFlipper
{
	// The header value is little-endian 0x04034b50. The file can be crippled by setting it to 0x05034b50.
	private void modifyFirstLocalFileHeaderSig(String fileName, byte val)
	{
                try
                {
                        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
                        raf.seek(3);
                        raf.write(new byte[]{val});
                        raf.close();
                }
                catch (Exception ex)
                {
                        ex.printStackTrace();
                }
	}

	public void crippleFile(String fileName)
	{
		byte val = 5;
		this.modifyFirstLocalFileHeaderSig(fileName, val);
	}

        public void unCrippleFile(String fileName)
        {
                byte val = 4;
                this.modifyFirstLocalFileHeaderSig(fileName, val);
        }

	public static void main(final String[] args) throws IOException
	{
		try
		{
			CrippleFlipper cf = new CrippleFlipper();
			if (args[1].equals("c"))
				cf.crippleFile(args[0]);
			if (args[1].equals("u"))
				cf.unCrippleFile(args[0]);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
