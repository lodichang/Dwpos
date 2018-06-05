package com.dw.qrfactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;


/** 
 * 创建文字图片 
 * @author yuki_ho 
 * 
 */  
public class FontImage {  
     // 默认格式  
     private static final String FORMAT_NAME = "JPG";  
     // 默认 宽度  
     private static final int WIDTH = 100;  
     // 默认 高度  
     private static final int HEIGHT = 20;  
             
      /** 
       * 创建图片 
       * @param content 内容 
       * @param font  字体 
       * @param width 宽 
       * @param height 高 
       * @return 
       */  
     private static BufferedImage createImage(String content,Font font,Integer width,Integer height){
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);     
            Graphics2D g2 = (Graphics2D)bi.getGraphics();     
            g2.setBackground(Color.WHITE);     
            g2.clearRect(0, 0, width, height);     
            g2.setPaint(Color.BLACK);
            g2.setFont(font);


            FontRenderContext context = g2.getFontRenderContext();
            Rectangle2D bounds = font.getStringBounds(content, context);
            double x = (width - bounds.getWidth()) / 2;
            double y = (height - bounds.getHeight()) / 2;     
            double ascent = -bounds.getY();     
            double baseY = y + ascent;     
                 
            //g2.drawString(content, (int)x, (int)baseY);居中打印
            g2.drawString(content, 10, (int)baseY);//距離左邊距10
            
              
            return bi;  
     }  
       
     /** 
      * 获取 图片  
      * @param content 内容 
      * @param font  字体 
      * @param width 宽 
      * @param height 高 
      * @return 
      */  
     public static BufferedImage getImage(String content,Font font,Integer width,Integer height) {
        width=width==null?WIDTH:width;  
        height=height==null?HEIGHT:height;  
        if(null==font) {
        	//font = new Font("Serif",Font.BOLD,11);
            try {
                String fontsPath = System.getProperty("user.dir") + File.separator + "fonts" + File.separator + "NotoSansCJKtc-Light.ttf";
                Font temp = Font.createFont(Font.TRUETYPE_FONT, new File(fontsPath));
                font = temp.deriveFont(Font.PLAIN, 14);

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
         return createImage(content, font, width, height);  
     }  
       
     /** 
      * 获取 图片 
      * @param content 内容 
      * @param width 宽 
      * @param height 高 
      * @return 
      */  
     public static BufferedImage getImage(String content,Integer width,Integer height){  

         return getImage(content, null,width, height);
     }  
       
     /** 
      * 获取图片 
      * @param content 内容 
      * @return 
      */  
     public static BufferedImage getImage(String content){  
         return getImage(content, null, null);
     }  
       
     /** 
      *  获取图片 
      * @param content 内容 
      * @param font 字体 
      * @param width 宽 
      * @param height 高 
      * @param destPath 输出路径 
      * @throws IOException  
      */  
     public static void getImage(String content,Font font ,Integer width,Integer height,String destPath) throws IOException{  
         mkdirs(destPath);  
         String file = UUID.randomUUID().toString()+".jpg";  
         ImageIO.write(getImage(content,font,width,height),FORMAT_NAME, new File(destPath+"/"+file));
     }  
      
     /** 
      * 获取图片 
      * @param content 内容 
      * @param font 字体 
      * @param width 宽 
      * @param height 高 
      * @param output 输出流 
      * @throws IOException 
      */  
     public static void getImage(String content,Font font,Integer width,Integer height, OutputStream output) throws IOException{  
         ImageIO.write(getImage(content,font,width,height), FORMAT_NAME, output);
     }  
       
     /** 
      * 获取图片 
      * @param content 内容 
      * @param width 宽 
      * @param height 高 
      * @param destPath 输出路径 
      * @throws IOException 
      */  
     public static void getImage(String content,Integer width,Integer height,String destPath) throws IOException{
        getImage(content, null, width, height, destPath);  
     }  
      
     /** 
      * 获取图片 
      * @param content 内容 
      * @param width 宽 
      * @param height 高 
      * @param output 输出流 
      * @throws IOException 
      */  
     public static void getImage(String content,Integer width,Integer height, OutputStream output) throws IOException{  
        getImage(content, null, width, height, output);  
     }  
       
  
       /** 
        * 创建 目录 
        * @param destPath 
        */  
       public static void mkdirs(String destPath) {  
            File file =new File(destPath);     
            //当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)  
            if (!file.exists() && !file.isDirectory()) {  
                file.mkdirs();  
            }  
        }  
       
     public static void main(String[] args) throws Exception {  
         getImage("매운 돼지 고기 고기 토스트", 200, 20, "d:");


     }
}  