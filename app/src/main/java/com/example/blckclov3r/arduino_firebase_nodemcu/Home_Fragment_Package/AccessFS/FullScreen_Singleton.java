package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.AccessFS;

public class FullScreen_Singleton {
    private boolean imageFS = true;

    private FullScreen_Singleton(){}

    private static FullScreen_Singleton instance = null;

    public static FullScreen_Singleton getInstance(){
        if(instance == null){
            synchronized (FullScreen_Singleton.class) {
                if(instance == null) {
                    instance = new FullScreen_Singleton();
                }
            }
        }
        return instance;
    }

    public void setImageFS(boolean imageFS){
        this.imageFS = imageFS;
    }

    public boolean getImageFS(){
        return imageFS;
    }
}
