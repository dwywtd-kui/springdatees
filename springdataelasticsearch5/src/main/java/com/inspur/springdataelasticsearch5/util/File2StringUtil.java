package com.inspur.springdataelasticsearch5.util;


import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

public class File2StringUtil {

    /**
     * 将word文档内容提取为String
     * @param path  word文档文件地址信息
     * @return word文档内容String
     * @throws Exception
     */
    public static String doc2String(String path) throws Exception {
        //创建一个字符串来接收文档内容
        String result=null;

        if(path.endsWith(".docx")){         //如果word文档类型为docx
            OPCPackage opcPackage = POIXMLDocument.openPackage(path);
            POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
            result = extractor.getText();
            extractor.close();
        }else if(path.endsWith(".doc")){    //如果word文档类型为doc
//            InputStream is = new FileInputStream(path);
//            WordExtractor we = new WordExtractor(is);
//            result=we.getText();
//            we.close();
            OPCPackage opcPackage = POIXMLDocument.openPackage(path);
            POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
            result = extractor.getText();
            extractor.close();
        }else {
            System.out.println("请提供word文档！------------");
        }
        return result;
    }

    /**
     * 提取PDF文件中的文本返回String数据
     * @param path 文件路径
     * @return String
     * @throws Exception
     */
    public static String pdf2String(String path) throws Exception{
        String result=null;
        FileInputStream is= new FileInputStream(path);
        PDFParser parser =new PDFParser(new RandomAccessBuffer(is));
        parser.parse();
        PDDocument document=parser.getPDDocument();
        PDFTextStripper stripper =new PDFTextStripper();
        result=stripper.getText(document);
        is.close();
        document.close();
        return result;
    }

    /**
     * 读取txt文件
     * @param filepath
     * @return
     * @throws IOException
     */
    public static String txt2String(String filepath) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filepath));//构造一个BufferedReader类来读取文件
        String line = null;
        while ((line = br.readLine()) != null) {//使用readLine方法，一次读一行
            result.append(line);
        }
        return result.toString();
    }

    public static String file2String(String filepath) throws Exception{
        String result =null;
        if(filepath.endsWith(".docx")){         //如果word文档类型为docx
            InputStream is = new FileInputStream(filepath);
            XWPFDocument doc = new XWPFDocument(is);
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            result= extractor.getText();
            extractor.close();
        }else if(filepath.endsWith(".doc")){    //如果word文档类型为doc
            //InputStream is = new FileInputStream(filepath);
            //WordExtractor we = new WordExtractor(is);
            //result=we.getText();
           // we.close();
            OPCPackage opcPackage = POIXMLDocument.openPackage(filepath);
            POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
            result = extractor.getText();
            extractor.close();
        }else if (filepath.endsWith(".pdf")){
            FileInputStream is= new FileInputStream(filepath);
            PDFParser parser =new PDFParser(new RandomAccessBuffer(is));
            parser.parse();
            PDDocument document=parser.getPDDocument();
            PDFTextStripper stripper =new PDFTextStripper();
            result=stripper.getText(document);
            is.close();
            document.close();
        }else {
            System.out.println("请上传word或PDF文档！------------");
        }
        return result;
    }
}
