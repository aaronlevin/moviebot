package com.sortable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MovieStore
{
	private String[] ids;
	static private String cache = "0111161, 0068646, 0071562, 0110912, 0060196, 0050083, 0468569, 0108052, 0167260, 0137523, 0080684, 0120737, 0073486, 1375666, 0099685, 0076759, 0047478, 0109830, 0133093, 0167261, 0317248, 0064116, 0114369, 0102926, 0034583, 0114814, 0082971, 0047396, 0054215, 0038650, 0110413, 0043014, 0209144, 0120586, 0078788, 0103064, 1853728, 0120815, 0057012, 0078748, 0021749, 0053125, 0245429, 1345836, 0027977, 0033467, 0081505, 0088763, 0253474, 0407887, 0052357, 0022100, 0169547, 0075314, 0118799, 0050825, 0036775, 0435761, 0090605, 0910970, 0405094, 0066921, 1675434, 0172495, 0120689, 0211915, 0056172, 0032553, 0482571, 0056592, 0105236, 0082096, 0180093, 0041959, 0040897, 0338013, 0095765, 0087843, 0110357, 0086190, 0071315, 0119488, 0093058, 0112573, 0071853, 0364569, 0045152, 0053291, 0086879, 0017136, 0042876, 0040522, 0062622, 0119698, 0105695, 0042192, 0053604, 0097576, 0070735, 0081398, 0050212, 0051201, 0095016, 0095327, 0372784, 1832382, 0457430, 0363163, 0055630, 0208092, 0059578, 0031679, 0057115, 0361748, 1049413, 0047296, 0080678, 0114709, 0050976, 0113277, 0017925, 0033870, 0083658, 0032976, 1205489, 0050986, 0116282, 0012349, 0086250, 0118715, 0052311, 0089881, 0077416, 0061512, 0848228, 0015864, 0401792, 0044079, 0025316, 0477348, 0120735, 0073195, 0167404, 0091763, 0084787, 0395169, 0112641, 0044706, 0064115, 0903624, 0117951, 0032138, 0266697, 1291584, 0075686, 0119217, 1305806, 0038787, 0031381, 0032551, 0758758, 1504320, 0079470, 0434409, 0096283, 0266543, 0892769, 0046912, 0038355, 0074958, 0088247, 0052618, 0469494, 0405159, 0048424, 0092005, 0246578, 0947798, 0107048, 0072890, 0114746, 0454876, 0245712, 0440963, 0053198, 0978762, 0060827, 0083987, 0061722, 0347149, 0049406, 0093779, 0056801, 1010048, 0268978, 0054997, 1655442, 0061184, 0047528, 0056218, 0075148, 0052561, 0065214, 0070047, 0046359, 0040746, 0069281, 0056217, 0072684, 1659337, 0353969, 1136608, 0338564, 0079944, 0120382, 0046250, 0325980, 0107207, 0401383, 1220719, 0198781, 0382932, 0058461, 1201607, 0044081, 0796366, 1024648, 0101414, 0063522, 0042546, 0986264, 0113247, 0087544, 0020629, 1130884, 0095953, 1255953, 1125849, 0327056, 0013442, 0079522, 0374546, 0107290, 1187043, 0070511, 0319061, 0092067, 0094226, 0036613, 1454029";
	private ArrayList<String> cacheList;
	private String printableTitle;

	public MovieStore()
	{
		this.ids = new String[250];
		int i = 0;
		StringTokenizer st = new StringTokenizer(cache, ", ");
		while (st.hasMoreTokens())
		{
			ids[i++] = st.nextToken();
		}
		cacheList = new ArrayList<String>();
		printableTitle = "";
	}

	public String[] getIds()
	{
		return ids;
	}

	public String[] getTriviaForMovieId(String id)
	{
		String imdbMain = "http://www.imdb.com/title/tt" + id + "/trivia";
		return soUgly(parseTrivia(getURL(imdbMain)).toArray());
	}

	public static String[] soUgly(Object[] o)
	{
		String[] ret = new String[o.length];
		for (int i = 0; i < o.length; i++)
		{
			ret[i] = (String) o[i];
		}
		return ret;
	}

	private static String getURL(String url)
	{
		HttpURLConnection connection = null;
		OutputStreamWriter wr = null;
		BufferedReader rd = null;
		StringBuilder sb = null;
		String line = null;

		URL serverAddress = null;

		try
		{
			serverAddress = new URL(url);
			// set up out communications stuff
			connection = null;

			// Set up the initial connection
			connection = (HttpURLConnection) serverAddress.openConnection();
			connection
					.setRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.57 Safari/537.17");
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);

			connection.connect();

			// get the output stream writer and write the output to the server
			// not needed in this example
			// wr = new OutputStreamWriter(connection.getOutputStream());
			// wr.write("");
			// wr.flush();

			// read the result from the server
			rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			sb = new StringBuilder();

			while ((line = rd.readLine()) != null)
			{
				sb.append(line + '\n');
			}

			return sb.toString();

		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (ProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			// close the connection, set all objects to null
			connection.disconnect();
			rd = null;
			sb = null;
			wr = null;
			connection = null;
		}
		return "";
	}

	private static ArrayList<String> getBlacklist(String title)
	{
		ArrayList<String> blacklist = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(title, "-:1234567890");
		while (st.hasMoreTokens())
		{
			String add = st.nextToken().trim();
			if (add.toLowerCase().startsWith("the "))
				add = add.substring(4);
			blacklist.add(add);
		}
		return blacklist;
	}

	private ArrayList<String> parseTrivia(String html)
	{
		String titlebeg = "<title>";
		String titleend = " (";
		int titleindexbeg = html.indexOf(titlebeg);
		int titleindexend = html.indexOf(titleend, titleindexbeg);
		String title = html.substring(titleindexbeg + titlebeg.length(),
				titleindexend).trim();
		this.printableTitle = title;
		ArrayList<String> blacklist = getBlacklist(title);
		this.cacheList = blacklist;
		//System.out.println(blacklist);

		ArrayList<String> trivia = new ArrayList<String>();
		String beg = "<div class=\"sodatext\">";
		int beglength = beg.length();
		String end = "</div>";
		int at = 0;
		int index = 0;
		while ((index = html.indexOf(beg, at)) >= 0)
		{
			at = index + beglength;
			int endIndex = html.indexOf(end, at);
			// System.out.println(html.substring(at, endIndex));
			String triviaSnippet = fixTriviaSnippet(html
					.substring(at, endIndex));
			boolean add = true;
			for (String black : blacklist)
			{
				if (triviaSnippet.indexOf(black) >= 0)
				{
					add = false;
					break;
				}
			}
			if (triviaSnippet.indexOf("&nbsp;") >= 0)
				add = false;
			if (add)
				trivia.add(triviaSnippet);
		}
		return trivia;
	}

	private static String fixTriviaSnippet(String snip)
	{
		StringBuilder snipb = new StringBuilder(snip);
		int index = snipb.indexOf("<br />");
		if (index >= 0)
		{
			snipb.delete(index, index + 6);
		}
		int index2 = snipb.indexOf("<a");
		while (index2 >= 0)
		{
			int closing = snipb.indexOf(">", index2);
			snipb.delete(index2, closing + 1);
			index2 = snipb.indexOf("<a", index2);
		}
		int index3 = snipb.indexOf("</a>");
		while (index3 >= 0)
		{
			snipb.delete(index3, index3 + 4);
			index3 = snipb.indexOf("</a>", index3);
		}
		return snipb.toString().trim();
	}

	public boolean checkGuess(String guess)
	{
		String myGuess = guess.toLowerCase();
		for (String answer : cacheList)
		{
			answer = answer.toLowerCase();
			if (myGuess.indexOf(answer) >= 0)
				return true;
		}
		return false;
	}
	
	public String getTitle()
	{
		return printableTitle;
	}

	public static void main(String[] args) throws Exception
	{
		/*MovieStore ms = new MovieStore();
		String[] ss = ms.getTriviaForMovieId("0167260");
		for (String sss : ss)
		{
			System.out.println(sss);
		}
		System.out.println(ms.checkGuess("lord of the rings"));*/
	}
}
