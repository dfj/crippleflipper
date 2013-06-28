import java.io.IOException;
import java.io.RandomAccessFile;

/*
 * Cripple a JAR or other zip file by flipping a bit in the end of central directory record
 * This process can be reversed by flipping the bit back.
 * Useful for rendering JARs non-executable.
 */

public class CrippleFlipper
{
	// The end of central directory record value is little-endian 0x06054b50. The file can be crippled by setting it to 0x07054b50
	private void modifyEndOfCentralDirSig(String fileName, byte val, int sig1, int sig2)
	{
                try
                {
                        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
			long filePos = raf.length();
			boolean keepSeeking = true;
			int readVal = 0;
			int prevReadVal = 0;
			while (keepSeeking)
			{
				filePos--;
				raf.seek(filePos);
				prevReadVal = readVal;
				readVal = raf.read();
				if (readVal == sig1 && prevReadVal == sig2)
				{
					raf.write(new byte[]{val});
					keepSeeking = false;
				}
			}
                        raf.close();
                }
                catch (Exception ex)
                {
                        ex.printStackTrace();
                }
	}

	public void crippleFile(String fileName)
	{
		byte val = 7;
		this.modifyEndOfCentralDirSig(fileName, val, 5, 6);
	}

        public void unCrippleFile(String fileName)
        {
                byte val = 6;
                this.modifyEndOfCentralDirSig(fileName, val, 5, 7);
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
