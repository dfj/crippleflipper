import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class CrippleFlipper
{
	public static void main(final String[] args) throws IOException
	{
		try
		{
			FileInputStream fis = new FileInputStream(new File(args[0]));
			FileOutputStream fos = new FileOutputStream(args[1]);

			byte[] sigBytes = new byte[]{80, 75, 5, 6};
			byte[] crippleSigBytes = new byte[]{80, 75, 5, 7};
			byte[] lastBytes = new byte[4];
			byte[] bytes = new byte[1];
			boolean flipComplete = false;

			int retval = 0;
			retval = fis.read(bytes);

			while (retval != -1)
			{
				if (!flipComplete)
				{
					lastBytes[0] = lastBytes[1];
					lastBytes[1] = lastBytes[2];
					lastBytes[2] = lastBytes[3];
					lastBytes[3] = bytes[0];

					if (Arrays.equals(sigBytes, lastBytes))
					{
						System.out.println("Found End of central dir signature (0x06054b50), crippling by changing to 0x07054b50");
						bytes[0]++;
						flipComplete = true;
					}
					if (Arrays.equals(crippleSigBytes, lastBytes))
					{
						System.out.println("Found crippled End of central dir signature (0x07054b50), uncrippling by changing to 0x06054b50");
						bytes[0]--;
						flipComplete = true;
					}
				}
				fos.write(bytes);
				retval = fis.read(bytes);
			}
			fos.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
