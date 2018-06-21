package split;
/**
 * 
 * @ClassName News
 * @Description 对信息的封装
 * @author 吴扬颉
 * @date 2018年6月4日
 *
 */
public class News{
	private String id;
	private String title;
	private String url;
	private String description;
	private String time;
	private String user;
	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getUrl(){
		return this.url;
	}
	public void setUrl(String url){
		this.url = url;
	}
	public String getDescription(){
		return this.description;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getTime(){
		return this.time;
	}
	public void setTime(String time){
		this.time = time;
	}
	public String getUser(){
		return this.user;
	}
	public void setUser(String user){
		this.user = user;
	}
}
