package com.dw.extended;

import com.dw.Main;
import javafx.concurrent.Task;
import javafx.geometry.NodeOrientation;
import javafx.scene.AccessibleRole;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 緩存網絡圖片,根据posSetting.imagepriority来控制优先级别
 * 1.网络优先network:
 * 1.1.讀取遠程圖片,并獲取文件名,並异步线程將其下載到本地,以防断网的时候使用
 * 1.2.優先顯示遠程圖片,如果断网,則讀取本地文件夾緩存的記錄
 * 2.本地优先local:
 * 2.1读取本地图片,如果不存在,则直接引用网络图片
 * 2.2如果网络正常,每次都会异步线程去更新缓存图片
 */
public class DwImageView extends ImageView {
    private static final String DEFAULT_STYLE_CLASS = "image-view";

    public DwImageView(String url) {

        String imagepriority = Main.posSetting.get("imagepriority");
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        if (url.contains("http://") || url.contains("https://")) {

            switch (imagepriority) {
                //如果是网络优先,则先读取网络图片,如果断网了.或则其他情况,则读取本地.
                case "network":
                    Image image = new Image(url);
                    if (image.isError()) {
                        String imagePath = System.getProperty("user.dir") + File.separator + "image" + File.separator + "cach" + File.separator + getImgName(url);
                        try {
                            image = new Image(new FileInputStream(imagePath));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        setImage(image);

                    } else {
                        setImage(image);
                        downSrc(url);
                    }
                    break;
                //本地优先,先缓存了,然后再读取
                case "local":
                    String imagePath = System.getProperty("user.dir") + File.separator + "image" + File.separator + "cach" + File.separator + getImgName(url);
                    File imageFile = new File(imagePath);
                    //如果不存在,则尝试先下载,然后读取远程
                    if (!imageFile.exists()) {
                        Image imageNetWord = new Image(url);
                        setImage(imageNetWord);

                    } else {
                        try {
                            Image imageLocal = new Image(new FileInputStream(imagePath));
                            setImage(imageLocal);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    downSrc(url);
                    break;
                default:
                    break;

            }
        } else {
            //普通图片加载
            Image image = new Image(url);
            String imagePath = System.getProperty("user.dir") + File.separator + "image" + File.separator + "cach" + File.separator + getImgName(url);
            try {
                image = new Image(new FileInputStream(imagePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            setImage(image);
        }


    }


    /**
     * 獲得URL的圖片名稱
     *
     * @param imgUrl
     * @return
     */
    private static String getImgName(String imgUrl) {
        if (imgUrl == null) {
            return null;
        }
        String[] strs = imgUrl.split("/");
        return strs[strs.length - 1];
    }

    /**
     * 根据图片下载
     *
     * @param urlstr
     */
    private void downSrc(String urlstr) {

        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    if (urlstr.contains("http://") || urlstr.contains("https://")) {
                        String downPath = System.getProperty("user.dir") + File.separator + "image" + File.separator + "cach" + File.separator;
                        File downFile = new File(downPath);
                        if (!downFile.exists()) {
                            downFile.mkdir();
                        }
                        String imgName = getImgName(urlstr);
                        URL url = new URL(urlstr);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5 * 1000);
                        conn.setRequestMethod("GET");
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                            InputStream is = conn.getInputStream();
                            File destFile = new File(downPath, imgName);
                            OutputStream os = new FileOutputStream(destFile);
                            byte[] buffer = new byte[400];
                            int length = 0;
                            while ((length = is.read(buffer)) > 0) {
                                os.write(buffer, 0, length);
                            }
                            conn.disconnect();
                            is.close();
                            os.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
        new Thread(task).start();
    }

}
