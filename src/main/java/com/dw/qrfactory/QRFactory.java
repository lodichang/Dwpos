package com.dw.qrfactory;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;


public class QRFactory {

	public static void main(String [] args) throws UnsupportedEncodingException{
		String addString="scode=100&branchcode=1234&mcode=123&tickdate=20170419&ddh=123456asdfgh77&amount=120.04";
		byte[] encodeBase64 = Base64.encodeBase64(addString.getBytes("UTF-8"));
        System.out.println("RESULT: " + new String(encodeBase64));  
        
		
		
	}
	public static String createQRCodeParam(String mainString,String detailString){
		StringBuffer sb = new StringBuffer();
		sb.append(mainString).append(detailString);
		byte[] encodeBase64 = null;
		try {
			encodeBase64 = Base64.encodeBase64(sb.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //.decodeFast(.getBytes("UTF-8"));  
        //System.out.println("RESULT: " + new String(encodeBase64));  
		return new String(encodeBase64);
	}
	
	//生成二维码图片，返回图片地址
	public static String createQRCode(String text, String path, int width, int height,
			String format, String imageName) throws Exception {
		/*int width = 100; // 二维码图片宽度
		int height = 100; // 二维码图片高度
		String format = "jpg";// 二维码的图片格式*/
		
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用字符集编码

		BitMatrix bitMatrix = new MultiFormatWriter().encode(text,
				BarcodeFormat.QR_CODE, width, height, hints);
		// 生成二维码
		File outputFile = new File(path + File.separator + imageName + "."+format);
		MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);

		return path + File.separator + imageName + "."+format;
		/*File file = new File(path + imageName + ".jpg");
		InputStream is = new FileInputStream(file);
		BufferedImage bi;
		bi = ImageIO.read(is);
		java.awt.Image im = (java.awt.Image) bi;
		return im;*/
	}

	/**
     * 直接读入内存，不再保存到磁盘
     * @param text
     * @param width
     * @param height
     * @return
     * @throws Exception
     */
    public static BufferedImage createQRCode2(String text, int width, int height) throws Exception {
    	Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用字符集编码
        hints.put(EncodeHintType.MARGIN, "1");

        BitMatrix bitMatrix = new MultiFormatWriter().encode(text,
                BarcodeFormat.QR_CODE, width, height, hints);

        return    MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
	
	 /**
	 * 使用BufferedImage获取图片尺寸
	 * 
	 * @param src
	 *            源图片路径
	 */
	public static BufferedImage getImageSizeByBufferedImage(String src) {
		
		File file = new File(src);
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		BufferedImage sourceImg = null;
		try {
			sourceImg = javax.imageio.ImageIO.read(is);
			return sourceImg;
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		
	}
	
	public static int getGray(int rgb){  
	    String str=Integer.toHexString(rgb);  
	    int r=Integer.parseInt(str.substring(2,4),16);  
	    int g=Integer.parseInt(str.substring(4,6),16);  
	    int b=Integer.parseInt(str.substring(6,8),16);  
	    //or 直接new个color对象  
	    Color c=new Color(rgb);  
	    r=c.getRed();  
	    g=c.getGreen();  
	    b=c.getBlue();  
	    int top=(r+g+b)/3;  
	    return (int)(top);  
    }  
	      
    /** 
     * 自己加周围8个灰度值再除以9，算出其相对灰度值 
     * @param gray 
     * @param x 
     * @param y 
     * @param w 
     * @param h 
     * @return 
     */  
    public static int  getAverageColor(int[][] gray, int x, int y, int w, int h)  
    {  
        int rs = gray[x][y]  
            + (x == 0 ? 255 : gray[x - 1][y])  
            + (x == 0 || y == 0 ? 255 : gray[x - 1][y - 1])  
            + (x == 0 || y == h - 1 ? 255 : gray[x - 1][y + 1])  
            + (y == 0 ? 255 : gray[x][y - 1])  
            + (y == h - 1 ? 255 : gray[x][y + 1])  
            + (x == w - 1 ? 255 : gray[x + 1][ y])  
            + (x == w - 1 || y == 0 ? 255 : gray[x + 1][y - 1])  
            + (x == w - 1 || y == h - 1 ? 255 : gray[x + 1][y + 1]);  
        return rs / 9;  
    } 
    
    
    
    
    
    public static void test01(String str){    
        int width = 300;        
        int height = 20;        
        //String s = "你好";  
        String s = str;
            
        File file = new File("D:/image.jpg");        
            
        Font font = new Font("Serif", Font.BOLD, 10);        
       //创建一个画布    
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
        
       //获取画布的画笔    
        Graphics2D g2 = (Graphics2D)bi.getGraphics();        
       //开始绘图    
        g2.setBackground(Color.WHITE);        
        g2.clearRect(0, 0, width, height);        
       /* g2.setPaint(new Color(0,0,255));        
        g2.fillRect(0, 0, 100, 10);    
        g2.setPaint(new Color(253,2,0));    
        g2.fillRect(0, 10, 100, 10);    
        g2.setPaint(Color.red);    */
        g2.setPaint(Color.black);
  
           
        FontRenderContext context = g2.getFontRenderContext();        
        Rectangle2D bounds = font.getStringBounds(s, context);        
        double x = (width - bounds.getWidth()) / 2;        
        double y = (height - bounds.getHeight()) / 2;        
        double ascent = -bounds.getY();        
        double baseY = y + ascent;        
  
       //绘制字符串    
        g2.drawString(s, (int)x, (int)baseY);     
  
        try {    
           //将生成的图片保存为jpg格式的文件。ImageIO支持jpg、png、gif等格式    
           ImageIO.write(bi, "jpg", file);    
       } catch (IOException e) {    
           System.out.println("生成图片出错........");    
           e.printStackTrace();    
       }        
  
   }  
    
   
   
}
