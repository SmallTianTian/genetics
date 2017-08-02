package com.smalltiantian.genetics;

public class Genetics {
    private static Genetics genetics = null;

    public static Genetics init() {
        return init(null);
    }

    public static Genetics init(String configPath) {
        if (genetics != null)
            throw new IllegalStateException("Error : You already init this(`Genetics`).");
        if (configPath != null) {
            
        } else {
            
        }
        return genetics;
    }

    public void begin() {
        
    }
}
