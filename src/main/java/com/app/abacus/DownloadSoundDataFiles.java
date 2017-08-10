/**
 * 
 */
package com.app.abacus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

/**
 * @author prashant.joshi
 *
 */
public class DownloadSoundDataFiles {
	
	private String VAAS_APPLICATION = "EVAL_7682787";
	private String VAAS_PASSWORD = "";
	private String VAAS_URL = "http://vaas.acapela-group.com/Services/Synthesizer/";
	private String VAAS_LOGIN = "";
	
	List<String> listOfInstructions;
	List<String> listOfActualIns;
	
	String selectedVoice;
	
	String[] listOfHtmlChars = {"<B>","</B>","<b>","</b>","<U>","</U>","<u>","</u>","<I>","</I>","<i>","</i>","<font color=\"red\">","<font color=\"cyan\">","</font>"};
	
	public void printInstructions() {
		for (String string : listOfActualIns) {
			System.out.println(string);
		}
	}
	
	public void loadInstructions() {
		listOfActualIns = new ArrayList<String>();
		for (String instruction : listOfInstructions) {
			if(!instruction.startsWith("<action>")) {
				instruction = instruction.trim();
				instruction = replaceHTMLCharacters(instruction);
				listOfActualIns.add(instruction);
			} else {
				instruction = null;
			}
		}
	}
	
	private String replaceHTMLCharacters(String instruction) {
		for (String htmlChar : listOfHtmlChars) {
			instruction = instruction.replaceAll(htmlChar, "");
		}
		return instruction;
	}
	
	/**
	 * Method used to read the text
	 */
	public void downloadSndText() throws IOException {
		int counter = 1;
		String fileName = "";
		//counter++;
		for (String insTxt : listOfActualIns) {
			String[] txt = insTxt.split("\n");
			String txtInput = "";
			for (String data : txt) {
				if (!data.trim().equalsIgnoreCase("")) {
					txtInput = txtInput + data + " \\pau=100\\ ";
					//txtInput = txtInput + data;
				}
			}
			
			System.out.println(txtInput);
			
			
			String url = getSndURL(getSelectedVoice(), txtInput);
			System.out.println("URL : " + url);
			
			fileName = counter + ".wav";
			counter++;
			if(url != null && url.trim().length() != 0)
				downSndWAVFile(url, fileName);
		}
	}
	
	private void downSndWAVFile(String url, String fileName) {
		try {
			URI uri = new URI(url);
			URLConnection conn = uri.toURL().openConnection();
		    InputStream is = conn.getInputStream();
	
		    File file = new File(fileName);
		    if(file.exists()) {
		    	file.delete();
		    }
		    OutputStream outstream = new FileOutputStream(new File(fileName));
		    byte[] buffer = new byte[4096];
		    int len;
		    while ((len = is.read(buffer)) > 0) {
		        outstream.write(buffer, 0, len);
		    }
		    outstream.close();
		    is.close();
		    
		    System.out.println(fileName + " created!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Speaks given text with given voice. */
	public String getSndURL(String voice, String text)
			throws IllegalArgumentException, IllegalStateException, IOException {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("prot_vers", "2");
		params.put("cl_env", "APACHE_2.2.9_PHP_5.5");
		params.put("cl_vers", "1-00");
		params.put("cl_login", VAAS_LOGIN);
		params.put("cl_app", VAAS_APPLICATION);
		params.put("cl_pwd", VAAS_PASSWORD);
		params.put("req_voice", voice);
		params.put("req_snd_type", "WAV");
		params.put("req_text",text);
		params.put("req_timeout", "120");
		HttpResponse response = doHttpPost(VAAS_URL, params, null, null,
				new DefaultHttpClient());
		String responseBody = getResponseBody(response);
		String vars[] = responseBody.split("&");
		for (int n = 0; n < vars.length; n++) {
			String var[] = vars[n].split("=");
			if (var[0].equals("snd_url")) {
				System.out.println("URL : " + var[1]);
				return var[1];
			}
		}
		return null;
	}
	
	/** Internal HTTP post helper function */
	private static HttpResponse doHttpPost(String mUrl,
			HashMap<String, String> hm, String username, String password,
			DefaultHttpClient httpClient) {
		HttpResponse response = null;
		if (username != null && password != null) {
			httpClient.getCredentialsProvider().setCredentials(
					new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
					new UsernamePasswordCredentials(username, password));
		}
		HttpPost postMethod = new HttpPost(mUrl);
		if (hm == null)
			return null;
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			Iterator<String> it = hm.keySet().iterator();
			String k, v;
			while (it.hasNext()) {
				k = it.next();
				v = hm.get(k);
				nameValuePairs.add(new BasicNameValuePair(k, v));
			}
			postMethod.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
			postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = httpClient.execute(postMethod);
		} catch (Exception e) { /** Eating exception */ } 
		return response;
	}

	/** Internal HTTP post get response body functions */
	private String getResponseBody(HttpResponse response) {
		String response_text = null;
		HttpEntity entity = null;
		try {
			entity = response.getEntity();
			response_text = _getResponseBody(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (IOException e1) {
				}
			}
		}
		return response_text;
	}

	/** Internal function to get response text from Http response */
	private String _getResponseBody(final HttpEntity entity)
			throws IOException, ParseException {
		InputStream instream = entity.getContent();
		if (instream == null) {
			return "";
		}
		if (entity.getContentLength() > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
					"HTTP entity too large to be buffered in memory");
		}
		String charset = getContentCharSet(entity);
		if (charset == null) {
			charset = HTTP.DEFAULT_CONTENT_CHARSET;
		}
		Reader reader = new InputStreamReader(instream, charset);
		StringBuilder buffer = new StringBuilder();
		try {
			char[] tmp = new char[1024];
			int l;
			while ((l = reader.read(tmp)) != -1) {
				buffer.append(tmp, 0, l);
			}
		} finally {
			reader.close();
		}
		return buffer.toString();
	}

	/** Internal function to fetch charset from Http Response */
	private String getContentCharSet(final HttpEntity entity)
			throws ParseException {
		String charset = null;
		if (entity.getContentType() != null) {
			HeaderElement values[] = entity.getContentType().getElements();
			if (values.length > 0) {
				NameValuePair param = values[0].getParameterByName("charset");
				if (param != null) {
					charset = param.getValue();
				}
			}
		}
		return charset;
	}

	/**
	 * @return the listOfInstructions
	 */
	public List<String> getListOfInstructions() {
		return listOfInstructions;
	}

	/**
	 * @param listOfInstructions the listOfInstructions to set
	 */
	public void setListOfInstructions(List<String> listOfInstructions) {
		this.listOfInstructions = listOfInstructions;
	}

	/**
	 * @return the selectedVoice
	 */
	public String getSelectedVoice() {
		return selectedVoice;
	}

	/**
	 * @param selectedVoice the selectedVoice to set
	 */
	public void setSelectedVoice(String selectedVoice) {
		this.selectedVoice = selectedVoice;
	}
}