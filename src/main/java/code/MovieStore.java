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

public class MovieStore
{
	private String[] ids;

	public MovieStore()
	{
		this.ids = new String[6];
		ids[0] = "0435761";
		ids[1] = "0133093";
		ids[2] = "0097757";
		ids[3] = "1853728";
		ids[4] = "0119217";
		ids[5] = "1205489";
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

	private static ArrayList<String> parseTrivia(String html)
	{
		ArrayList<String> trivia = new ArrayList<String>();
		String beg = "<div class=\"sodatext\">";
		int beglength = beg.length();
		String end = "</div>";
		int at = 0;
		int index = 0;
		while ((index = html.indexOf(beg, at)) > 0)
		{
			at = index + beglength;
			int endIndex = html.indexOf(end, at);
			// System.out.println(html.substring(at, endIndex));
			String triviaSnippet = html.substring(at, endIndex);
			trivia.add(fixTriviaSnippet(triviaSnippet));
		}
		return trivia;
	}

	private static String fixTriviaSnippet(String snip)
	{
		StringBuilder snipb = new StringBuilder(snip);
		int index = snipb.indexOf("<br />");
		if (index > 0)
		{
			snipb.delete(index, index + 6);
		}
		int index2 = snipb.indexOf("<a");
		while (index2 > 0)
		{
			int closing = snipb.indexOf(">", index2);
			snipb.delete(index2, closing + 1);
			index2 = snipb.indexOf("<a", index2);
		}
		int index3 = snipb.indexOf("</a>");
		while (index3 > 0)
		{
			snipb.delete(index3, index3 + 4);
			index3 = snipb.indexOf("</a>", index3);
		}
		return snipb.toString().trim();
	}

	public static void main(String[] args) throws Exception
	{
		MovieStore ms = new MovieStore();
		String[] s = ms.getTriviaForMovieId("0435761");
		for (String ss : s)
		{
			System.out.println(ss);
		}
	}
}
