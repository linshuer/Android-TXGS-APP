package bean;

public class Article {
	int articleid;
	private String title,type,picimage,master,mnickname,date,content;
	
	public Article(){
		this.title ="";
		this.type ="原创";
		this.picimage ="dome_picimage";
		this.master ="";
		this.mnickname="shuer";
		this.date="";
		this.content="";
	}

	

	public int getArticleid() {
		return articleid;
	}



	public void setArticleid(int articleid) {
		this.articleid = articleid;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getPicimage() {
		return picimage;
	}



	public void setPicimage(String picimage) {
		this.picimage = picimage;
	}



	public String getMaster() {
		return master;
	}



	public void setMaster(String master) {
		this.master = master;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}


	public String getMnickname() {
		return mnickname;
	}


	public void setMnickname(String mnickname) {
		this.mnickname = mnickname;
	}

	
	@Override
	public String toString() {
		return "Article [articleid=" + articleid + ", title=" + title + ", type="
				+ type + ", picimage=" + picimage+ ", master=" + master+ ", mnickname=" + mnickname + ", date=" + date + ", content=" + content +"]";
	}
}
