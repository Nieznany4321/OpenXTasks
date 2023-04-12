package org.example;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.HashMap;
import org.json.JSONObject;
import org.json.JSONArray;



public class Request {
    public static String request(URL url) {

        String data = null;
        try {

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                scanner.close();
                data = informationString.toString();
                System.out.println(data + "\n");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    public static void requestProduct(String productsData) {

        try {
                String products = productsData;
                JSONArray jsonarray = new JSONArray(products);
                //Storing unique keys
                HashMap<String, Double> categoryTotal = new HashMap<>();

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String category = jsonobject.getString("category"); //value
                    Double price = jsonobject.getDouble("price");
                    if (categoryTotal.containsKey(category)) {
                        double total = categoryTotal.get(category);
                        categoryTotal.put(category, (double)Math.round(total + price));
                    } else {
                        categoryTotal.put(category, (double) Math.round(price));
                    }

                }
                for (String category : categoryTotal.keySet()) {
                    System.out.println("Category: " + category + " | Total Price: " + categoryTotal.get(category));
                }
                System.out.println("\n");

            }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void cartHighestValue(String cartsData, String usersData) {

        try {

                String users = usersData;
                String carts = cartsData;
                // Converting the JSON string to a JSONArray
                JSONArray jsonArray = new JSONArray(carts);
                JSONArray usersJsonArr = new JSONArray(users);
                JSONObject usersJsonObj = new JSONObject(usersJsonArr);

                int highestValue = Integer.MIN_VALUE;
                int ownerId = Integer.MIN_VALUE;
                int cartId = Integer.MIN_VALUE;
                String ownerFullName = "";
                // Iterating through each cart in the JSONArray
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject cart = jsonArray.getJSONObject(i);
                    int cartValue = 0;
                    // Calculate the value of the current cart by summing up the product quantities
                    JSONArray products = cart.getJSONArray("products");
                    for (int j = 0; j < products.length(); j++) {
                        JSONObject product = products.getJSONObject(j);
                        int productId = product.getInt("productId");
                        int quantity = product.getInt("quantity");
                        cartValue += quantity; // for simplicty I assumed that carts quantity will be its value
                    }

                    if (cartValue > highestValue) {
                        highestValue = cartValue;
                        int userId = cart.getInt("userId");
                        cartId = cart.getInt("id");
                        ownerId = userId;
                        usersJsonObj = usersJsonArr.getJSONObject(ownerId-1).getJSONObject("name");
                        ownerFullName = usersJsonObj.getString("firstname") + " " + usersJsonObj.getString("lastname");
                    }
                }

                // Printing the highest value and its owner's full name
                System.out.println("The highest value cart ID: " + cartId + ", value: " + highestValue + ", owner's full name: " + ownerFullName + "\n");

            }
            catch (Exception e) {
            e.printStackTrace();
        }
    }
    static Double haversineFormula(double lat1, double lon1, double lat2, double lon2){
        double R = 6371; // Radius of the earth in kilometers
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double delta_phi = Math.toRadians(lat2 - lat1);
        double delta_lambda = Math.toRadians(lon2 - lon1);

        double a = Math.sin(delta_phi / 2) * Math.sin(delta_phi / 2)
                + Math.cos(phi1) * Math.cos(phi2) * Math.sin(delta_lambda / 2) * Math.sin(delta_lambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
    static Double calculateDistance(String usersData)
    {
        double highestDistance = 0;
        try {
            String users = usersData;
            JSONArray usersArr = new JSONArray(users);
            int user1 = -1;
            int user2 = -1;
            // each user 1  to n
            for (int i = 0; i < usersArr.length(); i++) {
                JSONObject usersObj = usersArr.getJSONObject(i);
                // pair to the previous user
                for (int j = i+1; j < usersArr.length(); j++) {
                    JSONObject pairUserObj = usersArr.getJSONObject(j);
                    String lat2String  = pairUserObj.getJSONObject("address").getJSONObject("geolocation").getString("lat");
                    String lon2String  = pairUserObj.getJSONObject("address").getJSONObject("geolocation").getString("lat");
                    String lat1String  = usersObj.getJSONObject("address").getJSONObject("geolocation").getString("lat");
                    String lon1String  = usersObj.getJSONObject("address").getJSONObject("geolocation").getString("lat");
                    double lat2 = Double.parseDouble(lat2String);
                    double lon2 = Double.parseDouble(lon2String);
                    double lat1 = Double.parseDouble(lat1String);;
                    double lon1 = Double.parseDouble(lon1String);
                    double distance = haversineFormula(lat1, lon1, lat2, lon2);
                    if (distance > highestDistance) {
                        highestDistance = distance;
                        user1 = i + 1;
                        user2 = j + 1;
                    }

                }

            }
            System.out.println("highestDistance: " + highestDistance + " between User " + user1 + " and User " + user2 );

        }catch (Exception e) {
        e.printStackTrace();
        }

        return highestDistance;
    }
}
