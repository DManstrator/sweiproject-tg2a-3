package def;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LampenSpass extends Thread {
	
	private final String HUE_IP = "10.28.9.120";
	private final String HUE_KEY = "197ea42c25303cef1a68c4042ed56887";

	
	void doLampe(int hue, boolean blink) throws IOException  {
		String requestURL = String.format("http://%s/api/%s/groups/0/action", HUE_IP, HUE_KEY);
		
		URL url = new URL(requestURL);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("PUT");
		OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
		String exec;
		System.out.println("Controlling Lamp now");
		if (blink){
			while (true){
				if (this.isInterrupted()){
    				System.out.println("Interrupted");
    				break;
    			}
				try {
					flash();
				} catch(Exception e){
					System.out.println("catchBlock1!");
//					killLamp();
					break;
				}
			}
		}else{
			exec = "{\"on\":true,\"hue\":"+hue+",\"bri\":255,\"sat\":255}";
			out.write(exec);
			out.flush();
		}
			
			//System.out.println(">10 min - relax!");
			//grün -> more than 10 min left
	}
	
	public void flash() throws Exception{
		String requestURL = String.format("http://%s/api/%s/groups/0/action", HUE_IP, HUE_KEY);
		
		URL url = new URL(requestURL);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("PUT");
		OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
		String exec;
		exec = "{\"alert\":\"select\"}";
		out.write(exec);
		out.flush();
		httpCon.getInputStream();
		sleep(1001);
	}
	
	public static void main(String[] args)  {
		LampenSpass lampenSpass = new LampenSpass();
		try {
			lampenSpass.doLampe(0, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
