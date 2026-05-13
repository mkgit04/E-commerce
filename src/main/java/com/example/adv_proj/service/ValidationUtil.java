package com.example.adv_proj.service;

import java.util.regex.Pattern;

public class ValidationUtil {

    // Validation patterns
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,50}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^.{6,100}$");
    private static final Pattern PRODUCT_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s\\-.,()]{1,255}$");

    // Validation messages
    public static final String INVALID_USERNAME = "Username must be 3-50 alphanumeric characters (underscore allowed)";
    public static final String INVALID_PASSWORD = "Password must be 6-100 characters";
    public static final String INVALID_PRODUCT_NAME = "Product name must be 1-255 characters (alphanumeric, spaces, hyphens, periods, commas, parentheses) and cannot be numbers only";
    public static final String INVALID_PRICE = "Price must be a positive number (max 999999.99)";
    public static final String INVALID_RATING = "Rating must be an integer between 1 and 5";
    public static final String INVALID_PRODUCT_ID = "Product ID must be a positive integer";
    public static final String MISSING_REQUIRED_FIELD = "Required field is missing";

    /**
     * Validates username format and length.
     * @param username The username to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    /**
     * Validates password length.
     * @param password The password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isBlank()) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Validates product name format and length.
     * @param productName The product name to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidProductName(String productName) {
        if (productName == null || productName.isBlank()) {
            return false;
        }
        String trimmed = productName.trim();
        
        // Check if it matches the allowed format
        if (!PRODUCT_NAME_PATTERN.matcher(trimmed).matches()) {
            return false;
        }
        
        // Check if it contains only digits (and spaces), which is not allowed
        if (isProductNameNumbersOnly(trimmed)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Checks if product name contains only digits and whitespace.
     * @param productName The product name to check
     * @return true if it contains only digits and spaces, false otherwise
     */
    private static boolean isProductNameNumbersOnly(String productName) {
        return productName.replaceAll("[\\s]", "").matches("^[0-9]+$");
    }

    /**
     * Validates product price.
     * @param priceString The price as a string to parse and validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPrice(String priceString) {
        if (priceString == null || priceString.isBlank()) {
            return false;
        }
        try {
            float price = Float.parseFloat(priceString);
            return price > 0 && price <= 999999.99f;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates rating value.
     * @param ratingString The rating as a string to parse and validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidRating(String ratingString) {
        if (ratingString == null || ratingString.isBlank()) {
            return false;
        }
        try {
            int rating = Integer.parseInt(ratingString);
            return rating >= 1 && rating <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates product ID.
     * @param productIdString The product ID as a string to parse and validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidProductId(String productIdString) {
        if (productIdString == null || productIdString.isBlank()) {
            return false;
        }
        try {
            int productId = Integer.parseInt(productIdString);
            return productId > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a string is null or blank.
     * @param value The string to check
     * @return true if null or blank, false otherwise
     */
    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }
}
