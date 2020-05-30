package json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetGooglePlaceTest2 {
	private static final String MY_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
			+ "location=24.95375,121.22575&"
			+ "radius=500&"
			+ "types=food&"
			+ "name=吃到飽&"
			+ "language=zh-TW&"
			+ "key=AIzaSyAYmC8oUYc9DGAZn8hqZKakFeclhAbTRSI";
	
	public static void main(String[] args) throws IOException, JSONException {
		URL url = new URL(MY_URL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setUseCaches(false);
		
		StringBuilder sb = new StringBuilder();
		
		//讀檔,避免金鑰使用達上限
//		FileReader fr = new FileReader("result2.txt");
//     BufferedReader br = new BufferedReader(fr) ;
//		String str;
		
		//讀URL
		InputStream is = con.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		
//		while ((str = br.readLine()) != null) {
//			sb.append(str);
//		}
//		System.out.println(sb);
		
		//取得最外層的json物件,注意stringbuilder的物件必須轉成string型別才能當成參數放入
		JSONObject jsonobject = new JSONObject(sb.toString());	
		//取得json物件中標籤為results的json陣列
		JSONArray results = jsonobject.getJSONArray("results");
		//分層解析,若向下解析的物件不含陣列型態,使用getJSONObject()後再取得對應型別的資料;反之則先取得陣列後再重複上方步驟
		for (int i = 0 ; i < results.length() ; i++) {
			String name = results.getJSONObject(i).getString("name");
			System.out.println("餐廳名稱 : "+name);
			Double rating = results.getJSONObject(i).getDouble("rating");
			System.out.println("餐廳評分 : "+rating);
			String vicinity = results.getJSONObject(i).getString("vicinity");
			System.out.println("餐廳地址 : "+vicinity);
			JSONObject opening_hours = results.getJSONObject(i).getJSONObject("opening_hours");
			Boolean open_now = opening_hours.getBoolean("open_now");
			if(open_now==true) {
				System.out.println("是否營業 : 是");
			} else {
				System.out.println("是否營業 : 否");
			}
			JSONObject geometry = results.getJSONObject(i).getJSONObject("geometry");
			JSONObject location = geometry.getJSONObject("location");
			Double lat = location.getDouble("lat");
			Double lng = location.getDouble("lng");
			System.out.println("經度 : "+lng);
			System.out.println("緯度 : "+lat);
			System.out.println();
		}

		br.close();
		isr.close();
		is.close();
		
//		fr.close();
		
	}

}
