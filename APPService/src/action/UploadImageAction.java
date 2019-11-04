package action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;

import bean.User;
import dao.DAOFactory;
import dao.UserDAO;



public class UploadImageAction extends HttpServlet {

	public UploadImageAction() {
		super();
	}


	public void destroy() {
		super.destroy();
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		DiskFileItemFactory factory = new DiskFileItemFactory();    
        //获取文件需要上传到的路径    
        String path = request.getRealPath("/headimage");
        File file=new File(path);  
        if(!file.exists()){  
            file.mkdirs();  
        }  
        factory.setRepository(new File(path));    
        //设置 缓存的大小  
        factory.setSizeThreshold(1024*1024) ;    
        //文件上传处理    
        ServletFileUpload upload = new ServletFileUpload(factory);    
        try {    
            //可以上传多个文件    
            List<FileItem> list = (List<FileItem>)upload.parseRequest(request);    
            for(FileItem item : list){    
                //获取属性名字    
                String name = item.getFieldName();    
                //如果获取的 表单信息是普通的 文本 信息    
                if(item.isFormField()){                       
                    //获取用户具体输入的字符串,因为表单提交过来的是 字符串类型的    
                    String value = item.getString() ;    
                    request.setAttribute(name, value);    
                }else{    
                    //获取路径名    
                    String value = item.getName() ;    
                    //索引到最后一个反斜杠    
                    int start = value.lastIndexOf("\\");    
                    //截取 上传文件的 字符串名字，加1是 去掉反斜杠，    
                    String filename = value.substring(start+1);    
                    request.setAttribute(name, filename);    
                    //写到磁盘上    
                    item.write( new File(path,filename) );//第三方提供的    
                    System.out.println("上传成功："+request.getRealPath("/headimage"));  
                    out.write("上传成功"); 
                }    
            }    
                
        } catch (Exception e) {    
            System.out.println("上传失败");  
            out.write("上传失败");
        }    
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request,response);
	}


	public void init() throws ServletException {
		// Put your code here
	}

}
