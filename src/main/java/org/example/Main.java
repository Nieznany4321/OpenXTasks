package org.example;

import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws MalformedURLException {

        var url = new URL("https://fakestoreapi.com/users");
        var url2 = new URL("https://fakestoreapi.com/products");
        var url3 = new URL("https://fakestoreapi.com/carts");

        // task 1
        System.out.println("task 1 \n");
        String users = Request.request(url);
        String products = Request.request(url2);
        String carts = Request.request(url3);
        // task 2
        System.out.println("task 2 \n");
        Request.requestProduct(products);
        // task 3
        System.out.println("task 3 \n");
        Request.cartHighestValue(carts, users);
        // task 4
        System.out.println("task 4 \n");
        Request.calculateDistance(users);


    }

}